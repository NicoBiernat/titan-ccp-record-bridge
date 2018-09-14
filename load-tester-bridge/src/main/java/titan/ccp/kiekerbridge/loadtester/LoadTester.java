package titan.ccp.kiekerbridge.loadtester;

import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.jctools.queues.MpscArrayQueue;

import kieker.common.record.IMonitoringRecord;
import redis.clients.jedis.Jedis;
import titan.ccp.kiekerbridge.KiekerBridge;
import titan.ccp.kiekerbridge.KiekerBridgeStream;
import titan.ccp.models.records.ActivePowerRecord;

public class LoadTester {

	public static void main(final String[] args) {
		// TODO Do this via configuration
		final int producers = Integer.parseInt(Objects.requireNonNullElse(System.getenv("PRODUCERS"), "100"));
		final int periodeInMs = Integer.parseInt(Objects.requireNonNullElse(System.getenv("PERIODE_IN_MS"), "1000"));
		final int value = Integer.parseInt(Objects.requireNonNullElse(System.getenv("VALUE"), "0"));

		final Random random = new Random();
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);
		final Queue<IMonitoringRecord> queue = new MpscArrayQueue<>(1024); // Non-blocking, but lock-free
		final AtomicLong counter = new AtomicLong(0);
		final String redisHost = Objects.requireNonNullElse(System.getenv("REDIS_HOST"), "localhost");
		final int redisPort = Integer.parseInt(Objects.requireNonNullElse(System.getenv("REDIS_PORT"), "6379"));
		final Jedis jedis = new Jedis(redisHost, redisPort);

		for (int i = 0; i < producers; i++) {
			final long initialDelay = random.nextInt(periodeInMs);
			final int id = i; // Make available in following lambda
			scheduler.scheduleAtFixedRate(() -> {
				queue.add(new ActivePowerRecord("sensor" + id, System.currentTimeMillis(), value));
				counter.incrementAndGet();
			}, initialDelay, periodeInMs, TimeUnit.MILLISECONDS);
		}

		final KiekerBridgeStream<IMonitoringRecord> stream = KiekerBridgeStream.from(queue);
		final KiekerBridge kiekerBridge = KiekerBridge.ofStream(stream).build();
		kiekerBridge.start();

		scheduler.scheduleAtFixedRate(() -> {
			final long oldValue = counter.getAndSet(0);
			System.out.println(oldValue);
			jedis.incrBy("input_counter", oldValue);
		}, 1, 1, TimeUnit.SECONDS);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			scheduler.shutdown();
			try {
				scheduler.awaitTermination(10, TimeUnit.SECONDS);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			final long oldValue = counter.getAndSet(0);
			System.out.println(oldValue);
			jedis.incrBy("input_counter", oldValue);
		}));
	}

}
