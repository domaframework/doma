package org.seasar.doma.internal.jdbc.scalar;

import java.util.OptionalDouble;
import java.util.function.Supplier;

public final class OptionalDoubleScalarSuppliers {

  public static Supplier<Scalar<Double, OptionalDouble>> of() {
    return OptionalDoubleScalar::new;
  }
}
