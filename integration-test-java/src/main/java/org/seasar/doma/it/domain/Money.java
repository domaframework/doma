package org.seasar.doma.it.domain;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

@Domain(valueType = BigDecimal.class, acceptNull = true)
public class Money {

  private final BigDecimal value;

  public Money(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }
}
