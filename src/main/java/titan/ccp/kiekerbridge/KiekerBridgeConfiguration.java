package titan.ccp.kiekerbridge;

import kieker.common.record.IMonitoringRecord;
import teetime.framework.AbstractProducerStage;
import teetime.framework.Configuration;
import teetime.stage.basic.AbstractTransformation;

public class KiekerBridgeConfiguration extends Configuration {

	public KiekerBridgeConfiguration(final AbstractProducerStage<IMonitoringRecord> readerStage) {
		final KafkaSenderStage senderStage = new KafkaSenderStage();
		super.from(readerStage).end(senderStage);
	}

	// TODO remove
	public <T> KiekerBridgeConfiguration(final AbstractProducerStage<T> readerStage,
			final AbstractTransformation<T, IMonitoringRecord> transformerStage) {
		// TODO avoid duplicate code
		final KafkaSenderStage senderStage = new KafkaSenderStage();
		super.from(readerStage).to(transformerStage).end(senderStage);
	}

}
