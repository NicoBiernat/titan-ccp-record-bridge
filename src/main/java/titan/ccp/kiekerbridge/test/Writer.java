package titan.ccp.kiekerbridge.test;


import java.util.List;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.configuration.ConfigurationKeys;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.collector.ChunkingCollector;
import kieker.monitoring.writer.kafka.KafkaWriter;
import kieker.monitoring.writer.serializer.BinarySerializer;
import titan.ccp.kiekerbridge.stages.MapStage;
import titan.ccp.models.records.ActivePowerRecord;

public class Writer {

  public static int Hllo;

  public int Hllod;

  public static final int HALLO_WORLD = 5;

  /**
   * {@link MapStage}
   *
   * @return
   *
   * @param args
   */
  public static void main(final String[] args) {


    final String text1 = "Hallo " + ".";
    final String text2 = "Thüss " + ".";

    final List list = List.of("hi");

    final String writerName = ChunkingCollector.class.getCanonicalName();

    final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();

    configuration.setProperty(ConfigurationKeys.WRITER_CLASSNAME, writerName);

    configuration.setProperty(ChunkingCollector.CONFIG_SERIALIZER_CLASSNAME,
        BinarySerializer.class.getCanonicalName());
    configuration.setProperty(ChunkingCollector.CONFIG_WRITER_CLASSNAME,
        KafkaWriter.class.getCanonicalName());

    // configuration.setProperty(ChunkingCollector.CONFIG_QUEUE_SIZE, "100");

    // The address and port of the Kafka bootstrap server(s), e.g. 127.0.0.1:9092
    configuration.setProperty(KafkaWriter.CONFIG_PROPERTY_BOOTSTRAP_SERVERS, "127.0.0.1:9092"); // TODO

    // The topic name to use for the monitoring records
    configuration.setProperty(KafkaWriter.CONFIG_PROPERTY_TOPIC_NAME, "test-soeren--3"); // TODO

    // configuration.setProperty(KafkaWriter.CONFIG_PROPERTY_BATCH_SIZE, "100");

    final IMonitoringController monitoringController =
        MonitoringController.createInstance(configuration);

    for (int i = 0; i < 1000000; i++) {
      final String identifier = "identifier";
      final long timestamp = 0;
      final int consumption = 10;
      final IMonitoringRecord record = new ActivePowerRecord(identifier, timestamp, consumption);
      monitoringController.newMonitoringRecord(record);
    }

    System.out.println("Finished writing");
    System.out.println("Terminate monitoring...");
    monitoringController.terminateMonitoring();
    System.out.println("Start sleeping");
    try {
      Thread.sleep(10000);
    } catch (final InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println("Finished sleeping, exit");
  }

}
