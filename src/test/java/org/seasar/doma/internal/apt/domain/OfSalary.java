package org.seasar.doma.internal.apt.domain;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

/** @author taedium */
@Domain(valueType = BigDecimal.class, factoryMethod = "of", acceptNull = true)
public class OfSalary {

  private final BigDecimal value;

  private OfSalary(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }

  public static OfSalary of(BigDecimal value) {
    return new OfSalary(value);
  }
}
