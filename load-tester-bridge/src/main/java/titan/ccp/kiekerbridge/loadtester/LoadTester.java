package titan.ccp.kiekerbridge.loadtester;

import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import kieker.common.record.IMonitoringRecord;
import org.jctools.queues.MpscArrayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import titan.ccp.kiekerbridge.RecordBridge;
import titan.ccp.kiekerbridge.RecordBridgeStream;
import titan.ccp.models.records.ActivePowerRecord;

/**
 * Generates monitoring records of fixed values using a configurable number of sensors that generate
 * records in a configurable time interval.
 */
public final class LoadTester {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoadTester.class);

  private static final int TERMINATION_TIMEOUT_SECONDS = 10;
  private static final String REDIS_INPUT_COUNTER_KEY = "input_counter";

  private LoadTester() {}

  /**
   * Main method to run the load tester.
   */
  public static void main(final String[] args) {
    // TODO Do this via configuration
    final int producers =
        Integer.parseInt(Objects.requireNonNullElse(System.getenv("PRODUCERS"), "1000"));
    final int periodeInMs =
        Integer.parseInt(Objects.requireNonNullElse(System.getenv("PERIODE_IN_MS"), "100"));
    final int value = Integer.parseInt(Objects.requireNonNullElse(System.getenv("VALUE"), "0"));

    final Random random = new Random();
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);
    // Non-blocking, but lock-free queue
    final Queue<IMonitoringRecord> queue = new MpscArrayQueue<>(8096);
    final AtomicLong counter = new AtomicLong(0);
    final String redisHost = Objects.requireNonNullElse(System.getenv("REDIS_HOST"), "localhost");
    final int redisPort =
        Integer.parseInt(Objects.requireNonNullElse(System.getenv("REDIS_PORT"), "6379"));
    final Jedis jedis = new Jedis(redisHost, redisPort);


    for (int i = 0; i < producers; i++) {
      final long initialDelay = random.nextInt(periodeInMs);
      final int id = i; // Make available in following lambda
      scheduler.scheduleAtFixedRate(() -> {
        try {
          queue.add(new ActivePowerRecord("sensor" + id, System.currentTimeMillis(), value));
        } catch (final IllegalStateException e) {
          LOGGER.warn("Record could not have been added.", e);
        }
        counter.incrementAndGet();
      }, initialDelay, periodeInMs, TimeUnit.MILLISECONDS);
    }


    final RecordBridgeStream<IMonitoringRecord> stream = RecordBridgeStream.from(queue);
    final RecordBridge recordBridge = RecordBridge.ofStream(stream).build();
    recordBridge.start();

    scheduler.scheduleAtFixedRate(() -> {
      final long oldValue = counter.getAndSet(0);
      System.out.println(oldValue); // NOPMD
      jedis.incrBy(REDIS_INPUT_COUNTER_KEY, oldValue);
    }, 1, 1, TimeUnit.SECONDS);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      scheduler.shutdown();
      try {
        scheduler.awaitTermination(TERMINATION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
      } catch (final InterruptedException e) {
        throw new IllegalStateException(e);
      }
      final long oldValue = counter.getAndSet(0);
      System.out.println(oldValue); // NOPMD
      jedis.incrBy(REDIS_INPUT_COUNTER_KEY, oldValue);
      jedis.close();
    }));
  }

}
