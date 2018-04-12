package titan.ccp.kiekerbridge.raritan.dummy;

import java.net.URI;
import java.text.MessageFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

//TODO move to .emulator
//TODO rename to SensorRunner
public class RaritanDummySensor {

	private static final URI PUSH_URI = URI.create("http://localhost:80/raritan");

	private final String sensor;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final HttpClient client = HttpClient.newHttpClient();

	public RaritanDummySensor(final String sensor) {
		this.sensor = sensor;
	}

	public void run() {
		final Runnable sender = () -> this.sendMessage(this.generateMessage(10));
		this.scheduler.scheduleAtFixedRate(sender, 0, 10, TimeUnit.SECONDS);
	}

	private String generateMessage(final int value) {
		final long timestamp = System.currentTimeMillis();
		return MessageFormat.format(JsonTemplate.TEMPLATE, this.sensor, String.valueOf(timestamp),
				String.valueOf(value));
	}

	private void sendMessage(final String message) {
		// final HttpRequest request =
		// HttpRequest.newBuilder().uri(PUSH_URI).POST(HttpRequest.BodyProcessor.fromString(message)).build();
		System.out.println("send msg");

		final HttpRequest request = HttpRequest.newBuilder().uri(PUSH_URI)
				.POST(HttpRequest.BodyProcessor.fromString(message)).build();
		this.client.sendAsync(request, HttpResponse.BodyHandler.<Void>discard(null));
	}

	public static void main(final String[] args) {
		new RaritanDummySensor("my-sensor").run();
	}

}
