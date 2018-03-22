package titan.ccp.kiekerbridge.raritan;

import kieker.analysisteetime.util.stage.FunctionStage;
import kieker.common.record.IMonitoringRecord;
import titan.ccp.kiekerbridge.KiekerBridge;
import titan.ccp.kiekerbridge.KiekerBridgeConfiguration;

public class RaritanKiekerBridge extends KiekerBridge {

	public RaritanKiekerBridge() {
		//Reader = 
		// Transformer =
		
		
		super(createConfiguration());
	}

	public static void main(String[] args) {
		//TODO
		System.out.println("Start...");
		
		new RaritanKiekerBridge().start();
		
		
	}

	
	//TODO remove from here
	private final static KiekerBridgeConfiguration createConfiguration() {
		RaritanRestServer raritanRestServer = new RaritanRestServer();
		
		QueueProccessorStage<String> queueProccessor = new QueueProccessorStage<>(raritanRestServer.getQueue());
		FunctionStage<String, IMonitoringRecord> functionStage = new FunctionStage<>(new RaritanJsonTransformer()); 
		
		
		KiekerBridgeConfiguration configuration = new KiekerBridgeConfiguration(queueProccessor, functionStage);
		return configuration;
	}
	
}
