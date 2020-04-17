package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

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
