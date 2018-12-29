package org.seasar.doma.internal.apt.domain;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

/** @author nakamura-to */
public class Outer_nonPublicInner {

  @Domain(valueType = BigDecimal.class)
  static class Inner {

    private final BigDecimal value;

    public Inner(BigDecimal value) {
      this.value = value;
    }

    public BigDecimal getValue() {
      return value;
    }
  }
}
