package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Holder;

/** @author taedium */
@Holder(valueType = String.class)
public class IllegalId {

  private final String value;

  public IllegalId(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
