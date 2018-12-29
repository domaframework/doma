package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;

@Domain(valueType = int.class)
public abstract class AbstractDomain {

  private final int value;

  protected AbstractDomain(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
