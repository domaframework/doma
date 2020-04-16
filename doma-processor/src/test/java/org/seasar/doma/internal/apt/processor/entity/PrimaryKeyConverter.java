package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class PrimaryKeyConverter implements DomainConverter<PrimaryKey, Integer> {

  @Override
  public Integer fromDomainToValue(PrimaryKey domain) {
    return null;
  }

  @Override
  public PrimaryKey fromValueToDomain(Integer value) {
    return null;
  }
}
