package titan.ccp.kiekerbridge.raritan.dummy;

import java.net.URI;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

//TODO move to .emulator
//TODO rename to Sender and only send messages
public class RaritanDummySensor {

	private static final URI PUSH_URI = URI.create("http://localhost:80/raritan");

	private final HttpClient client = HttpClient.newHttpClient();

	public RaritanDummySensor() {
	}

	public void sendMessage(final String message) {
		final HttpRequest request = HttpRequest.newBuilder().uri(PUSH_URI)
				.POST(HttpRequest.BodyProcessor.fromString(message)).build();
		this.client.sendAsync(request, HttpResponse.BodyHandler.<Void>discard(null));
	}
}
