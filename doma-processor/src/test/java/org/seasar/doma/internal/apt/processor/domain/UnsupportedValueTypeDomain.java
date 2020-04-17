package org.seasar.doma.internal.apt.processor.domain;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

@Domain(valueType = Override.class)
public class UnsupportedValueTypeDomain {

  private final BigDecimal value;

  public UnsupportedValueTypeDomain(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }
}
