package org.seasar.doma.internal.apt.processor.domain;

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
