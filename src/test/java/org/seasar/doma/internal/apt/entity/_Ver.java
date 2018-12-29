package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.jdbc.domain.AbstractDomainType;

public class _Ver extends AbstractDomainType<Integer, Ver> {

  private _Ver() {
    super(() -> new org.seasar.doma.wrapper.IntegerWrapper());
  }

  @Override
  public Ver newDomain(Integer value) {
    return null;
  }

  @Override
  public Integer getBasicValue(Ver domain) {
    return null;
  }

  @Override
  public Class<Integer> getBasicClass() {
    return null;
  }

  @Override
  public Class<Ver> getDomainClass() {
    return null;
  }

  public static _Ver getSingletonInternal() {
    return null;
  }
}
