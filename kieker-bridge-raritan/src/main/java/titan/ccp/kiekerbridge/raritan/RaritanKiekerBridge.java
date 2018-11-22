package titan.ccp.kiekerbridge.raritan;

import kieker.common.record.IMonitoringRecord;
import org.apache.commons.configuration2.Configuration;
import titan.ccp.common.configuration.Configurations;
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
    final Configuration configuration = Configurations.create();
    final boolean receiveTimestampsInMs = configuration.getBoolean("timestamp.in.ms");
    final int webserverPort = configuration.getInt("webserver.port");
    final String webserverPostUrl = configuration.getString("webserver.post.url");
    final String webserverPostQueryParameterId =
        configuration.getString("webserver.post.query.parameter.id");
    final int queueSize = configuration.getInt("queue.size");

    final RaritanRestServer raritanRestServer = new RaritanRestServer(webserverPort,
        webserverPostUrl, webserverPostQueryParameterId, queueSize);

    final RecordBridgeStream<IMonitoringRecord> stream =
        RecordBridgeStream.from(raritanRestServer.getQueue())
            .flatMap(new RaritanJsonTransformer(receiveTimestampsInMs));
    final KiekerBridge kiekerBridge = KiekerBridge.ofStream(stream)
        .onStart(raritanRestServer::start).onStop(raritanRestServer::stop).build();
    kiekerBridge.start();
  }

}
