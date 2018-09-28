package titan.ccp.kiekerbridge.raritan.simulator;

import java.time.Duration;
import java.util.function.LongToDoubleFunction;

/**
 * A simulated power consumption sensor. It has an identifier, a interval its value is requested and
 * a function that defines its value.
 */
public class SimulatedSensor {

  private final String identifier;

  private final Duration peroid;

  private final LongToDoubleFunction valueFunction;

  /**
   * Create a new simulated sensor.
   */
  public SimulatedSensor(final String identifier, final Duration peroid,
      final LongToDoubleFunction valueFunction) {
    this.identifier = identifier;
    this.peroid = peroid;
    this.valueFunction = valueFunction;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public Duration getPeroid() {
    return this.peroid;
  }

  public LongToDoubleFunction getValueFunction() {
    return this.valueFunction;
  }

}
