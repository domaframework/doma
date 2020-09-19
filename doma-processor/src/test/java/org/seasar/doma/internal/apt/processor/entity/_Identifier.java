package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.domain.AbstractDomainType;
import org.seasar.doma.wrapper.IntegerWrapper;

public class _Identifier extends AbstractDomainType<Integer, Identifier> {

  private _Identifier() {
    super(IntegerWrapper::new);
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
