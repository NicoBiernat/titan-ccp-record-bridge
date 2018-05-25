package titan.ccp.kiekerbridge.raritan.simulator;

import java.net.URI;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.configuration2.Configuration;

import titan.ccp.common.configuration.Configurations;

public class SimulationRunner {

	private final HttpPusher httpPusher;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	private final Collection<SensorReader> sensorReaders;

	public SimulationRunner(final URI uri, final Collection<SimulatedSensor> sensors) {
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
		final Configuration configuration = Configurations.create();
		new SimulationRunner(URI.create(configuration.getString("kieker.bridge.address")),
				// List.of(new SimulatedSensor("comcent.server1.pw1", Duration.ofSeconds(1),
				List.of(new SimulatedSensor("server1", Duration.ofSeconds(2),
						x -> (int) (Math.random() * 10) - 5 + 100),
						new SimulatedSensor("printer1", Duration.ofSeconds(1),
								x -> (int) (Math.random() * 10) - 5 + 120))).run();
	}

}
