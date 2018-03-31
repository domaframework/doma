package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;

@Holder(valueType = int.class, acceptNull = true)
public class IllegalAcceptNullHolder {

  private final int value;

  public IllegalAcceptNullHolder(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
