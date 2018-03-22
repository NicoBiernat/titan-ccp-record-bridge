package titan.ccp.kiekerbridge.raritan;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Consumer;

import org.jctools.queues.SpmcArrayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.record.IMonitoringRecord;
import spark.Request;
import spark.Response;
import spark.Service;
import titan.ccp.model.PowerConsumptionRecord;

public class RaritanRestServer {
	
	private static final int PORT = 80; //TODO as parameter
	private static final String POST_URL = "/raritan"; //TODO as parameter
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RaritanRestServer.class); 
	
	private static final int RESPONSE_STATUS_CODE = 204; //TODO temp
	private static final String RESPONSE_STATUS_MESSAGE = ""; //TODO temp
	
	private final Service service;
	private final Queue<String> queue = new SpmcArrayQueue<>(16); 
	
	public RaritanRestServer() {
		service = Service.ignite().port(PORT);
		
		service.post(POST_URL, (Request request, Response response) -> {
			queue.add(request.body());
			response.status(RESPONSE_STATUS_CODE);
			return RESPONSE_STATUS_MESSAGE;
		});
	}

	public void start() {
		
		
		
	}
	
	public void stop() {
		service.stop();
	}
	
	public Queue<String> getQueue() {
		return this.queue;
	}
}
