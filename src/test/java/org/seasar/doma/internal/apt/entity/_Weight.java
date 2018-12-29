package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.jdbc.domain.AbstractDomainType;

/** @author taedium */
public class _Weight<T> extends AbstractDomainType<Integer, Weight<T>> {

  private _Weight() {
    super(() -> new org.seasar.doma.wrapper.IntegerWrapper());
  }

  @Override
  public Weight<T> newDomain(Integer value) {
    return null;
  }

  @Override
  public Integer getBasicValue(Weight<T> domain) {
    return null;
  }

  @Override
  public Class<Integer> getBasicClass() {
    return null;
  }

  @Override
  public Class<Weight<T>> getDomainClass() {
    return null;
  }

  public static <T> _Weight<T> getSingletonInternal() {
    return null;
  }
}
