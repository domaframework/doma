package org.seasar.doma.it.domain;

import org.seasar.doma.Domain;

@Domain(valueType = Integer.class)
public class Height {

  private final Integer value;

  public Height(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
