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

	public static void main(final String[] args) throws InterruptedException {
		final Configuration configuration = Configurations.create();

		new SimulationRunner(URI.create(configuration.getString("kieker.bridge.address")), List.of(
				new SimulatedSensor("server1", Duration.ofSeconds(1),
						FunctionBuilder.of(x -> 50).plus(Functions.wave1()).plus(Functions.noise(10)).build()),
				new SimulatedSensor("server2", Duration.ofSeconds(2),
						FunctionBuilder.of(x -> 60).plus(Functions.noise(20)).build()),
				new SimulatedSensor("server3", Duration.ofSeconds(1),
						FunctionBuilder.of(x -> 40).plusScaled(20, Functions.squares(30_000, 10_000, 10 * 60_000))
								.plus(Functions.noise(5)).build()),
				new SimulatedSensor("printer1", Duration.ofSeconds(1),
						FunctionBuilder.of(x -> 50).plus(Functions.wave2()).plus(Functions.noise(10)).build()),
				new SimulatedSensor("printer2", Duration.ofSeconds(1),
						FunctionBuilder.of(x -> 60).plus(Functions.noise(10)).build()))).run();
	}

}
