package titan.ccp.kiekerbridge;

import teetime.framework.Configuration;
import teetime.framework.Execution;

public class KiekerBridge {

	private final Execution<Configuration> execution;

	// TODO clean up constructors

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
