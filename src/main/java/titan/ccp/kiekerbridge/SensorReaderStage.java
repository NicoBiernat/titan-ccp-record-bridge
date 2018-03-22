package titan.ccp.kiekerbridge;

import java.util.function.Consumer;
import java.util.function.Function;

import kieker.common.record.IMonitoringRecord;

public class SensorReaderStage extends teetime.framework.AbstractProducerStage<IMonitoringRecord> {

	private final SensorReader sensorReader;

	public SensorReaderStage(Function<Consumer<IMonitoringRecord>, SensorReader> sensorReaderFactory) {
		sensorReader = sensorReaderFactory.apply(this.getOutputPort()::send);
	}

	@Override
	protected void execute() throws Exception {
		sensorReader.start();
	}

	public void terminate() {
		sensorReader.stop();
	}

}
