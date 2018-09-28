package titan.ccp.kiekerbridge;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import kieker.analysisteetime.util.stage.FilterStage;
import teetime.framework.AbstractProducerStage;
import teetime.framework.OutputPort;
import teetime.stage.basic.ITransformation;
import titan.ccp.kiekerbridge.stages.BlockingQueueProccessorStage;
import titan.ccp.kiekerbridge.stages.FlatMapStage;
import titan.ccp.kiekerbridge.stages.MapStage;
import titan.ccp.kiekerbridge.stages.QueueProccessorStage;
import titan.ccp.kiekerbridge.stages.SupplierStage;
import titan.ccp.kiekerbridge.stages.Terminatable;

/**
 * A stream of elements to be processed by TeeTime.
 *
 * @param <T> Elements in this stream
 */
public final class RecordBridgeStream<T> {

  private static final int TEETIME_DEFAULT_PIPE_CAPACITY = 512;

  private final StreamBasedConfiguration configuration;

  private final OutputPort<? extends T> lastOutputPort;

  private RecordBridgeStream(final StreamBasedConfiguration configuration,
      final OutputPort<? extends T> lastOutputPort) {
    this.configuration = configuration;
    this.lastOutputPort = lastOutputPort;
  }

  public <R> RecordBridgeStream<R> map(final Function<? super T, ? extends R> mapper) {
    return this.addStage(new MapStage<>(mapper));
  }

  public <R> RecordBridgeStream<R> flatMap(
      final Function<? super T, ? extends Iterable<? extends R>> mapper) {
    return this.addStage(new FlatMapStage<>(mapper));
  }

  public RecordBridgeStream<T> filter(final Predicate<T> predicate) {
    return this.addStage(new FilterStage<>(predicate));
  }

  private <R> RecordBridgeStream<R> addStage(final ITransformation<? super T, ? extends R> stage) {
    this.configuration.connectPorts(this.lastOutputPort, stage.getInputPort(),
        TEETIME_DEFAULT_PIPE_CAPACITY);
    return new RecordBridgeStream<>(this.configuration, stage.getOutputPort());
  }

  /* default */ StreamBasedConfiguration getConfiguration() {
    return this.configuration;
  }

  /* default */ OutputPort<? extends T> getLastOutputPort() {
    return this.lastOutputPort;
  }

  /**
   * Create s stream from a {@link Queue}.
   */
  public static <T> RecordBridgeStream<T> from(final Queue<T> queue) {
    if (queue instanceof BlockingQueue) {
      return createFromStage(new BlockingQueueProccessorStage<>((BlockingQueue<T>) queue));
    } else {
      return createFromStage(new QueueProccessorStage<>(queue));
    }
  }

  /**
   * Create a stream from a {@link Supplier}.
   */
  public static <T> RecordBridgeStream<T> from(final Supplier<T> supplier) {
    return createFromStage(new SupplierStage<>(supplier));
  }

  private static <T, S extends AbstractProducerStage<T> & Terminatable> RecordBridgeStream<T> createFromStage( // NOCS
      final S stage) {
    return new RecordBridgeStream<>(new StreamBasedConfiguration(stage), stage.getOutputPort());
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
