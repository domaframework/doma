package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

/** @author taedium */
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
