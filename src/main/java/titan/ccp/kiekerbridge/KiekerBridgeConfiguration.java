package titan.ccp.kiekerbridge;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import kieker.analysisteetime.util.stage.FilterStage;
import kieker.common.record.IMonitoringRecord;
import teetime.framework.AbstractProducerStage;
import teetime.framework.Configuration;
import teetime.framework.OutputPort;
import teetime.stage.basic.ITransformation;
import titan.ccp.kiekerbridge.stages.FlatMapStage;
import titan.ccp.kiekerbridge.stages.MapStage;
import titan.ccp.kiekerbridge.stages.QueueProccessorStage;
import titan.ccp.kiekerbridge.stages.QueueProvider;
import titan.ccp.kiekerbridge.stages.SupplierStage;
import titan.ccp.kiekerbridge.stages.Terminatable;

public abstract class KiekerBridgeConfiguration extends Configuration {

	public KiekerBridgeConfiguration(final OutputPort<IMonitoringRecord> outputPort) {
		completeConfiguration(this, outputPort);
	}

	private KiekerBridgeConfiguration() {
	}

	public abstract CompletableFuture<Void> requestTermination();

	public static KiekerBridgeConfiguration of(final Stream<? extends IMonitoringRecord> stream) {
		return completeConfiguration(stream.configuration, stream.lastOutputPort);
	}

	private static KiekerBridgeConfiguration completeConfiguration(final KiekerBridgeConfiguration configuration,
			final OutputPort<? extends IMonitoringRecord> outputPort) {
		final KafkaSenderStage senderStage = new KafkaSenderStage();
		configuration.connectPorts(outputPort, senderStage.getInputPort());
		return configuration;
	}

	public static final class Stream<T> {

		private final KiekerBridgeConfiguration configuration;
		private final OutputPort<? extends T> lastOutputPort;

		private Stream(final KiekerBridgeConfiguration configuration, final OutputPort<? extends T> lastOutputPort) {
			this.configuration = configuration;
			this.lastOutputPort = lastOutputPort;
		}

		public <R> Stream<R> map(final Function<? super T, ? extends R> mapper) {
			return this.addStage(new MapStage<>(mapper));
		}

		public <R> Stream<R> flatMap(final Function<? super T, ? extends Iterable<? extends R>> mapper) {
			return this.addStage(new FlatMapStage<>(mapper));
		}

		public Stream<T> filter(final Predicate<T> predicate) {
			return this.addStage(new FilterStage<>(predicate));
		}

		private <R> Stream<R> addStage(final ITransformation<? super T, ? extends R> stage) {
			this.configuration.connectPorts(this.lastOutputPort, stage.getInputPort());
			return new Stream<>(this.configuration, stage.getOutputPort());
		}

		public static <T> Stream<T> of(final Queue<T> queue) {
			return createFromStage(new QueueProccessorStage<>(queue));
		}

		public static <T> Stream<T> of(final QueueProvider<T> queueProvider) {
			return createFromStage(new QueueProccessorStage<>(queueProvider));
		}

		public static <T> Stream<T> of(final Supplier<T> supplier) {
			return createFromStage(new SupplierStage<>(supplier));
		}

		private static <T, S extends AbstractProducerStage<T> & Terminatable> Stream<T> createFromStage(final S stage) {
			return new Stream<>(new StreamBasedConfiguration(stage), stage.getOutputPort());
		}

	}

	// TODO name
	private static class StreamBasedConfiguration extends KiekerBridgeConfiguration {

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
