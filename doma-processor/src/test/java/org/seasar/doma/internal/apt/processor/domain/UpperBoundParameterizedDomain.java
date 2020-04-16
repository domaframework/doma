package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.Domain;

@Domain(valueType = Integer.class)
public class UpperBoundParameterizedDomain<T extends Number> {

  private final Integer value;

  public UpperBoundParameterizedDomain(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
