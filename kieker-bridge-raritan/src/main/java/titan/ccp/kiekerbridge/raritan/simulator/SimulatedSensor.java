package titan.ccp.kiekerbridge.raritan.simulator;

import java.time.Duration;
import java.util.function.LongToIntFunction;

public class SimulatedSensor {

	private final String identifier;

	private final Duration peroid;

	private final LongToIntFunction valueFunction;

	public SimulatedSensor(final String identifier, final Duration peroid, final LongToIntFunction valueFunction) {
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

	public LongToIntFunction getValueFunction() {
		return this.valueFunction;
	}

}
