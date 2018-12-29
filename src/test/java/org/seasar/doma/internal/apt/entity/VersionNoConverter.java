package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

/** @author taedium */
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
