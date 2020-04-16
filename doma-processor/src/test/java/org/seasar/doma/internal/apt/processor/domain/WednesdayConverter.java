package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class WednesdayConverter implements DomainConverter<Wednesday, String> {

  @Override
  public String fromDomainToValue(Wednesday domain) {
    return null;
  }

  @Override
  public Wednesday fromValueToDomain(String value) {
    return null;
  }
}
