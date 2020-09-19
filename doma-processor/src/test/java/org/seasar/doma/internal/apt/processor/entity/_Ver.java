package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.domain.AbstractDomainType;
import org.seasar.doma.wrapper.IntegerWrapper;

public class _Ver extends AbstractDomainType<Integer, Ver> {

  private _Ver() {
    super(IntegerWrapper::new);
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
