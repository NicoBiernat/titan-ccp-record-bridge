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

  private static final Logger LOGGER = LoggerFactory.getLogger(RaritanRestServer.class);

  private static final int RESPONSE_STATUS_CODE = 200;
  private static final String RESPONSE_STATUS_MESSAGE = "";

  private final Service service;
  private final String postUrl;
  private final String idQueryParameter;

  private final Queue<PushMessage> queue;

  /**
   * Creates a new web server for receiving push messages sent by a Raritan PDU.
   *
   * @param port Port this web server should listen on
   * @param postUrl URL to which records are sent.
   * @param idQueryParameter Name of an optional query parameter to distinguish multiple PDUs.
   * @param queueSize Size of the internal queue.
   */
  public RaritanRestServer(final int port, final String postUrl, final String idQueryParameter,
      final int queueSize) {
    this.service = Service.ignite().port(port);
    this.postUrl = postUrl;
    this.idQueryParameter = idQueryParameter;

    this.queue = new MpscArrayQueue<>(queueSize); // Non-blocking, but lock-free
    // this.queue = new ArrayBlockingQueue<>(1024); // Blocking, but not lock-free
  }

  /**
   * Start the server.
   */
  public void start() {
    this.service.post(this.postUrl, (final Request request, final Response response) -> {
      final String id = request.queryParamOrDefault(this.idQueryParameter, null);
      LOGGER.info("Received push message on {}:{}", this.postUrl, this.service.port());
      // TODO change to debug
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

