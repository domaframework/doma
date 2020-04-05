package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Domain;

@Domain(valueType = Integer.class)
public class Height<T> {

  private final Integer value;

  public Height(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
