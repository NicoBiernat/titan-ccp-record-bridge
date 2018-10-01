package titan.ccp.kiekerbridge.stages;

import java.util.function.Function;
import teetime.stage.basic.AbstractTransformation;

/**
 * Maps every incoming element to another element using a configurable {@link Function}.
 *
 * @param <I> type of input elements
 * @param <O> type of output elements
 */
public class MapStage<I, O> extends AbstractTransformation<I, O> {

  private final Function<? super I, ? extends O> function;

  /**
   * Create a new {@link MapStage}.
   */
  public MapStage(final Function<? super I, ? extends O> function) {
    super();
    this.function = function;

  }

  @Override
  protected void execute(final I element) {
    final O mappedElement = this.function.apply(element);
    this.getOutputPort().send(mappedElement);
  }

}
