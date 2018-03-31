package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Holder;

@Holder(valueType = Integer.class)
public class Identifier {

  private final Integer value;

  public Identifier(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
