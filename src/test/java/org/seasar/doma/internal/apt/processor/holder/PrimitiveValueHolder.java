package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;

@Holder(valueType = int.class)
public class PrimitiveValueHolder {

  private final int value;

  public PrimitiveValueHolder(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
