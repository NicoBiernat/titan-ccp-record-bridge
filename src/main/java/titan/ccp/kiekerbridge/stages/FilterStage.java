package titan.ccp.kiekerbridge.stages;

import java.util.function.Predicate;
import teetime.stage.basic.AbstractFilter;

public class FilterStage<T> extends AbstractFilter<T> {

  private final Predicate<? super T> predicate;

  public FilterStage(final Predicate<? super T> predicate) {
    super();
    this.predicate = predicate;
  }

  @Override
  protected void execute(final T element) throws Exception {
    if (this.predicate.test(element)) {
      this.getOutputPort().send(element);
    }
  }

}
