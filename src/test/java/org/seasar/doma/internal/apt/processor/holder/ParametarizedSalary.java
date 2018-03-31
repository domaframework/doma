package org.seasar.doma.internal.apt.processor.holder;

import java.math.BigDecimal;
import org.seasar.doma.Holder;

@Holder(valueType = BigDecimal.class, acceptNull = true)
public class ParametarizedSalary<T, U> {

  private final BigDecimal value;

  public ParametarizedSalary(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }
}
