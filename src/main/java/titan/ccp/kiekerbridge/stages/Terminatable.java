package titan.ccp.kiekerbridge.stages;

import java.util.concurrent.CompletableFuture;

public interface Terminatable {

  public CompletableFuture<Void> requestTermination();

}
