package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.OriginalStates;

@Entity
public class OriginalStatesParentEntity {

  Integer aaa;

  Integer bbb;

  @OriginalStates OriginalStatesParentEntity states;

  public Integer getAaa() {
    return aaa;
  }

  public void setAaa(Integer aaa) {
    this.aaa = aaa;
  }
}
