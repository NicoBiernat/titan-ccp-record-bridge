package titan.ccp.kiekerbridge.raritan;

import titan.ccp.kiekerbridge.KiekerBridge;
import titan.ccp.kiekerbridge.KiekerBridgeConfiguration;

public class RaritanKiekerBridge extends KiekerBridge {

	public RaritanKiekerBridge() {
		super(createConfiguration());
	}

	public static void main(final String[] args) {
		new RaritanKiekerBridge().start();

	}

	// TODO remove from here
	private final static KiekerBridgeConfiguration createConfiguration() {
		final RaritanRestServer raritanRestServer = new RaritanRestServer();

		return KiekerBridgeConfiguration
				.of(KiekerBridgeConfiguration.Stream.of(raritanRestServer).flatMap(new RaritanJsonTransformer()));
	}

}
