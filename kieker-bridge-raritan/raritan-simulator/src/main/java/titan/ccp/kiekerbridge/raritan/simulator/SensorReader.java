package titan.ccp.kiekerbridge.raritan.simulator;

import java.text.MessageFormat;

public class SensorReader {

	private final SimulatedSensor sensor;

	private final long startTimestamp;

	public SensorReader(final SimulatedSensor sensor) {
		this(sensor, System.currentTimeMillis());
	}

	public SensorReader(final SimulatedSensor sensor, final long startTimestamp) {
		this.sensor = sensor;
		this.startTimestamp = startTimestamp;
	}

	public String getMessage() {
		return this.getMessage(System.currentTimeMillis());
	}

	public String getMessage(final long timestamp) {
		final double value = this.getValue(timestamp);
		return MessageFormat.format(JsonTemplate.TEMPLATE, this.sensor.getIdentifier(), String.valueOf(timestamp),
				String.valueOf(value));
	}

	public double getValue() {
		return this.getValue(System.currentTimeMillis());
	}

	public double getValue(final long timestamp) {
		final long millisSinceStart = this.startTimestamp - timestamp;
		return this.sensor.getValueFunction().applyAsDouble(millisSinceStart);
	}

	public SimulatedSensor getSensor() {
		return this.sensor;
	}

}
