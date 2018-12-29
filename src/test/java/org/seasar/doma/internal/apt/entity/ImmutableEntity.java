package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;

@Entity(immutable = true)
public class ImmutableEntity {

  private final String aaa;

  private final int bbb;

  private final Integer ccc;

  public ImmutableEntity(String aaa, int bbb, Integer ccc) {
    this.aaa = aaa;
    this.bbb = bbb;
    this.ccc = ccc;
  }

  public String getAaa() {
    return aaa;
  }

  public int getBbb() {
    return bbb;
  }

  public Integer getCcc() {
    return ccc;
  }
}
