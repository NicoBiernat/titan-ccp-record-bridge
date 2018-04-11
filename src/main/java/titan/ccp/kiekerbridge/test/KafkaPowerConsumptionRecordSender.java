package titan.ccp.kiekerbridge.test;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import kieker.common.record.IMonitoringRecord;
import teetime.framework.AbstractConsumerStage;
import titan.ccp.models.records.PowerConsumptionRecord;
import titan.ccp.models.records.serialization.kafka.PowerConsumptionRecordSerializer;

public class KafkaPowerConsumptionRecordSender {

	private static final String BOOTSRATP_SERVERS = "localhost:9092";
	private static final String TOPIC = "test-topic-18040319";

	private final Producer<String, PowerConsumptionRecord> producer;

	public KafkaPowerConsumptionRecordSender() {
		final Properties properties = new Properties();

		properties.put("bootstrap.servers", BOOTSRATP_SERVERS);
		// properties.put("acks", this.acknowledges);
		// properties.put("batch.size", this.batchSize);
		// properties.put("linger.ms", this.lingerMs);
		// properties.put("buffer.memory", this.bufferMemory);

		this.producer = new KafkaProducer<>(properties, new StringSerializer(), new PowerConsumptionRecordSerializer());
	}

	public void write(final PowerConsumptionRecord powerConsumptionRecord) {
		final ProducerRecord<String, PowerConsumptionRecord> record = new ProducerRecord<>(TOPIC,
				powerConsumptionRecord.getIdentifier(), powerConsumptionRecord);
		this.producer.send(record);
	}

	public static class Stage extends AbstractConsumerStage<IMonitoringRecord> {

		private final KafkaPowerConsumptionRecordSender sender;

		public Stage() {
			this.sender = new KafkaPowerConsumptionRecordSender();
		}

		@Override
		protected void execute(final IMonitoringRecord record) throws Exception {
			if (record instanceof PowerConsumptionRecord) {
				this.sender.write((PowerConsumptionRecord) record);
			}
		}

	}

}
