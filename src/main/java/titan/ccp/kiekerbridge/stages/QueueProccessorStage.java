package titan.ccp.kiekerbridge.stages;

import java.util.Queue;

import titan.ccp.kiekerbridge.QueueProvider;

public class QueueProccessorStage<T> extends AbstractTerminatableProducerStage<T> {

	private final Queue<T> queue;

	public QueueProccessorStage(final QueueProvider<T> queueProvider) {
		this.queue = queueProvider.getQueue();
	}

	public QueueProccessorStage(final Queue<T> queue) {
		this.queue = queue;
	}

	@Override
	protected void execute2() throws Exception {
		if (!this.queue.isEmpty()) {
			final T element = this.queue.poll();
			this.getOutputPort().send(element);
		}
	}

}
