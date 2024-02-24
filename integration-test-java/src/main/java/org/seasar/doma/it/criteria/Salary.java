package org.seasar.doma.it.criteria;

import java.math.BigDecimal;
import java.util.Objects;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Salary salary = (Salary) o;
    return Objects.equals(value, salary.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
