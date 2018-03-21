package titan.ccp.kiekerbridge.raritan;

import java.util.function.Consumer;

import kieker.common.record.IMonitoringRecord;
import spark.Request;
import spark.Response;
import spark.Service;
import titan.ccp.kiekerbridge.SensorReader;
import titan.ccp.model.PowerConsumptionRecord;

public class RaritanRestListener implements SensorReader {

	private static final int PORT = 0; //TODO as parameter
	private static final String POST_URL = ""; //TODO as parameter
	
	private static final int RESPONSE_STATUS_CODE = 204;
	private static final String RESPONSE_STATUS_MESSAGE = "";
	
	private final Service service;
	private final Consumer<IMonitoringRecord> newRecordHandler;
	
	public RaritanRestListener(Consumer<IMonitoringRecord> handler) {
		service = Service.ignite().port(PORT);
		this.newRecordHandler = handler;
	}

	public void start() {
		
		service.post(POST_URL, (Request request, Response response) -> {
			processEvent(request.body());
			response.status(RESPONSE_STATUS_CODE);
			return RESPONSE_STATUS_MESSAGE;
		});
		
	}
	
	public void stop() {
		service.stop();
	}
	
	private void processEvent(String message) {
		IMonitoringRecord record = createMonitoringRecord(message);
		this.newRecordHandler.accept(record);
	}
	
	private IMonitoringRecord createMonitoringRecord(String message) {
		//TODO transform
		final byte[] identifier = {1,2,3,4,5,6,7,8};
		final long timestamp = 0;
		final int consumption = 10;
		return new PowerConsumptionRecord(identifier,timestamp, consumption);
	}
	
}
