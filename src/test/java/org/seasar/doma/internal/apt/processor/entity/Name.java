package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Holder;

@Holder(valueType = String.class)
public class Name {
  private final String value;

  public Name(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
