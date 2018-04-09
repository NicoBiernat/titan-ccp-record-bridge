package titan.ccp.kiekerbridge;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import kieker.analysisteetime.util.stage.FilterStage;
import teetime.framework.AbstractProducerStage;
import teetime.framework.OutputPort;
import teetime.stage.basic.ITransformation;
import titan.ccp.kiekerbridge.stages.FlatMapStage;
import titan.ccp.kiekerbridge.stages.MapStage;
import titan.ccp.kiekerbridge.stages.QueueProccessorStage;
import titan.ccp.kiekerbridge.stages.SupplierStage;
import titan.ccp.kiekerbridge.stages.Terminatable;

public final class KiekerBridgeStream<T> {

	private static final int TEETIME_DEFAULT_PIPE_CAPACITY = 512;

	private final StreamBasedConfiguration configuration; // TODO
	private final OutputPort<? extends T> lastOutputPort; // TODO

	private KiekerBridgeStream(final StreamBasedConfiguration configuration,
			final OutputPort<? extends T> lastOutputPort) {
		this.configuration = configuration;
		this.lastOutputPort = lastOutputPort;
	}

	public <R> KiekerBridgeStream<R> map(final Function<? super T, ? extends R> mapper) {
		return this.addStage(new MapStage<>(mapper));
	}

	public <R> KiekerBridgeStream<R> flatMap(final Function<? super T, ? extends Iterable<? extends R>> mapper) {
		return this.addStage(new FlatMapStage<>(mapper));
	}

	public KiekerBridgeStream<T> filter(final Predicate<T> predicate) {
		return this.addStage(new FilterStage<>(predicate));
	}

	private <R> KiekerBridgeStream<R> addStage(final ITransformation<? super T, ? extends R> stage) {
		this.configuration.connectPorts(this.lastOutputPort, stage.getInputPort(), TEETIME_DEFAULT_PIPE_CAPACITY);
		return new KiekerBridgeStream<>(this.configuration, stage.getOutputPort());
	}

	protected StreamBasedConfiguration getConfiguration() {
		return this.configuration;
	}

	protected OutputPort<? extends T> getLastOutputPort() {
		return this.lastOutputPort;
	}

	public static <T> KiekerBridgeStream<T> from(final Queue<T> queue) {
		return createFromStage(new QueueProccessorStage<>(queue));
	}

	public static <T> KiekerBridgeStream<T> from(final QueueProvider<T> queueProvider) {
		return createFromStage(new QueueProccessorStage<>(queueProvider));
	}

	public static <T> KiekerBridgeStream<T> from(final Supplier<T> supplier) {
		return createFromStage(new SupplierStage<>(supplier));
	}

	private static <T, S extends AbstractProducerStage<T> & Terminatable> KiekerBridgeStream<T> createFromStage(
			final S stage) {
		return new KiekerBridgeStream<>(new StreamBasedConfiguration(stage), stage.getOutputPort());
	}

	private static class StreamBasedConfiguration extends TerminatableConfiguration {

		private final Terminatable terminatable;

		public StreamBasedConfiguration(final Terminatable terminatable) {
			super();
			this.terminatable = terminatable;
		}

		@Override
		public CompletableFuture<Void> requestTermination() {
			return this.terminatable.requestTermination();
		}

	}

}
