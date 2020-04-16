package org.seasar.doma.internal.apt.processor.domain;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

public class Outer {

  @Domain(valueType = BigDecimal.class)
  public static class Inner {

    private final BigDecimal value;

    public Inner(BigDecimal value) {
      this.value = value;
    }

    public BigDecimal getValue() {
      return value;
    }
  }
}
