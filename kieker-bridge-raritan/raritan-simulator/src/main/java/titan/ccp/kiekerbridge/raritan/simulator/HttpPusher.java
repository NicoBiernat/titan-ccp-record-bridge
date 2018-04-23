package titan.ccp.kiekerbridge.raritan.simulator;

import java.net.URI;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import jdk.incubator.http.HttpResponse.BodyHandler;

public class HttpPusher {

	private final HttpClient client = HttpClient.newHttpClient();

	private final URI pushUri;

	public HttpPusher(final URI pushUri) {
		this.pushUri = pushUri;
	}

	public void sendMessage(final String message) {
		final HttpRequest request = HttpRequest.newBuilder().uri(this.pushUri)
				.POST(HttpRequest.BodyPublisher.fromString(message)).build();
		final BodyHandler<Void> bodyHandler = HttpResponse.BodyHandler.discard(null);
		this.client.sendAsync(request, bodyHandler);
	}
}
