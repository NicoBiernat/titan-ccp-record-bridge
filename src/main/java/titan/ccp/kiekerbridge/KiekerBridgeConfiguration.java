package titan.ccp.kiekerbridge;

import kieker.common.record.IMonitoringRecord;
import teetime.framework.AbstractProducerStage;
import teetime.framework.Configuration;

public class KiekerBridgeConfiguration extends Configuration {

	public KiekerBridgeConfiguration(AbstractProducerStage<IMonitoringRecord> readerStage) {
		final KafkaSenderStage senderStage = new KafkaSenderStage();
		super.from(readerStage).end(senderStage);
	}
	
}
