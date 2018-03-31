package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.holder.AbstractHolderDesc;
import org.seasar.doma.wrapper.IntegerWrapper;

public class _Weight<T> extends AbstractHolderDesc<Integer, Weight<T>> {

  private _Weight() {
    super(IntegerWrapper::new);
  }

  @Override
  public Weight<T> newHolder(Integer value) {
    return null;
  }

  @Override
  public Integer getBasicValue(Weight<T> holder) {
    return null;
  }

  @Override
  public Class<Integer> getBasicClass() {
    return null;
  }

  @Override
  public Class<Weight<T>> getHolderClass() {
    return null;
  }

  public static <T> _Weight<T> getSingletonInternal() {
    return null;
  }
}
