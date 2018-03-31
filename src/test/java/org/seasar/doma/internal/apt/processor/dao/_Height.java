package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.jdbc.holder.AbstractHolderDesc;

public class _Height<T> extends AbstractHolderDesc<Integer, Height<T>> {

  private _Height() {
    super(() -> new org.seasar.doma.wrapper.IntegerWrapper());
  }

  @Override
  public Height<T> newHolder(Integer value) {
    return null;
  }

  @Override
  public Integer getBasicValue(Height<T> holder) {
    return null;
  }

  public Class<Integer> getBasicClass() {
    return Integer.class;
  }

  @SuppressWarnings("unchecked")
  public Class<Height<T>> getHolderClass() {
    Class<?> clazz = Height.class;
    return (Class<Height<T>>) clazz;
  }

  public static <T> _Height<T> getSingletonInternal() {
    return null;
  }
}
