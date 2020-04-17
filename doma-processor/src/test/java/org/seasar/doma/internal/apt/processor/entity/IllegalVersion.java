package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Domain;

@Domain(valueType = String.class)
public class IllegalVersion {

  private final String value;

  public IllegalVersion(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
