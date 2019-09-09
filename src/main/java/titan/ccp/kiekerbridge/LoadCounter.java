package titan.ccp.kiekerbridge;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class LoadCounter {

  public static void main(final String[] args) throws InterruptedException {


    final String kafkaBootstrapServers =
        Objects.requireNonNullElse(System.getenv("KAFKA_BOOTSTRAP_SERVERS"), "localhost:9092");
    final String kafkaInputTopic =
        Objects.requireNonNullElse(System.getenv("KAFKA_INPUT_TOPIC"), "input");
    final String kafkaOutputTopic =
        Objects.requireNonNullElse(System.getenv("KAFKA_OUTPUT_TOPIC"), "output");

    final Properties props = new Properties();
    props.setProperty("bootstrap.servers", kafkaBootstrapServers);
    props.setProperty("group.id", "load-counter");
    props.setProperty("enable.auto.commit", "true");
    props.setProperty("auto.commit.interval.ms", "1000");
    props.setProperty("max.poll.records", "1000000");
    props.setProperty("max.partition.fetch.bytes", "134217728"); // 128 MB
    props.setProperty("key.deserializer",
        "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("value.deserializer",
        "org.apache.kafka.common.serialization.StringDeserializer");

    final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(List.of(kafkaInputTopic, kafkaOutputTopic));

    // final AtomicLong sum = new AtomicLong(0);
    // final AtomicLong count = new AtomicLong(0);

    executor.scheduleAtFixedRate(
        () -> {
          final long time = System.currentTimeMillis();
          // final Set<TopicPartition> assignment = consumer.assignment();
          // final Map<TopicPartition, Long> endOffsets = consumer.endOffsets(assignment);
          // for (final Entry<TopicPartition, Long> endOffset : endOffsets.entrySet()) {
          // System.out.println(endOffset.getKey() + ": " + endOffset.getValue());
          // }
          final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
          // records.partitions().stream().filter(tp -> tp.topic().equals("input"));
          final long inputCount =
              StreamSupport.stream(records.records(kafkaInputTopic).spliterator(),
                  false).count();
          final long outputCount =
              StreamSupport.stream(records.records(kafkaOutputTopic).spliterator(),
                  false).count();
          // sum.addAndGet(outputCount);
          // count.incrementAndGet();
          // final long mean = sum.get() / count.get();
          final long elapsedTime = System.currentTimeMillis() - time;
          System.out.println("input," + time + ',' + elapsedTime + ',' + inputCount);
          System.out.println("output," + time + ',' + elapsedTime + ',' + outputCount);
          // System.out.println("output," + time + ',' + outputCount + ',' + mean);
        },
        1,
        1,
        TimeUnit.SECONDS);

  }

}
