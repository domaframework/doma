package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;

@Holder(valueType = int.class)
public abstract class AbstractHolder {

  private final int value;

  protected AbstractHolder(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
