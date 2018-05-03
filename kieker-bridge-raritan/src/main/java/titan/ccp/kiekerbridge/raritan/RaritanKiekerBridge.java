package titan.ccp.kiekerbridge.raritan;

import kieker.common.record.IMonitoringRecord;
import titan.ccp.kiekerbridge.KiekerBridge;
import titan.ccp.kiekerbridge.KiekerBridgeStream;

public class RaritanKiekerBridge {

	public static void main(final String[] args) {
		final RaritanRestServer raritanRestServer = new RaritanRestServer();

		final KiekerBridgeStream<IMonitoringRecord> stream = KiekerBridgeStream.from(raritanRestServer)
				.flatMap(new RaritanJsonTransformer());
		final KiekerBridge kiekerBridge = KiekerBridge.ofStream(stream).onStop(raritanRestServer::stop).build();
		kiekerBridge.start();
	}

}
