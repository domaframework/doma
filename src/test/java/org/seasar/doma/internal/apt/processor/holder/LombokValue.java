package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;
import org.seasar.doma.internal.apt.lombok.Value;

@Holder(valueType = String.class)
@Value
public class LombokValue {

  private final String value;

  public LombokValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
