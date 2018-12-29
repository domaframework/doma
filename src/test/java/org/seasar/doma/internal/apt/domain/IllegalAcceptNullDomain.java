package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;

@Domain(valueType = int.class, acceptNull = true)
public class IllegalAcceptNullDomain {

  private final int value;

  public IllegalAcceptNullDomain(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
