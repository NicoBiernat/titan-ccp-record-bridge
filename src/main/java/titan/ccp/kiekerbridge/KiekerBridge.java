package titan.ccp.kiekerbridge;

import java.util.function.Consumer;
import java.util.function.Function;

import kieker.common.record.IMonitoringRecord;
import teetime.framework.AbstractProducerStage;
import teetime.framework.Configuration;
import teetime.framework.Execution;

public class KiekerBridge {

	private final Execution<Configuration> execution;

	// TODO clean up constructors

	public KiekerBridge(final Function<Consumer<IMonitoringRecord>, SensorReader> sensorReaderFactory) {
		this(new SensorReaderStage(sensorReaderFactory));
	}

	public KiekerBridge(final AbstractProducerStage<IMonitoringRecord> sensorReaderStage) {
		this(new KiekerBridgeConfiguration(sensorReaderStage));
	}

	public KiekerBridge(final Configuration configuration) {
		this.execution = new Execution<>(configuration);
	}

	public void start() {
		this.execution.executeNonBlocking();
	}

	public static void main(final String[] args) {
		// new KiekerBridge(readerStage).start();
	}

}
