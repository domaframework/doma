package org.seasar.doma.internal.apt.processor.holder;

import java.math.BigDecimal;
import org.seasar.doma.Holder;

public class InterfaceOuter {

  @Holder(valueType = BigDecimal.class, factoryMethod = "of")
  public interface Inner {

    default BigDecimal getValue() {
      return BigDecimal.ZERO;
    }

    static Inner of(BigDecimal value) {
      return new Inner() {};
    }
  }
}
