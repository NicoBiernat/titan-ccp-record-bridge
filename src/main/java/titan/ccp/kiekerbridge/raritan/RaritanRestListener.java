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

	public RaritanRestListener(final Consumer<IMonitoringRecord> handler) {
		this.restServer.start();
		this.newRecordHandler = handler;
	}

	@Override
	public void start() {
		while (!this.restServer.getQueue().isEmpty()) {
			final String event = this.restServer.getQueue().poll();
			final IMonitoringRecord record = this.createMonitoringRecord(event);
			this.newRecordHandler.accept(record);
		}
	}

	@Override
	public void stop() {
		// service.stop(); //TODO
	}

	private IMonitoringRecord createMonitoringRecord(final String message) {
		// TODO transform
		final String identifier = "identifier";
		final long timestamp = System.nanoTime();
		final int consumption = 10;
		return new PowerConsumptionRecord(identifier, timestamp, consumption);
	}

}
