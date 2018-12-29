package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Domain;

/** @author taedium */
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
