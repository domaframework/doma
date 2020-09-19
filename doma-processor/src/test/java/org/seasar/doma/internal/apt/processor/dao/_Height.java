package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.jdbc.domain.AbstractDomainType;
import org.seasar.doma.wrapper.IntegerWrapper;

public class _Height<T> extends AbstractDomainType<Integer, Height<T>> {

  private _Height() {
    super(IntegerWrapper::new);
  }

  @Override
  public Height<T> newDomain(Integer value) {
    return null;
  }

  @Override
  public Integer getBasicValue(Height<T> domain) {
    return null;
  }

  public Class<Integer> getBasicClass() {
    return Integer.class;
  }

  @SuppressWarnings("unchecked")
  public Class<Height<T>> getDomainClass() {
    Class<?> clazz = Height.class;
    return (Class<Height<T>>) clazz;
  }

  public static <T> _Height<T> getSingletonInternal() {
    return null;
  }
}
