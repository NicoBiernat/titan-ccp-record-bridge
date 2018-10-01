package titan.ccp.kiekerbridge.stages;

import java.util.concurrent.BlockingQueue;

/**
 * Producer stage that reads elements from a {@link BlockingQueue} and forwards them to is output
 * port.
 *
 * @param <T> type of elements in the queue
 */
public class BlockingQueueProccessorStage<T> extends AbstractTerminatableProducerStage<T> {

  private final BlockingQueue<T> queue;

  public BlockingQueueProccessorStage(final BlockingQueue<T> queue) {
    super();
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
