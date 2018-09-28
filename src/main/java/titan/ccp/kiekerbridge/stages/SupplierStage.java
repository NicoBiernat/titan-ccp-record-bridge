package titan.ccp.kiekerbridge.stages;

import java.util.function.Supplier;

public class SupplierStage<T> extends AbstractTerminatableProducerStage<T> {

  private final Supplier<T> supplier;

  public SupplierStage(final Supplier<T> supplier) {
    this.supplier = supplier;
  }

  @Override
  protected void execute2() throws Exception {
    final T element = this.supplier.get();
    this.getOutputPort().send(element);
  }

}
