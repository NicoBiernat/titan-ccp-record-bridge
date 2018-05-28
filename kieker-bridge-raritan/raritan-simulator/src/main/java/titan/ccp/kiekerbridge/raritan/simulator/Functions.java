package titan.ccp.kiekerbridge.raritan.simulator;

import java.util.function.LongToDoubleFunction;

public final class Functions {

	public static LongToDoubleFunction squares(final long initialLowLength, final long highLength,
			final long lowLength) {
		final long period = highLength + lowLength;
		final double normalizedHighLength = (double) highLength / period * 2 * Math.PI;
		final double horizontalOffset = Math.cos(normalizedHighLength / 2);
		return x -> Math.max(
				Math.signum(
						Math.cos((x - highLength / 2 - initialLowLength) * 2 * Math.PI / period) - horizontalOffset),
				0);
	}

	public static LongToDoubleFunction noise(final int maxNoise) {
		final double half = maxNoise / 2;
		return x -> Math.random() * maxNoise - half;
	}

	public static LongToDoubleFunction wave1() {
		return x -> 5 * (10 + Math.sin(x / 20_000) + Math.sin((x / 1000 + 1) / 40) + 5 * Math.sin(x / 100_000)
				+ Math.sin(x / 1000_000));
	}

	public static LongToDoubleFunction wave2() {
		return x -> 2 * (Math.sin(x / 1_000_000) + Math.sin(x / 200_000) + 4);
	}

}
