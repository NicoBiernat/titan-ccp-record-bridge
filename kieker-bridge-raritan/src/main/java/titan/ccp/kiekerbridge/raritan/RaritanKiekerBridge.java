package titan.ccp.kiekerbridge.raritan;

import java.util.Objects;
import kieker.common.record.IMonitoringRecord;
import titan.ccp.kiekerbridge.KiekerBridge;
import titan.ccp.kiekerbridge.RecordBridgeStream;

/**
 * A Record Bridge that integrates Raritan PDUs.
 */
public final class RaritanKiekerBridge {
  // TODO rename to RecordBridge

  private RaritanKiekerBridge() {}

  /**
   * Start the {@link RaritanKiekerBridge}.
   */
  public static void main(final String[] args) {
    // TODO Do this via configuration
    final boolean receiveTimestampsInMs = Boolean
        .parseBoolean(Objects.requireNonNullElse(System.getenv("TIMESTAMPS_IN_MS"), "false"));

    final RaritanRestServer raritanRestServer = new RaritanRestServer();

    final RecordBridgeStream<IMonitoringRecord> stream =
        RecordBridgeStream.from(raritanRestServer.getQueue())
            .flatMap(new RaritanJsonTransformer(receiveTimestampsInMs));
    final KiekerBridge kiekerBridge = KiekerBridge.ofStream(stream)
        .onStart(raritanRestServer::start).onStop(raritanRestServer::stop).build();
    kiekerBridge.start();
  }

}
