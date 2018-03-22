package titan.ccp.kiekerbridge;

import kieker.common.record.IMonitoringRecord;
import teetime.framework.AbstractConsumerStage;

public class KafkaSenderStage extends AbstractConsumerStage<IMonitoringRecord> {

	private final KafkaSender kafkaSender;

	public KafkaSenderStage() {
		kafkaSender = new KafkaSender();
	}

	@Override
	protected void execute(IMonitoringRecord record) throws Exception {
		kafkaSender.send(record);
	}

	@Override
	protected void onTerminating() {
		kafkaSender.shutdown();
		super.onTerminating();
	}

}
