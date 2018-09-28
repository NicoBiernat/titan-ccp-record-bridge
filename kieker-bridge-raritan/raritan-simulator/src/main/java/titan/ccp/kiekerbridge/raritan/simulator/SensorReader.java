package titan.ccp.kiekerbridge.raritan.simulator;

import java.text.MessageFormat;

/**
 * Reads the value from a (simulated) sensor and makes it accessible.
 */
public class SensorReader {

  private final JsonTemplate jsonTemplate = new JsonTemplate();

  private final SimulatedSensor sensor;

  private final long startTimestamp;

  private final boolean sendTimestampsInMs;

  public SensorReader(final SimulatedSensor sensor) {
    this(sensor, false, System.currentTimeMillis());
  }

  /**
   * Create a new sensor reader.
   *
   * @param sensor The sensor data should be read from
   * @param sendTimestampsInMs Whether the values should be sent in milliseconds.
   */
  public SensorReader(final SimulatedSensor sensor, final boolean sendTimestampsInMs) {
    this(sensor, sendTimestampsInMs, System.currentTimeMillis());
  }

  /**
   * Create a new sensor reader.
   *
   * @param sensor The sensor data should be read from
   * @param sendTimestampsInMs Whether the values should be sent in milliseconds.
   * @param startTimestamp The start timestamp
   */
  public SensorReader(final SimulatedSensor sensor, final boolean sendTimestampsInMs,
      final long startTimestamp) {
    this.sensor = sensor;
    this.startTimestamp = startTimestamp;
    this.sendTimestampsInMs = sendTimestampsInMs;
  }

  public String getMessage() {
    return this.getMessage(System.currentTimeMillis());
  }

  /**
   * Get a JSON message of the read value.
   */
  public String getMessage(final long timestampInMs) {
    final double value = this.getValue(timestampInMs);
    final long covertedTimestamp = this.sendTimestampsInMs ? timestampInMs : timestampInMs / 1_000; // NOCS
    return MessageFormat.format(this.jsonTemplate.getTemplate(), this.sensor.getIdentifier(),
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
