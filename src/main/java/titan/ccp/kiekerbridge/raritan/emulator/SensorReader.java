package titan.ccp.kiekerbridge.raritan.emulator;

import java.text.MessageFormat;

public class SensorReader {

	private final EmulatedSensor sensor;

	private final long startTimestamp;

	public SensorReader(final EmulatedSensor sensor) {
		this(sensor, System.currentTimeMillis());
	}

	public SensorReader(final EmulatedSensor sensor, final long startTimestamp) {
		this.sensor = sensor;
		this.startTimestamp = startTimestamp;
	}

	public String getMessage() {
		return this.getMessage(System.currentTimeMillis());
	}

	public String getMessage(final long timestamp) {
		final int value = this.getValue(timestamp);
		return MessageFormat.format(JsonTemplate.TEMPLATE, this.sensor.getIdentifier(), String.valueOf(timestamp),
				String.valueOf(value));
	}

	public int getValue() {
		return this.getValue(System.currentTimeMillis());
	}

	public int getValue(final long timestamp) {
		final long millisSinceStart = this.startTimestamp - timestamp;
		return this.sensor.getValueFunction().applyAsInt(millisSinceStart);
	}

	public EmulatedSensor getSensor() {
		return this.sensor;
	}

}
