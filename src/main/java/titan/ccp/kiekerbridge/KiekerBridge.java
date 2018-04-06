package titan.ccp.kiekerbridge;

import teetime.framework.Execution;

public class KiekerBridge {

	private final Execution<KiekerBridgeConfiguration> execution;

	// TODO clean up constructors

	public KiekerBridge(final KiekerBridgeConfiguration configuration) {
		this.execution = new Execution<>(configuration);
	}

	public void start() {
		this.execution.executeNonBlocking();
	}

	public static void main(final String[] args) {
		// new KiekerBridge(readerStage).start();
	}

}
