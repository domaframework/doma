package org.seasar.doma.internal.apt.processor.embeddabledesc;

import org.seasar.doma.jdbc.domain.AbstractDomainType;

public class _City extends AbstractDomainType<String, City> {

  private _City() {
    super(() -> new org.seasar.doma.wrapper.StringWrapper());
  }

  @Override
  public City newDomain(String value) {
    return null;
  }

  @Override
  public String getBasicValue(City domain) {
    return null;
  }

  @Override
  public Class<String> getBasicClass() {
    return null;
  }

  @Override
  public Class<City> getDomainClass() {
    return null;
  }

  public static _City getSingletonInternal() {
    return null;
  }
}
