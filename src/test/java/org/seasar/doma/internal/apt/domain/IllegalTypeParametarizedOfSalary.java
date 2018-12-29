package org.seasar.doma.internal.apt.domain;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

@Domain(valueType = BigDecimal.class, factoryMethod = "of")
public class IllegalTypeParametarizedOfSalary<T, U> {

  private final BigDecimal value;

  private IllegalTypeParametarizedOfSalary(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }

  public static <T, U extends Comparable<T>> IllegalTypeParametarizedOfSalary<T, U> of(
      BigDecimal value) {
    return new IllegalTypeParametarizedOfSalary<T, U>(value);
  }
}
