package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.jdbc.domain.DomainConverter;

// @ExternalDomain
public class TuesdayConverter implements DomainConverter<Tuesday, String> {

  @Override
  public String fromDomainToValue(Tuesday domain) {
    return null;
  }

  @Override
  public Tuesday fromValueToDomain(String value) {
    return null;
  }
}
