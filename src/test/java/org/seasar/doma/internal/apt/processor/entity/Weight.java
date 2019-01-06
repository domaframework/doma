package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Domain;

@Domain(valueType = Integer.class)
public class Weight<T> {

  private final Integer value;

  public Weight(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
