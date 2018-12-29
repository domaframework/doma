package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;
import org.seasar.doma.internal.apt.lombok.Value;

@Domain(valueType = String.class)
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
