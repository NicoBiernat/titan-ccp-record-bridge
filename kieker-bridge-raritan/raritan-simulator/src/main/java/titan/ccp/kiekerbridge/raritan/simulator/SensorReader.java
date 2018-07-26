package titan.ccp.kiekerbridge.raritan.simulator;

import java.text.MessageFormat;

public class SensorReader {

	private final SimulatedSensor sensor;

	private final long startTimestamp;

	private final boolean sendTimestampsInMs;

	public SensorReader(final SimulatedSensor sensor) {
		this(sensor, false, System.currentTimeMillis());
	}

	public SensorReader(final SimulatedSensor sensor, final boolean sendTimestampsInMs) {
		this(sensor, sendTimestampsInMs, System.currentTimeMillis());
	}

	public SensorReader(final SimulatedSensor sensor, final boolean sendTimestampsInMs, final long startTimestamp) {
		this.sensor = sensor;
		this.startTimestamp = startTimestamp;
		this.sendTimestampsInMs = sendTimestampsInMs;
	}

	public String getMessage() {
		return this.getMessage(System.currentTimeMillis());
	}

	public String getMessage(final long timestampInMs) {
		final double value = this.getValue(timestampInMs);
		final long covertedTimestamp = this.sendTimestampsInMs ? timestampInMs : timestampInMs / 1_000;
		return MessageFormat.format(JsonTemplate.TEMPLATE, this.sensor.getIdentifier(),
				String.valueOf(covertedTimestamp), String.valueOf(value));
	}

	public double getValue() {
		return this.getValue(System.currentTimeMillis());
	}

	public double getValue(final long timestamp) {
		final long millisSinceStart = timestamp - this.startTimestamp;
		return this.sensor.getValueFunction().applyAsDouble(millisSinceStart);
	}

	public SimulatedSensor getSensor() {
		return this.sensor;
	}

}
