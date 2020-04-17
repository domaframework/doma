package org.seasar.doma.internal.apt.processor.domain;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

public class InterfaceOuter {

  @Domain(valueType = BigDecimal.class, factoryMethod = "of")
  public interface Inner {

    default BigDecimal getValue() {
      return BigDecimal.ZERO;
    }

    static Inner of(BigDecimal value) {
      return new Inner() {};
    }
  }
}
