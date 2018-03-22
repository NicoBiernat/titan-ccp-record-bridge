package titan.ccp.kiekerbridge.raritan;

import java.util.Queue;

import teetime.framework.AbstractProducerStage;

public class QueueProccessorStage<T> extends AbstractProducerStage<T> {

	private final Queue<T> queue;

	public QueueProccessorStage(QueueProvider<T> queueProvider) {
		this.queue = queueProvider.getQueue();
	}

	public QueueProccessorStage(final Queue<T> queue) {
		this.queue = queue;
	}

	@Override
	protected void execute() throws Exception {
		while (!queue.isEmpty()) {
			T element = queue.poll();
			this.getOutputPort().send(element);
		}
	}

}
