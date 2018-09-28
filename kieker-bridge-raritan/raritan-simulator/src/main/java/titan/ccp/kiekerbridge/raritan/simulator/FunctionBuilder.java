package titan.ccp.kiekerbridge.raritan.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongToDoubleFunction;

public final class FunctionBuilder {

  private final List<LongToDoubleFunction> functions = new ArrayList<>();

  private FunctionBuilder(final LongToDoubleFunction function) {
    this.functions.add(function);
  }

  public FunctionBuilder plus(final LongToDoubleFunction function) {
    this.functions.add(function);
    return this;
  }

  public FunctionBuilder plusScaled(final double factor, final LongToDoubleFunction function) {
    this.functions.add(x -> factor * function.applyAsDouble(x));
    return this;
  }

  public LongToDoubleFunction build() {
    return x -> functions.stream().mapToDouble(f -> f.applyAsDouble(x)).sum();
  }

  public static FunctionBuilder of(final LongToDoubleFunction function) {
    return new FunctionBuilder(function);
  }
}
