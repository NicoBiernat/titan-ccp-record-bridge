package titan.ccp.kiekerbridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import kieker.common.record.IMonitoringRecord;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import teetime.framework.Execution;
import teetime.framework.OutputPort;
import teetime.stage.InstanceOfFilter;
import titan.ccp.common.configuration.Configurations;
import titan.ccp.models.records.ActivePowerRecord;


/**
 * Framework class to simplify the creation of Record Bridges.
 */
public final class KiekerBridge {
  // TODO RENAME to Record Bridge

  private final Execution<TerminatableConfiguration> execution;

  private final List<Runnable> onStartActions;

  private final List<Supplier<CompletableFuture<Void>>> onStopActions;

  private KiekerBridge(final TerminatableConfiguration configuration,
      final List<Runnable> onStartActions,
      final List<Supplier<CompletableFuture<Void>>> onStopActions) {
    this.execution = new Execution<>(configuration);
    this.onStartActions = onStartActions;
    this.onStopActions = onStopActions;

    Runtime.getRuntime().addShutdownHook(new Thread(this::stopBlocking));
  }

  /**
   * Start the bridge.
   */
  public void start() {
    for (final Runnable onStartAction : this.onStartActions) {
      onStartAction.run();
    }
    this.execution.executeNonBlocking();
  }

  /**
   * Asynchronously stop the bridge.
   */
  public CompletableFuture<Void> stop() {
    final CompletableFuture<Void> requestTerminationResult =
        this.execution.getConfiguration().requestTermination();
    final Stream<CompletableFuture<Void>> onStopActionResults =
        this.onStopActions.stream().map(action -> action.get());
    final Stream<CompletableFuture<Void>> stopResults =
        Stream.concat(onStopActionResults, Stream.of(requestTerminationResult));

    return CompletableFuture.allOf(stopResults.toArray(size -> new CompletableFuture[size]));
  }

  /**
   * Stop the bridge. This method blocks until it is eventually stopped.
   */
  public void stopBlocking() {
    this.stop().join();
  }

  public static Builder ofStream(final RecordBridgeStream<? extends IMonitoringRecord> stream) {
    return new Builder(stream.getConfiguration(), stream.getLastOutputPort()); // NOPMD
  }

  public static Builder ofConfiguration(final TerminatableConfiguration configuration,
      final OutputPort<? extends IMonitoringRecord> outputPort) {
    return new Builder(configuration, outputPort); // NOPMD
  }

  /**
   * Builder to create Record Bridges.
   */
  public static class Builder {

    private static final int TEETIME_DEFAULT_PIPE_CAPACITY = 512;

    private static final String DEFAULT_PROPERTY_LOCATION =
        "META-INF/default-record-bridge.properties";

    private final Function<Configuration, TerminatableConfiguration> teetimeConfigFactory;

    private final List<Runnable> onStartActions = new ArrayList<>(4);

    private final List<Supplier<CompletableFuture<Void>>> onStopActions = new ArrayList<>(4);

    private final CompositeConfiguration configuration = new CompositeConfiguration();

    private Builder(final TerminatableConfiguration teetimeConfiguration,
        final OutputPort<? extends IMonitoringRecord> outputPort) {

      this.configuration.addConfiguration(Configurations.create());
      try {
        this.configuration
            .addConfiguration(new org.apache.commons.configuration2.builder.fluent.Configurations()
                .properties(DEFAULT_PROPERTY_LOCATION));
      } catch (final ConfigurationException e) {
        throw new IllegalArgumentException(
            "Cannot load configuration from ressource " + "\"" + DEFAULT_PROPERTY_LOCATION + "\"",
            e);
      }

      this.teetimeConfigFactory = config -> {
        // final KafkaSenderStage senderStage = new KafkaSenderStage();
        // final KafkaPowerConsumptionRecordSender kafkaSender = new
        // KafkaPowerConsumptionRecordSender(
        // config.getString("kafka.bootstrap.servers"), config.getString("kafka.topic"),
        // new Properties());
        final KafkaRecordSender<ActivePowerRecord> kafkaSender =
            new KafkaRecordSender<>(config.getString("kafka.bootstrap.servers"),
                config.getString("kafka.topic"), r -> r.getIdentifier(), new Properties());
        // final KafkaPowerConsumptionRecordSender.Stage senderStage = new
        // KafkaPowerConsumptionRecordSender.Stage(
        // kafkaSender);
        final KafkaRecordSender.Stage<ActivePowerRecord> senderStage =
            new KafkaRecordSender.Stage<>(kafkaSender);
        final InstanceOfFilter<IMonitoringRecord, ActivePowerRecord> instanceOfFilter =
            new InstanceOfFilter<>(ActivePowerRecord.class);
        teetimeConfiguration.connectPorts(outputPort, instanceOfFilter.getInputPort(),
            TEETIME_DEFAULT_PIPE_CAPACITY);
        teetimeConfiguration.connectPorts(instanceOfFilter.getMatchedOutputPort(),
            senderStage.getInputPort(), TEETIME_DEFAULT_PIPE_CAPACITY);
        return teetimeConfiguration;
      };
    }

    /**
     * Add an on-start action.
     */
    public Builder onStart(final Runnable action) {
      this.onStartActions.add(action);
      return this;
    }

    /**
     * Add an on-stop action.
     */
    public Builder onStop(final Runnable action) {
      this.onStopActions.add(() -> {
        action.run();
        return CompletableFuture.completedFuture(null);
      });
      return this;
    }

    /**
     * Add an asynchronous on-stop action.
     */
    public Builder onStop(final Supplier<CompletableFuture<Void>> action) {
      this.onStopActions.add(action);
      return this;
    }

    public KiekerBridge build() {
      return new KiekerBridge(this.teetimeConfigFactory.apply(this.configuration), // NOPMD
          this.onStartActions, this.onStopActions);
    }

  }

}
