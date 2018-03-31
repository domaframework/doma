package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;

@Holder(valueType = String.class, acceptNull = false)
public class NullRejectionHolder {

  private final String value;

  public NullRejectionHolder(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
