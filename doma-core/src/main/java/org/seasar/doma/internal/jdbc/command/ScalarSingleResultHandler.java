package org.seasar.doma.internal.jdbc.command;

import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;

public class ScalarSingleResultHandler<BASIC, CONTAINER>
    extends AbstractSingleResultHandler<CONTAINER> {

  public ScalarSingleResultHandler(Supplier<Scalar<BASIC, CONTAINER>> supplier) {
    super(
        new ScalarIterationHandler<>(
            supplier, new SingleResultCallback<CONTAINER>(() -> supplier.get().getDefault())));
  }
}
