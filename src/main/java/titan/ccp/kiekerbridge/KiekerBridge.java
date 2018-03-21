package titan.ccp.kiekerbridge;

import kieker.common.record.IMonitoringRecord;
import teetime.framework.AbstractProducerStage;
import teetime.framework.Execution;

public class KiekerBridge {

	private final Execution<KiekerBridgeConfiguration> execution;
	
	public KiekerBridge(AbstractProducerStage<IMonitoringRecord> readerStage) {
		KiekerBridgeConfiguration configuration = new KiekerBridgeConfiguration(readerStage);
		execution = new Execution<KiekerBridgeConfiguration>(configuration);
	}
	
	public void start() {
		execution.executeNonBlocking();
	}
	
	public static void main(String[] args) {
		//new KiekerBridge(readerStage).start();
	}

}
