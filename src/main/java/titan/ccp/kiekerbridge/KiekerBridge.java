package titan.ccp.kiekerbridge;

import java.util.function.Consumer;
import java.util.function.Function;

import kieker.common.record.IMonitoringRecord;
import teetime.framework.AbstractProducerStage;
import teetime.framework.Execution;

public class KiekerBridge {
 
	private final Execution<KiekerBridgeConfiguration> execution;
	
	//TODO clean up constructors
	
	public KiekerBridge(Function<Consumer<IMonitoringRecord>, SensorReader> sensorReaderFactory) {
		this(new SensorReaderStage(sensorReaderFactory));
	}
	
	public KiekerBridge(AbstractProducerStage<IMonitoringRecord> sensorReaderStage) {
		this(new KiekerBridgeConfiguration(sensorReaderStage));
	}
	
	public KiekerBridge(KiekerBridgeConfiguration configuration) {
		execution = new Execution<KiekerBridgeConfiguration>(configuration);
	}
	
	public void start() {
		execution.executeNonBlocking();
	}
	
	public static void main(String[] args) {
		//new KiekerBridge(readerStage).start();
	}

}
