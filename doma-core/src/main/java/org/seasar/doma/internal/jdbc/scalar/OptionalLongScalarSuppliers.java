package org.seasar.doma.internal.jdbc.scalar;

import java.util.OptionalLong;
import java.util.function.Supplier;

public final class OptionalLongScalarSuppliers {

  public static Supplier<Scalar<Long, OptionalLong>> of() {
    return OptionalLongScalar::new;
  }
}
