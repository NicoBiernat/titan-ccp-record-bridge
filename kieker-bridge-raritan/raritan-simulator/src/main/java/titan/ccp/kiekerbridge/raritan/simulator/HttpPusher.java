package titan.ccp.kiekerbridge.raritan.simulator;

import java.net.URI;
import java.time.Duration;
import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import jdk.incubator.http.HttpResponse.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Push messages via HTTP to a configured destination ({@link URI}).
 */
public class HttpPusher {

  private static final int PUSH_TIMEOUT_SECONDS = 10;

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpPusher.class);

  private final HttpClient client = HttpClient.newHttpClient();

  private final URI pushUri;

  public HttpPusher(final URI pushUri) {
    this.pushUri = pushUri;
  }

  /**
   * Push the passed message.
   */
  public void sendMessage(final String message) {
    final HttpRequest request =
        HttpRequest.newBuilder().uri(this.pushUri).timeout(Duration.ofSeconds(PUSH_TIMEOUT_SECONDS))
            .POST(HttpRequest.BodyPublisher.fromString(message)).build();
    final BodyHandler<Void> bodyHandler = HttpResponse.BodyHandler.discard(null);

    this.client.sendAsync(request, bodyHandler).thenAccept(r -> {
      LOGGER.info("Pushed message"); // TODO debug level
    }).exceptionally(e -> {
      LOGGER.warn("Failed to push message.", e);
      return null;
    });

  }
}
