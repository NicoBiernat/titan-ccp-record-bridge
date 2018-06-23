package titan.ccp.kiekerbridge.raritan;

import java.util.Queue;

import org.jctools.queues.MpscArrayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Service;

public class RaritanRestServer {

	private static final int PORT = 80; // TODO as parameter
	private static final String POST_URL = "/raritan"; // TODO as parameter

	private static final Logger LOGGER = LoggerFactory.getLogger(RaritanRestServer.class);

	private static final int RESPONSE_STATUS_CODE = 200; // TODO temp
	private static final String RESPONSE_STATUS_MESSAGE = ""; // TODO temp

	private final Service service;
	private final Queue<String> queue = new MpscArrayQueue<>(1024); // Non-blocking, but lock-free
	// private final Queue<String> queue = new ArrayBlockingQueue<>(1024); //
	// Blocking, but not lock-free

	public RaritanRestServer() {
		this.service = Service.ignite().port(PORT);
		LOGGER.info("Instantiate Spark server.");

		this.service.post(POST_URL, (final Request request, final Response response) -> {
			LOGGER.info("Received push message on {}:{}", POST_URL, PORT); // TODO change to debug
			try {
				this.queue.add(request.body());
			} catch (final IllegalStateException e) {
				LOGGER.warn("Element cannot be added since queue capacity is exhausted.", e);
			}

			response.status(RESPONSE_STATUS_CODE);
			return RESPONSE_STATUS_MESSAGE;
		});
	}

	public void start() {

	}

	public void stop() {
		this.service.stop();
	}

	public Queue<String> getQueue() {
		return this.queue;
	}
}
