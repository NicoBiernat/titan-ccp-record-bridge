package titan.ccp.kiekerbridge.stages;

import java.util.function.Function;
import teetime.stage.basic.AbstractTransformation;

public class FlatMapStage<I, O> extends AbstractTransformation<I, O> {

  private final Function<? super I, ? extends Iterable<? extends O>> mapper;

  public FlatMapStage(final Function<? super I, ? extends Iterable<? extends O>> mapper) {
    super();
    this.mapper = mapper;
  }

  @Override
  protected void execute(final I element) throws Exception {
    final Iterable<? extends O> mappedElements = this.mapper.apply(element);
    for (final O mappedElement : mappedElements) {
      this.outputPort.send(mappedElement);
    }
  }

}
