package titan.ccp.kiekerbridge;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.system.CPUUtilizationRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.collector.ChunkingCollector;
import kieker.monitoring.writer.kafka.KafkaWriter;
import kieker.monitoring.writer.serializer.BinarySerializer;

public class Main {

	public static void main(String[] args) {
		
		String writerName = ChunkingCollector.class.getCanonicalName();
		
		
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();

		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, writerName);
		
		configuration.setProperty(ChunkingCollector.CONFIG_SERIALIZER_CLASSNAME, BinarySerializer.class.getCanonicalName());
		configuration.setProperty(ChunkingCollector.CONFIG_WRITER_CLASSNAME, KafkaWriter.class.getCanonicalName());
		
		// The address and port of the Kafka bootstrap server(s), e.g. 127.0.0.1:9092
		configuration.setProperty(KafkaWriter.CONFIG_PROPERTY_BOOTSTRAP_SERVERS, "127.0.0.1:9092");
		
		// The topic name to use for the monitoring records
		configuration.setProperty(KafkaWriter.CONFIG_PROPERTY_TOPIC_NAME, "example-topic");
		
		IMonitoringController monitoringController = MonitoringController.createInstance(configuration);
		
		for (int i = 0; i < 100; i++) {
			IMonitoringRecord record = new CPUUtilizationRecord(0, "", "", 0, 0, 0, 0, 0, 0, 0); //TODO temp
			monitoringController.newMonitoringRecord(record);
		}
		
	}

}
