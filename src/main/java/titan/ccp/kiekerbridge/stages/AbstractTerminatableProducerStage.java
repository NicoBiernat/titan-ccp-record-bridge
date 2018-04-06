package titan.ccp.kiekerbridge.stages;

import java.util.concurrent.CompletableFuture;

import teetime.framework.AbstractProducerStage;

public abstract class AbstractTerminatableProducerStage<T> extends AbstractProducerStage<T> implements Terminatable {

	private volatile boolean terminationRequested = false;

	private final CompletableFuture<Void> terminationRequestResult = new CompletableFuture<>();

	@Override
	protected final void execute() throws Exception {
		if (!this.terminationRequested) {
			this.execute2();
		} else {
			this.terminationRequestResult.complete(null);
			super.workCompleted();
		}
	}

	protected abstract void execute2() throws Exception; // TODO name

	@Override
	public final CompletableFuture<Void> requestTermination() {
		this.terminationRequested = true;
		return this.terminationRequestResult;
	}

}
