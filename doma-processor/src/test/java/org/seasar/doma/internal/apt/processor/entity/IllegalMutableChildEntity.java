package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;

@SuppressWarnings("DefaultAnnotationParam")
@Entity(immutable = false)
public class IllegalMutableChildEntity extends ImmutableParentEntity {

  final String ccc;

  public IllegalMutableChildEntity(Integer aaa, Integer bbb, String ccc) {
    super(aaa, bbb);
    this.ccc = ccc;
  }
}
