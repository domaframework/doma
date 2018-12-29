package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Domain;

/** @author taedium */
@Domain(valueType = Integer.class)
public class Identifier {

  private final Integer value;

  public Identifier(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
