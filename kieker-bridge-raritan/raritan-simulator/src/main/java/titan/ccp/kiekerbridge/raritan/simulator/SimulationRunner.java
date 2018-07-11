package titan.ccp.kiekerbridge.raritan.simulator;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
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

	private final AtomicLong counter = new AtomicLong(0);

	private final Collection<SensorReader> sensorReaders;

	public SimulationRunner(final URI uri, final Collection<SimulatedSensor> sensors) {
		this.httpPusher = new HttpPusher(uri);
		this.sensorReaders = sensors.stream().map(s -> new SensorReader(s)).collect(Collectors.toList());
	}

	public List<ScheduledFuture<?>> run() {
		final List<ScheduledFuture<?>> futures = new ArrayList<>(this.sensorReaders.size());
		for (final SensorReader sensorReader : this.sensorReaders) {
			final Runnable sender = () -> {
				this.httpPusher.sendMessage(sensorReader.getMessage());
				this.counter.addAndGet(1);
			};
			final ScheduledFuture<?> future = this.scheduler.scheduleAtFixedRate(sender, 0,
					sensorReader.getSensor().getPeroid().toMillis(), TimeUnit.MILLISECONDS);
			futures.add(future);
		}
		// this.scheduler.schedule(() -> {
		// System.out.println(this.counter.get());
		// System.exit(0);
		// }, 10, TimeUnit.SECONDS);
		return futures;
	}

	public static void main(final String[] args) throws InterruptedException {
		// Turn off Java's DNS caching
		java.security.Security.setProperty("networkaddress.cache.ttl", "0"); // TODO

		final Configuration configuration = Configurations.create();

		final String setupType = configuration.getString("setup", "feas");

		List<SimulatedSensor> sensors;
		if (setupType.equals("scale")) {
			final int frequency = configuration.getInt("frequency", 1);
			final int sensorsCount = configuration.getInt("sensors", 1000);
			sensors = getScalabilitySetup(frequency, sensorsCount, 100);
			LOGGER.info("Use scalability setup");
		} else if (setupType.equals("demo")) {
			sensors = getDemoSetup();
			LOGGER.info("Use demo setup");
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

	public static List<SimulatedSensor> getDemoSetup() {
		return List.of(
				new SimulatedSensor("server1", Duration.ofSeconds(1),
						FunctionBuilder.of(x -> 50).plus(Functions.wave1()).plus(Functions.noise(10)).build()),
				new SimulatedSensor("server2", Duration.ofSeconds(2),
						FunctionBuilder.of(x -> 60).plus(Functions.noise(20)).build()),
				new SimulatedSensor("server3", Duration.ofSeconds(1),
						FunctionBuilder.of(x -> 30).plusScaled(20, Functions.squares(4 * 60_000, 100_000, 5 * 60_000))
								.plus(Functions.noise(5)).build()), // Aright
				new SimulatedSensor("printer1", Duration.ofSeconds(1),
						FunctionBuilder.of(x -> 10)
								.plusScaled(80, Functions.squares(5 * 60_000, 15 * 60_000, 35 * 60_000))
								.plus(Functions.noise(20)).build()), // Aright
				new SimulatedSensor("printer2", Duration.ofSeconds(2),
						FunctionBuilder.of(x -> 5)
								.plusScaled(60, Functions.squares(1 * 60_000, 12 * 60_000, 19 * 60_000))
								.plus(Functions.noise(10)).build()), // Aright
				new SimulatedSensor("fan1", Duration.ofSeconds(10),
						FunctionBuilder.of(x -> 30).plus(Functions.wave2()).plus(Functions.noise(5)).build()),
				new SimulatedSensor("ac1", Duration.ofSeconds(1),
						FunctionBuilder.of(Functions.wave3()).plus(Functions.noise(10)).build()), // Aright
				new SimulatedSensor("ac2", Duration.ofSeconds(1),
						FunctionBuilder.of(Functions.wave3()).plus(Functions.noise(10)).build())); // Aright
	}

	public static List<SimulatedSensor> getScalabilitySetup(final int frequency, final int sensorsCount,
			final long totalTimeInS) {
		return IntStream.range(0, sensorsCount).mapToObj(i -> new SimulatedSensor("sensor" + i,
				Duration.ofMillis(frequency), FunctionBuilder.of(x -> 50).plus(Functions.noise(10)).build()))
				.collect(Collectors.toList());
	}
}
