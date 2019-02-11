package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Domain;

@Domain(valueType = int.class)
public class Age {

  private final int value;

  public Age(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
