package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.domain.AbstractDomainType;
import org.seasar.doma.wrapper.StringWrapper;

public class _Name extends AbstractDomainType<String, Name> {

  private _Name() {
    super(StringWrapper::new);
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
