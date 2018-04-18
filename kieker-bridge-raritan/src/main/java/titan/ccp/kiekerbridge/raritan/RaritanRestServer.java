package titan.ccp.kiekerbridge.raritan;

import java.util.Queue;

import org.jctools.queues.SpmcArrayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Service;
import titan.ccp.kiekerbridge.QueueProvider;

public class RaritanRestServer implements QueueProvider<String> {

	private static final int PORT = 80; // TODO as parameter
	private static final String POST_URL = "/raritan"; // TODO as parameter

	private static final Logger LOGGER = LoggerFactory.getLogger(RaritanRestServer.class);

	private static final int RESPONSE_STATUS_CODE = 204; // TODO temp
	private static final String RESPONSE_STATUS_MESSAGE = ""; // TODO temp

	private final Service service;
	private final Queue<String> queue = new SpmcArrayQueue<>(1024);

	public RaritanRestServer() {
		this.service = Service.ignite().port(PORT);

		this.service.post(POST_URL, (final Request request, final Response response) -> {
			this.queue.add(request.body());
			response.status(RESPONSE_STATUS_CODE);
			return RESPONSE_STATUS_MESSAGE;
		});
	}

	public void start() {

	}

	public void stop() {
		this.service.stop();
	}

	@Override
	public Queue<String> getQueue() {
		return this.queue;
	}
}
