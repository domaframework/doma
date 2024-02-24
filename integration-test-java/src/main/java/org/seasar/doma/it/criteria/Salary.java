package org.seasar.doma.it.criteria;

import java.math.BigDecimal;
import org.seasar.doma.Domain;

@Domain(valueType = BigDecimal.class)
public class Salary {

  private final BigDecimal value;

  public Salary(BigDecimal value) {
    this.value = value;
  }

  public Salary(String value) {
    this(new BigDecimal(value));
  }

  public BigDecimal getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
