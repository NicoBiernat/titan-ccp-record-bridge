package titan.ccp.kiekerbridge;

import teetime.stage.basic.AbstractTransformation;

public class FlattenerStage<O, I extends Iterable<O>> extends AbstractTransformation<I, O> {

	public FlattenerStage() {
		super();
	}

	@Override
	protected void execute(final I elements) throws Exception {
		for (final O element : elements) {
			this.getOutputPort().send(element);
		}
	}

}
