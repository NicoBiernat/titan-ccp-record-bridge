package titan.ccp.kiekerbridge.stages;

import java.util.Queue;

public class QueueProccessorStage<T> extends AbstractTerminatableProducerStage<T> {

	private static final long DEFAULT_TIMEOUT_IN_MS = 100;

	private final Queue<T> queue;
	private final long timeoutInMs;

	public QueueProccessorStage(final Queue<T> queue) {
		this(queue, DEFAULT_TIMEOUT_IN_MS);
	}

	public QueueProccessorStage(final Queue<T> queue, final long timeoutInMs) {
		this.queue = queue;
		this.timeoutInMs = timeoutInMs;
	}

	@Override
	protected void execute2() throws Exception {
		if (this.queue.isEmpty()) {
			Thread.sleep(this.timeoutInMs);
		} else {
			final T element = this.queue.poll();
			this.getOutputPort().send(element);
		}
	}

}
