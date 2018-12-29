package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;

@Domain(valueType = int.class, factoryMethod = "of")
public class OfPrimitiveValueDomain {

  private final int value;

  private OfPrimitiveValueDomain(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static OfPrimitiveValueDomain of(int value) {
    return new OfPrimitiveValueDomain(value);
  }
}
