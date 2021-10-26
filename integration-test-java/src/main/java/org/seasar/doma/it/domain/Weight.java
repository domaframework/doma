package org.seasar.doma.it.domain;

import org.seasar.doma.Domain;

@Domain(valueType = Integer.class, acceptNull = true)
public class Weight {

  private final Integer value;

  public Weight(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
