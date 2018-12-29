package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.Domain;

@Domain(valueType = String.class, acceptNull = false)
public class NullRejectionDomain {

  private final String value;

  public NullRejectionDomain(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
