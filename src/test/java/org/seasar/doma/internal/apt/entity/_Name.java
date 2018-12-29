package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.jdbc.domain.AbstractDomainType;

public class _Name extends AbstractDomainType<String, Name> {

  private _Name() {
    super(() -> new org.seasar.doma.wrapper.StringWrapper());
  }

  @Override
  public Name newDomain(String value) {
    return null;
  }

  @Override
  public String getBasicValue(Name domain) {
    return null;
  }

  @Override
  public Class<String> getBasicClass() {
    return null;
  }

  @Override
  public Class<Name> getDomainClass() {
    return null;
  }

  public static _Name getSingletonInternal() {
    return null;
  }
}
