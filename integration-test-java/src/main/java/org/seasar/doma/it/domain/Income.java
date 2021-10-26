package org.seasar.doma.it.domain;

import org.seasar.doma.Domain;

@Domain(valueType = double.class)
public class Income {
  private final double value;

  public Income(double value) {
    this.value = value;
  }

  public double getValue() {
    return value;
  }
}
