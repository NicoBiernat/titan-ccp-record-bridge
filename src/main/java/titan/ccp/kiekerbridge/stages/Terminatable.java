package titan.ccp.kiekerbridge.stages;

import java.util.concurrent.CompletableFuture;

/**
 * Something that can be terminated.
 */
public interface Terminatable {

  public CompletableFuture<Void> requestTermination();

}
