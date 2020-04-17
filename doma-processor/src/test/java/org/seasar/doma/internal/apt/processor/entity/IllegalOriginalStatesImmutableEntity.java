package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.OriginalStates;

@Entity(immutable = true)
public class IllegalOriginalStatesImmutableEntity {

  private final String aaa;

  private final int bbb;

  private final Integer ccc;

  @OriginalStates private IllegalOriginalStatesImmutableEntity states;

  public IllegalOriginalStatesImmutableEntity(String aaa, int bbb, Integer ccc) {
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
