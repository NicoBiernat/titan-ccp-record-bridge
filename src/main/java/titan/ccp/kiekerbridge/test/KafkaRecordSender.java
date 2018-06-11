package titan.ccp.kiekerbridge.test;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;

import kieker.common.record.IMonitoringRecord;
import teetime.framework.AbstractConsumerStage;
import titan.ccp.common.kieker.kafka.IMonitoringRecordSerde;

public class KafkaRecordSender<T extends IMonitoringRecord> {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaRecordSender.class);

	private final String topic;

	private final Function<T, String> keyAccessor;

	private final Producer<String, T> producer;

	public KafkaRecordSender(final String bootstrapServers, final String topic) {
		this(bootstrapServers, topic, x -> "", new Properties());
	}

	public KafkaRecordSender(final String bootstrapServers, final String topic, final Function<T, String> keyAccessor) {
		this(bootstrapServers, topic, keyAccessor, new Properties());
	}

	public KafkaRecordSender(final String bootstrapServers, final String topic, final Function<T, String> keyAccessor,
			final Properties defaultProperties) {
		this.topic = topic;
		this.keyAccessor = keyAccessor;

		final Properties properties = new Properties();
		properties.putAll(defaultProperties);
		properties.put("bootstrap.servers", bootstrapServers);
		// properties.put("acks", this.acknowledges);
		// properties.put("batch.size", this.batchSize);
		// properties.put("linger.ms", this.lingerMs);
		// properties.put("buffer.memory", this.bufferMemory);

		this.producer = new KafkaProducer<>(properties, new StringSerializer(), IMonitoringRecordSerde.serializer());
	}

	public void write(final T monitoringRecord) {
		final ProducerRecord<String, T> record = new ProducerRecord<>(this.topic,
				this.keyAccessor.apply(monitoringRecord), monitoringRecord);

		LOGGER.info("Send record to Kafka topic {}: {}", this.topic, record); // TODO debug
		this.producer.send(record);
	}

	public void terminate() {
		this.producer.close();
	}

	public static class Stage<T extends IMonitoringRecord> extends AbstractConsumerStage<T> {

		private final KafkaRecordSender<T> sender;

		public Stage(final KafkaRecordSender<T> sender) {
			this.sender = sender;
		}

		@Override
		protected void execute(final T record) throws Exception {
			this.sender.write(record);
		}

		@Override
		protected void onTerminating() {
			this.sender.terminate();
			super.onTerminating();
		}

	}

}
