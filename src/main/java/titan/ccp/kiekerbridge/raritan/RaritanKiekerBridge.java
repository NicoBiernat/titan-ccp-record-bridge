package titan.ccp.kiekerbridge.raritan;

import java.util.List;

import kieker.analysisteetime.util.stage.FunctionStage;
import kieker.common.record.IMonitoringRecord;
import teetime.framework.Configuration;
import teetime.framework.ConfigurationBuilder;
import titan.ccp.kiekerbridge.FlattenerStage;
import titan.ccp.kiekerbridge.KafkaSenderStage;
import titan.ccp.kiekerbridge.KiekerBridge;

public class RaritanKiekerBridge extends KiekerBridge {

	public RaritanKiekerBridge() {
		// Reader =
		// Transformer =

		super(createConfiguration());
	}

	public static void main(final String[] args) {
		// TODO
		System.out.println("Start...");

		new RaritanKiekerBridge().start();

	}

	// TODO remove from here
	private final static Configuration createConfiguration() {
		final RaritanRestServer raritanRestServer = new RaritanRestServer();

		final QueueProccessorStage<String> queueProccessor = new QueueProccessorStage<>(raritanRestServer.getQueue());
		final FunctionStage<String, List<IMonitoringRecord>> functionStage = new FunctionStage<>(
				new RaritanJsonTransformer());
		final KafkaSenderStage senderStage = new KafkaSenderStage();

		return ConfigurationBuilder.from(queueProccessor).to(functionStage).to(new FlattenerStage<>()).end(senderStage);
	}

}
