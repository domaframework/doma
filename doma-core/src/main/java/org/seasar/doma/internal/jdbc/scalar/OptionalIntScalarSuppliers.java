package org.seasar.doma.internal.jdbc.scalar;

import java.util.OptionalInt;
import java.util.function.Supplier;

public final class OptionalIntScalarSuppliers {

  public static Supplier<Scalar<Integer, OptionalInt>> of() {
    return OptionalIntScalar::new;
  }
}
