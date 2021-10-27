package org.seasar.doma.it.domain;

import org.seasar.doma.Domain;

@Domain(valueType = int.class)
public class PrimitiveHeight {

  private final int value;

  public PrimitiveHeight(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
