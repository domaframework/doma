package org.seasar.doma.internal.jdbc.entity;

import java.util.function.Supplier;
import org.seasar.doma.jdbc.entity.NullEntityListener;

public class NullEntityListenerSuppliers {

  public static <ENTITY> Supplier<NullEntityListener<ENTITY>> of() {
    return NullEntityListener::new;
  }
}
