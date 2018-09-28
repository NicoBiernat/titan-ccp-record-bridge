package titan.ccp.kiekerbridge.raritan.simulator;

import java.time.Duration;
import java.util.function.LongToDoubleFunction;

public class SimulatedSensor {

  private final String identifier;

  private final Duration peroid;

  private final LongToDoubleFunction valueFunction;

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
