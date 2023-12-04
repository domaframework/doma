package org.seasar.doma.it.criteria;

import java.math.BigDecimal;
import java.util.Objects;
import org.seasar.doma.Entity;
import org.seasar.doma.Metamodel;

@Entity(metamodel = @Metamodel)
public class NameAndAmount {
  private String name;
  private Integer amount;

  public NameAndAmount() {}

  public NameAndAmount(String accounting, BigDecimal bigDecimal) {
    this.name = accounting;
    this.amount = bigDecimal.intValue();
  }

  public String getname() {
    return name;
  }

  public void setname(String name) {
    this.name = name;
  }

  public Integer getamount() {
    return amount;
  }

  public void setamount(Integer amount) {
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NameAndAmount that = (NameAndAmount) o;
    return Objects.equals(name, that.name) && Objects.equals(amount, that.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, amount);
  }
}
