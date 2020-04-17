package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class VersionNoConverter implements DomainConverter<VersionNo, Integer> {

  @Override
  public Integer fromDomainToValue(VersionNo domain) {
    return null;
  }

  @Override
  public VersionNo fromValueToDomain(Integer value) {
    return null;
  }
}
