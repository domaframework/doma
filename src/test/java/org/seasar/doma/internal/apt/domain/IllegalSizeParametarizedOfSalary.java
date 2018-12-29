package org.seasar.doma.internal.apt.domain;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

/** @author taedium */
@Domain(valueType = BigDecimal.class, factoryMethod = "of")
public class IllegalSizeParametarizedOfSalary<T, U> {

  private final BigDecimal value;

  private IllegalSizeParametarizedOfSalary(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }

  public static <T, U, V> IllegalSizeParametarizedOfSalary<T, U> of(BigDecimal value) {
    return new IllegalSizeParametarizedOfSalary<T, U>(value);
  }
}
