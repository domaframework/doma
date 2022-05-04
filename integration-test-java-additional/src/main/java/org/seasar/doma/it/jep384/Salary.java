package org.seasar.doma.it.jep384;

import java.math.BigDecimal;
import org.seasar.doma.DataType;

@DataType
public record Salary(BigDecimal value) {
  public static Salary of(String value) {
    return new Salary(new BigDecimal(value));
  }
}
