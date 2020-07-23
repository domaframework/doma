package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.domain.AbstractDomainType;

public class _Identifier extends AbstractDomainType<Integer, Identifier> {

  private _Identifier() {
    super(() -> new org.seasar.doma.wrapper.IntegerWrapper());
  }

  @Override
  public Identifier newDomain(Integer value) {
    return null;
  }

  @Override
  public Integer getBasicValue(Identifier domain) {
    return null;
  }

  @Override
  public Class<Integer> getBasicClass() {
    return null;
  }

  @Override
  public Class<Identifier> getDomainClass() {
    return null;
  }

  public static _Identifier getSingletonInternal() {
    return null;
  }
}
