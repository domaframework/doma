package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.holder.AbstractHolderDesc;

public class _Identifier extends AbstractHolderDesc<Integer, Identifier> {

  private _Identifier() {
    super(() -> new org.seasar.doma.wrapper.IntegerWrapper());
  }

  @Override
  public Identifier newHolder(Integer value) {
    return null;
  }

  @Override
  public Integer getBasicValue(Identifier holder) {
    return null;
  }

  @Override
  public Class<Integer> getBasicClass() {
    return null;
  }

  @Override
  public Class<Identifier> getHolderClass() {
    return null;
  }

  public static _Identifier getSingletonInternal() {
    return null;
  }
}
