package titan.ccp.kiekerbridge.raritan.simulator;

import java.net.URI;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import titan.ccp.common.configuration.Configurations;

public class SimulationRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimulationRunner.class);

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
		java.security.Security.setProperty("networkaddress.cache.ttl", "5");

		final Configuration configuration = Configurations.create();

		final String setupType = configuration.getString("setup", "feas");

		List<SimulatedSensor> sensors;
		if (setupType.equals("scale")) {
			final int frequency = configuration.getInt("frequency", 1);
			final int sensorsCount = configuration.getInt("sensors", 1000);
			sensors = getScalabilitySetup(frequency, sensorsCount);
			LOGGER.info("Use scalability setup");
		} else {
			sensors = getFeasibilitySetup();
			LOGGER.info("Use feasability setup");
		}

		new SimulationRunner(URI.create(configuration.getString("kieker.bridge.address")), sensors).run();
	}

	public static List<SimulatedSensor> getFeasibilitySetup() {
		return List.of(
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
						FunctionBuilder.of(x -> 60).plus(Functions.noise(10)).build()));
	}

	public static List<SimulatedSensor> getScalabilitySetup(final int frequency, final int sensorsCount) {
		return IntStream.range(0, sensorsCount).mapToObj(i -> new SimulatedSensor("sensor" + i,
				Duration.ofMillis(frequency), FunctionBuilder.of(x -> 50).plus(Functions.noise(10)).build()))
				.collect(Collectors.toList());
	}
}
