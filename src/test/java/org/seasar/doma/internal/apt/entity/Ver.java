package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Domain;

@Domain(valueType = Integer.class)
public class Ver {

  private final Integer value;

  public Ver(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
