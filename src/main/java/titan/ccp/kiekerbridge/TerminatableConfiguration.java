package titan.ccp.kiekerbridge;

import java.util.concurrent.CompletableFuture;
import teetime.framework.Configuration;
import titan.ccp.kiekerbridge.stages.Terminatable;

/**
 * A TeeTime configuration that can be terminated.
 *
 */
public abstract class TerminatableConfiguration extends Configuration implements Terminatable {

  @Override
  public abstract CompletableFuture<Void> requestTermination();

}
