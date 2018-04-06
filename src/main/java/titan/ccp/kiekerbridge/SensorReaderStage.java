package titan.ccp.kiekerbridge;

import java.util.function.Consumer;
import java.util.function.Function;

import kieker.common.record.IMonitoringRecord;
import teetime.framework.AbstractProducerStage;

@Deprecated
public class SensorReaderStage extends AbstractProducerStage<IMonitoringRecord> {

	private final SensorReader sensorReader;

	public SensorReaderStage(final Function<Consumer<IMonitoringRecord>, SensorReader> sensorReaderFactory) {
		this.sensorReader = sensorReaderFactory.apply(this.getOutputPort()::send);
	}

	@Override
	protected void execute() throws Exception {
		this.sensorReader.start();
	}

	public void terminate() {
		this.sensorReader.stop();
	}

}
