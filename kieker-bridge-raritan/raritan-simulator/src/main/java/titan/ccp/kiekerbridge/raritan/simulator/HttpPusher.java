package titan.ccp.kiekerbridge.raritan.simulator;

import java.net.URI;
import java.time.Duration;
import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import jdk.incubator.http.HttpResponse.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpPusher {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpPusher.class);

  private final HttpClient client = HttpClient.newHttpClient();

  private final URI pushUri;

  public HttpPusher(final URI pushUri) {
    this.pushUri = pushUri;
  }

  public void sendMessage(final String message) {
    final HttpRequest request =
        HttpRequest.newBuilder().uri(this.pushUri).timeout(Duration.ofSeconds(10))
            .POST(HttpRequest.BodyPublisher.fromString(message)).build();
    final BodyHandler<Void> bodyHandler = HttpResponse.BodyHandler.discard(null);

    // TODO "Pushed message" ist printed always
    this.client.sendAsync(request, bodyHandler).exceptionally(e -> {
      LOGGER.warn("Failed to push message.", e);
      return null;
    }).thenAccept(r -> LOGGER.info("Pushed message")); // TODO debug level

  }
}
