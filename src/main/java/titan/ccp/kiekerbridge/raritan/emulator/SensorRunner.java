package titan.ccp.kiekerbridge.raritan.emulator;

import java.net.URI;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SensorRunner {

	private final HttpPusher httpPusher;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	private final Collection<SensorReader> sensorReaders;

	public SensorRunner(final URI uri, final Collection<EmulatedSensor> sensors) {
		this.httpPusher = new HttpPusher(uri);
		this.sensorReaders = sensors.stream().map(s -> new SensorReader(s)).collect(Collectors.toList());
	}

	public void run() {
		for (final SensorReader sensorReader : this.sensorReaders) {
			final Runnable sender = () -> this.httpPusher.sendMessage(sensorReader.getMessage());
			this.scheduler.scheduleAtFixedRate(sender, 0, sensorReader.getSensor().getPeroid().toMillis(),
					TimeUnit.MILLISECONDS);
		}
	}

	public static void main(final String[] args) {
		new SensorRunner(URI.create("http://localhost:80/raritan"),
				List.of(new EmulatedSensor("comcent.server1.pw1", Duration.ofSeconds(5), x -> 10))).run();
	}

}
