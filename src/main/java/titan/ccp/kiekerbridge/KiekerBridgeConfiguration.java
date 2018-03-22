package titan.ccp.kiekerbridge;

import kieker.common.record.IMonitoringRecord;
import teetime.framework.AbstractProducerStage;
import teetime.framework.Configuration;
import teetime.stage.basic.AbstractTransformation;

public class KiekerBridgeConfiguration extends Configuration {

	public KiekerBridgeConfiguration(AbstractProducerStage<IMonitoringRecord> readerStage) {
		final KafkaSenderStage senderStage = new KafkaSenderStage();
		super.from(readerStage).end(senderStage);
	}
	
	public <T> KiekerBridgeConfiguration(AbstractProducerStage<T> readerStage, AbstractTransformation<T, IMonitoringRecord> transformerStage) {
		//TODO avoid duplicate code
		final KafkaSenderStage senderStage = new KafkaSenderStage();
		super.from(readerStage).to(transformerStage).end(senderStage);
	}
	
}
