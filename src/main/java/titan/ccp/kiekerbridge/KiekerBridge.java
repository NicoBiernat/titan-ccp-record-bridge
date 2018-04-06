package titan.ccp.kiekerbridge;

import java.util.concurrent.CompletableFuture;

import teetime.framework.Execution;

public class KiekerBridge {

	private final Execution<KiekerBridgeConfiguration> execution;

	public KiekerBridge(final KiekerBridgeConfiguration configuration) {
		this.execution = new Execution<>(configuration);
	}

	public void start() {
		this.execution.executeNonBlocking();
	}

	public CompletableFuture<Void> stop() {
		return this.execution.getConfiguration().requestTermination();
	}

	public static void main(final String[] args) {
		// new KiekerBridge(readerStage).start();
	}

}
