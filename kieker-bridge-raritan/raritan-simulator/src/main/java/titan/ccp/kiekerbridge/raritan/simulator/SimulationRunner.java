package titan.ccp.kiekerbridge.raritan.simulator;

import java.net.URI;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import jdk.incubator.http.HttpResponse.BodyHandler;
import titan.ccp.common.configuration.Configurations;

public class SimulationRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimulationRunner.class);

	private final HttpPusher httpPusher;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);

	private final AtomicLong counter = new AtomicLong(0);

	private final Collection<SensorReader> sensorReaders;

	public SimulationRunner(final URI uri, final Collection<SimulatedSensor> sensors) {
		this.httpPusher = new HttpPusher(uri);
		this.sensorReaders = sensors.stream().map(s -> new SensorReader(s)).collect(Collectors.toList());
	}

	public void run() {
		for (final SensorReader sensorReader : this.sensorReaders) {
			final Runnable sender = () -> {
				this.httpPusher.sendMessage(sensorReader.getMessage());
				this.counter.addAndGet(1);
			};
			this.scheduler.scheduleAtFixedRate(sender, 0, sensorReader.getSensor().getPeroid().toMillis(),
					TimeUnit.MILLISECONDS);
		}

	}

	public final long getCounter() {
		return this.counter.get();
	}

	public void shutdown() {
		this.scheduler.shutdownNow();
	}

	public static void main(final String[] args) throws InterruptedException {
		// Turn off Java's DNS caching
		java.security.Security.setProperty("networkaddress.cache.ttl", "0"); // TODO

		final Configuration configuration = Configurations.create();
		final String setupType = configuration.getString("setup", "demo");

		if (setupType.equals("scale")) { // TODO

			final int frequency = configuration.getInt("frequency", 1);
			final int sensorsCount = configuration.getInt("sensors", 1000);
			final List<SimulatedSensor> sensors = getScalabilitySetup(frequency, sensorsCount, 100);
			LOGGER.info("Use scalability setup with frequency: '{}' and sensors: '{}'", frequency, sensorsCount);

			final ScheduledExecutorService monitoringScheduler = Executors.newScheduledThreadPool(1);

			final SimulationRunner runner = new SimulationRunner(
					URI.create(configuration.getString("kieker.bridge.address")), sensors);
			runner.run();

			// Start input counter
			final long startTime = System.currentTimeMillis();
			monitoringScheduler.scheduleAtFixedRate(() -> {
				final long elapsedTime = System.currentTimeMillis() - startTime;
				System.out.println("input;" + elapsedTime + ";" + runner.getCounter());
			}, 0, 1, TimeUnit.SECONDS);

			// Start output counter
			final HttpClient httpClient = HttpClient.newHttpClient();
			monitoringScheduler.scheduleAtFixedRate(() -> {
				final HttpRequest request = HttpRequest.newBuilder()
						.uri(URI.create("http://cc01:31302/power-consumption-count")).GET().build();
				final BodyHandler<String> bodyHandler = HttpResponse.BodyHandler.asString();

				final long requestStartedTime = System.currentTimeMillis() - startTime;

				httpClient.sendAsync(request, bodyHandler).thenApply(r -> Long.parseLong(r.body())).thenAccept(v -> {
					final long elapsedTime = System.currentTimeMillis() - startTime;
					// countData.add(new CountData(elapsedTime, v));
					System.out.println("output;" + requestStartedTime + ";" + elapsedTime + ";" + v);
				});
			}, 0, 1, TimeUnit.SECONDS);

			// Wait for termination
			Thread.sleep(3 * 60 * 1000);
			runner.shutdown();
			monitoringScheduler.shutdownNow();

		} else if (setupType.equals("demo")) {
			LOGGER.info("Use demo setup");
			new SimulationRunner(URI.create(configuration.getString("kieker.bridge.address")), getDemoSetup()).run();
		} else {
			LOGGER.info("Use feasability setup");
			new SimulationRunner(URI.create(configuration.getString("kieker.bridge.address")), getFeasibilitySetup())
					.run();
		}

	}

	public static List<SimulatedSensor> getFeasibilitySetup() {
		return List.of(
				new SimulatedSensor("server1", Duration.ofSeconds(1),
						FunctionBuilder.of(x -> 50).plus(Functions.wave1()).plus(Functions.noise(10)).build()),
				new SimulatedSensor("server2", Duration.ofSeconds(2),
						FunctionBuilder.of(x -> 60).plus(Functions.noise(10)).build()),
				new SimulatedSensor("server3", Duration.ofSeconds(1),
						FunctionBuilder.of(x -> 40).plusScaled(20, Functions.squares(30_000, 10_000, 10 * 60_000))
								.plus(Functions.noise(5)).build()),
				new SimulatedSensor("printer1", Duration.ofSeconds(1),
						FunctionBuilder.of(x -> 50).plus(Functions.wave2()).plus(Functions.noise(5)).build()),
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
								.plus(Functions.noise(5)).build()),
				new SimulatedSensor("printer1", Duration.ofSeconds(1),
						FunctionBuilder.of(x -> 10)
								.plusScaled(80, Functions.squares(5 * 60_000, 15 * 60_000, 35 * 60_000))
								.plus(Functions.noise(20)).build()),
				new SimulatedSensor("printer2", Duration.ofSeconds(2),
						FunctionBuilder.of(x -> 5)
								.plusScaled(60, Functions.squares(1 * 60_000, 12 * 60_000, 19 * 60_000))
								.plus(Functions.noise(10)).build()),
				new SimulatedSensor("fan1", Duration.ofSeconds(10),
						FunctionBuilder.of(x -> 30).plus(Functions.wave2()).plus(Functions.noise(5)).build()),
				new SimulatedSensor("ac1", Duration.ofSeconds(1),
						FunctionBuilder.of(Functions.wave3()).plus(Functions.noise(5)).build()),
				new SimulatedSensor("ac2", Duration.ofSeconds(1),
						FunctionBuilder.of(Functions.wave3()).plus(Functions.noise(5)).build()));
	}

	public static List<SimulatedSensor> getScalabilitySetup(final int frequency, final int sensorsCount,
			final long totalTimeInS) {
		return IntStream.range(0, sensorsCount).mapToObj(i -> new SimulatedSensor("sensor" + i,
				Duration.ofMillis(frequency), FunctionBuilder.of(x -> 50).plus(Functions.noise(10)).build()))
				.collect(Collectors.toList());
	}
}
