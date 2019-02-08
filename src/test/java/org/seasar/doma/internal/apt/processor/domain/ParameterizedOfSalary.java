package org.seasar.doma.internal.apt.processor.domain;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

@Domain(valueType = BigDecimal.class, factoryMethod = "of", acceptNull = true)
public class ParameterizedOfSalary<T, U> {

  private final BigDecimal value;

  private ParameterizedOfSalary(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }

  public static <T, U> ParameterizedOfSalary<T, U> of(BigDecimal value) {
    return new ParameterizedOfSalary<T, U>(value);
  }
}
