package titan.ccp.kiekerbridge.stages;

import java.util.function.Supplier;

/**
 * TeeTime producer stage that produces elements by continuously calling a {@link Supplier}.
 *
 * @param <T> type of elements to supply
 */
public class SupplierStage<T> extends AbstractTerminatableProducerStage<T> {

  private final Supplier<T> supplier;

  public SupplierStage(final Supplier<T> supplier) {
    super();
    this.supplier = supplier;
  }

  @Override
  protected void execute2() throws Exception {
    final T element = this.supplier.get();
    this.getOutputPort().send(element);
  }

}
