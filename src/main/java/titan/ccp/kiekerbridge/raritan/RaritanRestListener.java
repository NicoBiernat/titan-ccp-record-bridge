package titan.ccp.kiekerbridge.raritan;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.record.IMonitoringRecord;
import titan.ccp.kiekerbridge.SensorReader;
import titan.ccp.model.PowerConsumptionRecord;

public class RaritanRestListener implements SensorReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaritanRestListener.class); 

	private final Consumer<IMonitoringRecord> newRecordHandler;
	
	private final RaritanRestServer restServer = new RaritanRestServer();
	
	public RaritanRestListener(Consumer<IMonitoringRecord> handler) {
		restServer.start();
		newRecordHandler = handler;
	}

	public void start() {
		while (!restServer.getQueue().isEmpty()) {
			String event = restServer.getQueue().poll();
			IMonitoringRecord record = createMonitoringRecord(event);
			this.newRecordHandler.accept(record);
		}
	}
	
	public void stop() {
		//service.stop(); //TODO
	}

	
	private IMonitoringRecord createMonitoringRecord(String message) {
		//TODO transform
		final byte[] identifier = {1,2,3,4,5,6,7,8};
		final long timestamp = System.nanoTime();
		final int consumption = 10;
		return new PowerConsumptionRecord(identifier,timestamp, consumption);
	}
	
}
