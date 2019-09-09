package titan.ccp.kiekerbridge;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import titan.ccp.model.sensorregistry.MutableAggregatedSensor;
import titan.ccp.model.sensorregistry.MutableSensorRegistry;
import titan.ccp.models.records.ActivePowerRecord;

public class LoadGenerator {

  public static void main(final String[] args) throws InterruptedException, IOException {

    final int numNestedGroups =
        Integer.parseInt(Objects.requireNonNullElse(System.getenv("NUM_NESTED_GROUPS"), "1"));
    final int numSensor =
        Integer.parseInt(Objects.requireNonNullElse(System.getenv("NUM_SENSORS"), "1"));
    final int periodMs =
        Integer.parseInt(Objects.requireNonNullElse(System.getenv("PERIOD_MS"), "1000"));
    final int value =
        Integer.parseInt(Objects.requireNonNullElse(System.getenv("VALUE"), "10"));
    final String kafkaBootstrapServers =
        Objects.requireNonNullElse(System.getenv("KAFKA_BOOTSTRAP_SERVERS"), "localhost:9092");
    final String kafkaInputTopic =
        Objects.requireNonNullElse(System.getenv("KAFKA_INPUT_TOPIC"), "input");
    final String configurationUrl =
        Objects.requireNonNullElse(System.getenv("CONFIGURATION_URL"),
            "http://localhost:8082/sensor-registry");

    final MutableSensorRegistry sensorRegistry = new MutableSensorRegistry("group_lvl_0");
    MutableAggregatedSensor lastSensor = sensorRegistry.getTopLevelSensor();
    for (int lvl = 1; lvl < numNestedGroups; lvl++) {
      lastSensor = lastSensor.addChildAggregatedSensor("group_lvl_" + lvl);
    }
    for (int s = 0; s < numSensor; s++) {
      lastSensor.addChildMachineSensor("sensor_" + s);
    }
    final List<String> sensors =
        lastSensor.getChildren().stream().map(s -> s.getIdentifier()).collect(Collectors.toList());

    final HttpClient httpClient = HttpClient.newHttpClient();
    final HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(configurationUrl))
        .PUT(BodyPublishers.ofString(sensorRegistry.toJson()))
        .build();

    httpClient.send(request, BodyHandlers.discarding());


    final KafkaRecordSender<ActivePowerRecord> kafkaRecordSender = new KafkaRecordSender<>(
        kafkaBootstrapServers, kafkaInputTopic, r -> r.getIdentifier(), r -> r.getTimestamp());

    final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    final Random random = new Random();

    for (final String sensor : sensors) {
      final int initialDelay = random.nextInt(periodMs);
      executor.scheduleAtFixedRate(
          () -> {
            kafkaRecordSender.write(new ActivePowerRecord(
                sensor,
                System.currentTimeMillis(),
                value));
          },
          initialDelay,
          periodMs,
          TimeUnit.MICROSECONDS);
    }

    System.out.println("Wait for termination...");
    executor.awaitTermination(30, TimeUnit.DAYS);
    System.out.println("Will terminate now");

  }
}
