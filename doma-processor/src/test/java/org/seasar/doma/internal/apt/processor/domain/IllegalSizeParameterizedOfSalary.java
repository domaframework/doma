package org.seasar.doma.internal.apt.processor.domain;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

@Domain(valueType = BigDecimal.class, factoryMethod = "of")
public class IllegalSizeParameterizedOfSalary<T, U> {

  private final BigDecimal value;

  private IllegalSizeParameterizedOfSalary(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }

  public static <T, U, V> IllegalSizeParameterizedOfSalary<T, U> of(BigDecimal value) {
    return new IllegalSizeParameterizedOfSalary<>(value);
  }
}
