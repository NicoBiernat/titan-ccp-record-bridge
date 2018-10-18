package titan.ccp.kiekerbridge.raritan;

import java.util.Queue;
import org.jctools.queues.MpscArrayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Service;

/**
 * Rest server to receive Raritan push messages and store them to a queue.
 */
public class RaritanRestServer {

  private static final int PORT = 8080; // TODO as parameter
  private static final String POST_URL = "/raritan"; // TODO as parameter
  private static final String ID_QUERY_PARAMETER = "id"; // TODO as parameter

  private static final int QUEUE_SIZE = 1024; // TODO as parameter

  private static final Logger LOGGER = LoggerFactory.getLogger(RaritanRestServer.class);

  private static final int RESPONSE_STATUS_CODE = 200; // TODO temp
  private static final String RESPONSE_STATUS_MESSAGE = ""; // TODO temp

  private final Service service;

  private final Queue<PushMessage> queue = new MpscArrayQueue<>(QUEUE_SIZE);
  // Non-blocking, but lock-free
  // private final Queue<String> queue = new ArrayBlockingQueue<>(1024);
  // Blocking, but not lock-free

  public RaritanRestServer() {
    this.service = Service.ignite().port(PORT);
  }

  /**
   * Start the server.
   */
  public void start() {
    this.service.post(POST_URL, (final Request request, final Response response) -> {
      final String id = request.queryParamOrDefault(ID_QUERY_PARAMETER, null);
      LOGGER.info("Received push message on {}:{}", POST_URL, PORT); // TODO change to debug
      try {
        this.queue.add(new PushMessage(id, request.body()));
      } catch (final IllegalStateException e) {
        LOGGER.warn("Element cannot be added since queue capacity is exhausted.", e);
      }

      response.status(RESPONSE_STATUS_CODE);
      return RESPONSE_STATUS_MESSAGE;
    });
    LOGGER.info("Instantiate Spark server.");
  }

  /**
   * Stop the server.
   */
  public void stop() {
    this.service.stop();
  }

  public Queue<PushMessage> getQueue() {
    return this.queue;
  }
}

