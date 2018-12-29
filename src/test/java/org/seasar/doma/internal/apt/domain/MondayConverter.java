package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

/** @author taedium */
@ExternalDomain
public class MondayConverter implements DomainConverter<Monday, String> {

  @Override
  public String fromDomainToValue(Monday domain) {
    return null;
  }

  @Override
  public Monday fromValueToDomain(String value) {
    return null;
  }
}
