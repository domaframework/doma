package org.seasar.doma.internal.apt.processor.entity;

import java.math.BigDecimal;
import org.seasar.doma.Entity;

/** @author taedium */
@Entity
public class ChildEntity extends ParentEntity {

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
