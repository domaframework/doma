package org.seasar.doma.it.domain;

import org.seasar.doma.Domain;

@Domain(valueType = Integer.class)
public class Identity<T> {

  private final Integer value;

  public Identity(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return this.value;
  }
}
