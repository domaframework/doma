package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.domain.AbstractDomainType;
import org.seasar.doma.wrapper.StringWrapper;

public class _Names extends AbstractDomainType<String, Names> {

  private _Names() {
    super(StringWrapper::new);
  }

  @Override
  public Names newDomain(String value) {
    return null;
  }

  @Override
  public String getBasicValue(Names domain) {
    return null;
  }

  @Override
  public Class<String> getBasicClass() {
    return null;
  }

  @Override
  public Class<Names> getDomainClass() {
    return null;
  }

  public static _Names getSingletonInternal() {
    return null;
  }
}
