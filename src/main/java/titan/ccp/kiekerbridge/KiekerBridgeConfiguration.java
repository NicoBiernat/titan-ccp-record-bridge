package titan.ccp.kiekerbridge;

import java.util.Queue;
import java.util.function.Function;
import java.util.function.Predicate;

import kieker.analysisteetime.util.stage.FilterStage;
import kieker.common.record.IMonitoringRecord;
import teetime.framework.AbstractProducerStage;
import teetime.framework.Configuration;
import teetime.framework.OutputPort;
import teetime.stage.basic.ITransformation;
import titan.ccp.kiekerbridge.raritan.QueueProccessorStage;
import titan.ccp.kiekerbridge.raritan.QueueProvider;
import titan.ccp.kiekerbridge.stages.FlatMapStage;
import titan.ccp.kiekerbridge.stages.MapStage;

public abstract class KiekerBridgeConfiguration extends Configuration {

	public KiekerBridgeConfiguration(final OutputPort<IMonitoringRecord> outputPort) {
		completeConfiguration(this, outputPort);
	}

	private KiekerBridgeConfiguration() {
	}

	public static KiekerBridgeConfiguration of(final Stream<? extends IMonitoringRecord> stream) {
		return completeConfiguration(stream.configuration, stream.lastOutputPort);
	}

	private static KiekerBridgeConfiguration completeConfiguration(final KiekerBridgeConfiguration configuration,
			final OutputPort<? extends IMonitoringRecord> outputPort) {
		final KafkaSenderStage senderStage = new KafkaSenderStage();
		configuration.connectPorts(outputPort, senderStage.getInputPort());
		return configuration;
	}

	// TODO name
	private static class StreamBasedConfiguration extends KiekerBridgeConfiguration {

		public StreamBasedConfiguration() {
			super();
		}

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

		public static <T> Stream<T> ofQueue(final Queue<T> queue) {
			return create(new QueueProccessorStage<>(queue));
		}

		public static <T> Stream<T> ofQueue(final QueueProvider<T> queueProvider) {
			return create(new QueueProccessorStage<>(queueProvider));
		}

		private static <T> Stream<T> create(final AbstractProducerStage<T> stage) {
			return new Stream<>(new StreamBasedConfiguration(), stage.getOutputPort());
		}

	}

	public static void main(final String[] args) {
		final Stream<String> map = new Stream<String>(null, null).map(x -> x.getBytes()).map(b -> b.length)
				.map(i -> i.toString()).map(s -> s.toLowerCase()).filter(s -> s.length() > 10)
				.filter(s -> s.hashCode() < 1000);

	}

}
