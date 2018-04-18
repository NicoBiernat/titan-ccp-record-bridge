package titan.ccp.kiekerbridge.raritan.simulator;

import java.net.URI;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

public class HttpPusher {

	private final HttpClient client = HttpClient.newHttpClient();

	private final URI pushUri;

	public HttpPusher(final URI pushUri) {
		this.pushUri = pushUri;
	}

	public void sendMessage(final String message) {
		final HttpRequest request = HttpRequest.newBuilder().uri(this.pushUri)
				.POST(HttpRequest.BodyProcessor.fromString(message)).build();
		this.client.sendAsync(request, HttpResponse.BodyHandler.<Void>discard(null));
	}
}
