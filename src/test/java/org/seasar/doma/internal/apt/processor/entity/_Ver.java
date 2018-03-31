package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.holder.AbstractHolderDesc;
import org.seasar.doma.wrapper.IntegerWrapper;

public class _Ver extends AbstractHolderDesc<Integer, Ver> {

  private _Ver() {
    super(IntegerWrapper::new);
  }

  @Override
  public Ver newHolder(Integer value) {
    return null;
  }

  @Override
  public Integer getBasicValue(Ver holder) {
    return null;
  }

  @Override
  public Class<Integer> getBasicClass() {
    return null;
  }

  @Override
  public Class<Ver> getHolderClass() {
    return null;
  }

  public static _Ver getSingletonInternal() {
    return null;
  }
}
