package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;

@Domain(valueType = String.class)
public class Name {
  private final String value;

  public Name(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
