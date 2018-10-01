package titan.ccp.kiekerbridge.stages;

import java.util.concurrent.CompletableFuture;
import teetime.framework.AbstractProducerStage;

/**
 * An {@link AbstractProducerStage} that can be terminated.
 *
 * @param <T> type of the default output port
 */
public abstract class AbstractTerminatableProducerStage<T> extends AbstractProducerStage<T>
    implements Terminatable {

  private volatile boolean terminationRequested; // false

  private final CompletableFuture<Void> terminationRequestResult = new CompletableFuture<>();

  @Override
  protected final void execute() throws Exception {
    if (this.terminationRequested) {
      this.terminationRequestResult.complete(null);
      super.workCompleted();
    } else {
      this.execute2();
    }
  }

  protected abstract void execute2() throws Exception; // NOPMD// TODO name

  @Override
  public final CompletableFuture<Void> requestTermination() {
    this.terminationRequested = true;
    return this.terminationRequestResult;
  }

}
