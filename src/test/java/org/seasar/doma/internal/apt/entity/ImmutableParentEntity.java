package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;

@Entity(immutable = true)
public class ImmutableParentEntity {

  final Integer aaa;

  final Integer bbb;

  public ImmutableParentEntity(Integer aaa, Integer bbb) {
    super();
    this.aaa = aaa;
    this.bbb = bbb;
  }

  public Integer getAaa() {
    return aaa;
  }

  public Integer getBbb() {
    return bbb;
  }
}
