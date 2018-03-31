package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;

@Holder(valueType = int.class, factoryMethod = "of")
public class OfPrimitiveValueHolder {

  private final int value;

  private OfPrimitiveValueHolder(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static OfPrimitiveValueHolder of(int value) {
    return new OfPrimitiveValueHolder(value);
  }
}
