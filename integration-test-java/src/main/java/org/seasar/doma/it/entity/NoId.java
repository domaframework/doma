package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Table;

@Entity
@Table(name = "NO_ID")
public class NoId {

  Integer value1;

  Integer value2;

  @OriginalStates NoId originalStates;

  public Integer getValue1() {
    return value1;
  }

  public void setValue1(Integer value1) {
    this.value1 = value1;
  }

  public Integer getValue2() {
    return value2;
  }

  public void setValue2(Integer value2) {
    this.value2 = value2;
  }
}
