package org.seasar.doma.internal.apt.processor.domain;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

public class Outer_nonStaticInner {

  @SuppressWarnings("InnerClassMayBeStatic")
  @Domain(valueType = BigDecimal.class)
  public class Inner {

    private final BigDecimal value;

    public Inner(BigDecimal value) {
      this.value = value;
    }

    public BigDecimal getValue() {
      return value;
    }
  }
}
