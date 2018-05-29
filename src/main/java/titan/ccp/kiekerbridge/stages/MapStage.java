package titan.ccp.kiekerbridge.stages;

import java.util.function.Function;

import teetime.stage.basic.AbstractTransformation;

public class MapStage<I, O> extends AbstractTransformation<I, O> {

	private final Function<? super I, ? extends O> function;

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
