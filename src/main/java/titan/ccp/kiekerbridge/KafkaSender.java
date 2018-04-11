package titan.ccp.kiekerbridge;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.configuration.ConfigurationKeys;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.collector.ChunkingCollector;
import kieker.monitoring.writer.kafka.KafkaWriter;
import kieker.monitoring.writer.serializer.BinarySerializer;
import titan.ccp.models.records.PowerConsumptionRecord;

public class KafkaSender {

	private static final String BOOTSTRAP_SERVER = "127.0.0.1:9092"; // TODO
	private static final String TOPIC_NAME = "test-soeren--3"; // TODO

	private final IMonitoringController monitoringController;

	public KafkaSender() {
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(ConfigurationKeys.WRITER_CLASSNAME, ChunkingCollector.class.getCanonicalName());
		configuration.setProperty(ChunkingCollector.CONFIG_SERIALIZER_CLASSNAME,
				BinarySerializer.class.getCanonicalName());
		configuration.setProperty(ChunkingCollector.CONFIG_WRITER_CLASSNAME, KafkaWriter.class.getCanonicalName());
		// configuration.setProperty(ChunkingCollector.CONFIG_QUEUE_SIZE, "100"); //TODO
		configuration.setProperty(KafkaWriter.CONFIG_PROPERTY_BOOTSTRAP_SERVERS, BOOTSTRAP_SERVER);
		configuration.setProperty(KafkaWriter.CONFIG_PROPERTY_TOPIC_NAME, TOPIC_NAME);
		// configuration.setProperty(KafkaWriter.CONFIG_PROPERTY_BATCH_SIZE, "100");
		// //TODO
		this.monitoringController = MonitoringController.createInstance(configuration);
	}

	public void send(final IMonitoringRecord record) {
		this.monitoringController.newMonitoringRecord(record);
	}

	public void shutdown() {
		// TODO
		this.monitoringController.terminateMonitoring();
		// monitoringController.waitForTermination(0);
	}

	// TODO remove
	public static void main(final String[] args) {
		final KafkaSender kafkaSender = new KafkaSender();

		for (int i = 0; i < 1000000; i++) {
			final String identifier = "identifier";
			final long timestamp = 0;
			final int consumption = 10;
			final IMonitoringRecord record = new PowerConsumptionRecord(identifier, timestamp, consumption);
			kafkaSender.send(record);
		}

		System.out.println("Finished writing");
		System.out.println("Terminate monitoring...");
		kafkaSender.shutdown();
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
