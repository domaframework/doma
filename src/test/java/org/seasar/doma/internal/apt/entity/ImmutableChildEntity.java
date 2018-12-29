package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;

@Entity
public class ImmutableChildEntity extends ImmutableParentEntity {

  final String ccc;

  public ImmutableChildEntity(Integer aaa, Integer bbb, String ccc) {
    super(aaa, bbb);
    this.ccc = ccc;
  }
}
