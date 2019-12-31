package titan.ccp.kiekerbridge.raritan.simulator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.function.LongToDoubleFunction;
import java.util.stream.IntStream;

/**
 * Functions to create {@link LongToDoubleFunction}s.
 */
public final class Functions {

  private Functions() {}

  public static LongToDoubleFunction squares(final long initialLowLength, final long highLength, // NOCS
      final long lowLength) {
    final long period = highLength + lowLength;
    final double normalizedHighLength = (double) highLength / period * 2 * Math.PI;
    final double horizontalOffset = Math.cos(normalizedHighLength / 2);
    return x -> Math
        .max(Math.signum(Math.cos((x - highLength / 2 - initialLowLength) * 2 * Math.PI / period)
            - horizontalOffset), 0);
  }

  public static LongToDoubleFunction noise(final int maxNoise) {
    final double half = maxNoise / 2;
    return x -> Math.random() * maxNoise - half;
  }

  public static LongToDoubleFunction wave1() {
    return x -> 5 * (10 + Math.sin((double) x / 20_000) + Math.sin(((double) x / 1000 + 1) / 40) // NOCS
        + 5 * Math.sin((double) x / 100_000) + Math.sin((double) x / 1000_000)); // NOCS
  }

  public static LongToDoubleFunction wave2() {
    return x -> 2 * (Math.sin((double) x / 1_000_000) + Math.sin((double) x / 200_000) + 4); // NOCS
  }

  public static LongToDoubleFunction wave3() {
    // 10*sin(1+(x/500))+ 30
    return x -> 10 * Math.sin(1 + (double) x / 500_000) + 30; // NOCS
  }

  /**
   * A function that:
   *  - has a constant value within an hour
   *  - has random values between hours of one week
   *  - has the same repeating pattern every week
   *
   * stepFunction : [0, Long.MAX_VALUE] -> [0,1)
   *
   * @return
   * The step function.
   */
  public static LongToDoubleFunction stepFunctionHours(long seed) {
    Random generator = new Random(seed);
    Double[][] values = IntStream.range(0,7).mapToObj(x ->
                          IntStream.range(0,24).mapToObj(y ->
                          generator.nextDouble())
                          .toArray(Double[]::new))
                        .toArray(Double[][]::new);
    return x -> {
      LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(x), SimulatedTime.getTimeZone());
      return values[ldt.getDayOfWeek().getValue()-1][ldt.getHour()];
    };
  }

  /**
   * A function that:
   *  - has a constant value within an day
   *  - has random values between days of one week
   *  - has the same repeating pattern every week
   *
   * stepFunction : [0, Long.MAX_VALUE] -> [0,1)
   *
   * @return
   * The step function.
   */
  public static LongToDoubleFunction stepFunctionDays(long seed) {
    Random generator = new Random(seed);
    Double[] values = IntStream.range(0,7)
                               .mapToObj(x -> generator.nextDouble())
                               .toArray(Double[]::new);
    return x -> {
      LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(x), SimulatedTime.getTimeZone());
      return values[ldt.getDayOfWeek().getValue()-1];
    };
  }

  /**
   * A function that returns an outlier value from a given {@link Queue} of {@link Double}
   * or 0 if no outliers are in the queue at the moment.
   * @param outliers
   * The queue containing outlier values
   * @return
   * The value of the outlier
   */
  public static LongToDoubleFunction outlier(Queue<Double> outliers) {
    return x -> {
      Double outlierValue = outliers.poll();
      if (outlierValue == null) {
        return 0.0;
      }
      return outlierValue;
    };
  }

}
