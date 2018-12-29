package org.seasar.doma.internal.apt.entity;

import java.math.BigDecimal;
import org.seasar.doma.Entity;

@Entity
public class Child3InheritingEntity extends Parent3Entity {

  BigDecimal bbb;

  String ccc;

  public BigDecimal getBbb() {
    return bbb;
  }

  public void setBbb(BigDecimal bbb) {
    this.bbb = bbb;
  }

  public String getCcc() {
    return ccc;
  }

  public void setCcc(String ccc) {
    this.ccc = ccc;
  }
}
