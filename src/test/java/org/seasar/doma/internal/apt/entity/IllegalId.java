package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Domain;

@Domain(valueType = String.class)
public class IllegalId {

  private final String value;

  public IllegalId(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
