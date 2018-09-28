package titan.ccp.kiekerbridge.raritan;

import java.util.Objects;
import kieker.common.record.IMonitoringRecord;
import titan.ccp.kiekerbridge.KiekerBridge;
import titan.ccp.kiekerbridge.RecordBridgeStream;

public class RaritanKiekerBridge {

  public static void main(final String[] args) {
    // TODO Do this via configuration
    final boolean receiveTimestampsInMs = Boolean
        .parseBoolean(Objects.requireNonNullElse(System.getenv("TIMESTAMPS_IN_MS"), "false"));

    final RaritanRestServer raritanRestServer = new RaritanRestServer();

    final RecordBridgeStream<IMonitoringRecord> stream =
        RecordBridgeStream.from(raritanRestServer.getQueue())
            .flatMap(new RaritanJsonTransformer(receiveTimestampsInMs));
    final KiekerBridge kiekerBridge =
        KiekerBridge.ofStream(stream).onStop(raritanRestServer::stop).build();
    kiekerBridge.start();
  }

}
