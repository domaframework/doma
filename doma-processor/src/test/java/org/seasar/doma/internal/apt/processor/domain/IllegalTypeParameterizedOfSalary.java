package org.seasar.doma.internal.apt.processor.domain;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

@Domain(valueType = BigDecimal.class, factoryMethod = "of")
public class IllegalTypeParameterizedOfSalary<T, U> {

  private final BigDecimal value;

  private IllegalTypeParameterizedOfSalary(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }

  public static <T, U extends Comparable<T>> IllegalTypeParameterizedOfSalary<T, U> of(
      BigDecimal value) {
    return new IllegalTypeParameterizedOfSalary<>(value);
  }
}
