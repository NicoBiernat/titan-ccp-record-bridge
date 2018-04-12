package titan.ccp.kiekerbridge.raritan.emulator;

import java.net.URI;
import java.text.MessageFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jdk.incubator.http.HttpClient;
import titan.ccp.kiekerbridge.raritan.dummy.RaritanDummySensor;

//TODO move to .emulator
//TODO rename to Sender and only send messages
public class SensorRunner {

	private static final URI PUSH_URI = URI.create("http://localhost:80/raritan");

	private final RaritanDummySensor httpPusher = new RaritanDummySensor(null);

	private final String sensor;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final HttpClient client = HttpClient.newHttpClient();

	public SensorRunner(final String sensor) {
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
		this.httpPusher.sendMessage(message);
	}

	public static void main(final String[] args) {
		new RaritanDummySensor("my-sensor").run();
	}

}
