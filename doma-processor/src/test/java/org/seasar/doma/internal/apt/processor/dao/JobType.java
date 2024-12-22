package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Domain;

@Domain(valueType = int.class)
class JobType {

  private final int value;

  public JobType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
