package titan.ccp.kiekerbridge.stages;

import java.util.concurrent.BlockingQueue;

public class BlockingQueueProccessorStage<T> extends AbstractTerminatableProducerStage<T> {

	private final BlockingQueue<T> queue;

	public BlockingQueueProccessorStage(final BlockingQueue<T> queue) {
		this.queue = queue;
	}

	@Override
	protected void execute2() throws Exception {
		if (!this.queue.isEmpty()) {
			final T element = this.queue.take();
			this.getOutputPort().send(element);
		}
	}

}
