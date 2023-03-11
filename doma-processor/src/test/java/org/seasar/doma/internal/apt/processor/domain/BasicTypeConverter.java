package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class BasicTypeConverter implements DomainConverter<Boolean, Integer> {

  @Override
  public Integer fromDomainToValue(Boolean domain) {
    return null;
  }

  @Override
  public Boolean fromValueToDomain(Integer value) {
    return null;
  }
}
