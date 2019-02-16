package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Supplier;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;

public class SingleResultCallback<TARGET> implements IterationCallback<TARGET, TARGET> {

  protected final Supplier<TARGET> supplier;

  public SingleResultCallback() {
    this(() -> null);
  }

  public SingleResultCallback(Supplier<TARGET> supplier) {
    assertNotNull(supplier);
    this.supplier = supplier;
  }

  @Override
  public TARGET defaultResult() {
    return supplier.get();
  }

  @Override
  public TARGET iterate(TARGET target, IterationContext context) {
    context.exit();
    return target;
  }
}
