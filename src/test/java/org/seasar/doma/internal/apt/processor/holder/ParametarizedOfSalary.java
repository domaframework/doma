package org.seasar.doma.internal.apt.processor.holder;

import java.math.BigDecimal;
import org.seasar.doma.Holder;

@Holder(valueType = BigDecimal.class, factoryMethod = "of", acceptNull = true)
public class ParametarizedOfSalary<T, U> {

  private final BigDecimal value;

  private ParametarizedOfSalary(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }

  public static <T, U> ParametarizedOfSalary<T, U> of(BigDecimal value) {
    return new ParametarizedOfSalary<T, U>(value);
  }
}
