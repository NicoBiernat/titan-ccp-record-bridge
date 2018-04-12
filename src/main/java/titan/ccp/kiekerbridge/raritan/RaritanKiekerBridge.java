package titan.ccp.kiekerbridge.raritan;

import java.util.Properties;

import kieker.common.record.IMonitoringRecord;
import titan.ccp.kiekerbridge.KiekerBridge;
import titan.ccp.kiekerbridge.KiekerBridgeStream;

public class RaritanKiekerBridge {

	public static void main(final String[] args) {

		// Cloudkarafka configuration
		final String kafkaBootstrapServer = System.getenv("CLOUDKARAFKA_BROKERS");
		final String username = System.getenv("CLOUDKARAFKA_USERNAME");
		final String password = System.getenv("CLOUDKARAFKA_PASSWORD");
		final Properties kafkaProperties = new Properties();
		final String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
		final String jaasCfg = String.format(jaasTemplate, username, password);
		kafkaProperties.put("security.protocol", "SASL_SSL");
		kafkaProperties.put("sasl.mechanism", "SCRAM-SHA-256");
		kafkaProperties.put("sasl.jaas.config", jaasCfg);
		final String kafkaTopic = username + "-default";

		final RaritanRestServer raritanRestServer = new RaritanRestServer();

		final KiekerBridgeStream<IMonitoringRecord> stream = KiekerBridgeStream.from(raritanRestServer)
				.flatMap(new RaritanJsonTransformer());
		final KiekerBridge kiekerBridge = KiekerBridge.ofStream(stream).onStop(raritanRestServer::stop)
				.withKafkaConfiguration(kafkaBootstrapServer, kafkaTopic, kafkaProperties).build();
		kiekerBridge.start();
	}

}
