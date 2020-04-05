package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.Domain;

@Domain(valueType = Integer.class)
public class IntersectionParameterizedDomain<T extends Number & Runnable> {

  private final Integer value;

  public IntersectionParameterizedDomain(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
