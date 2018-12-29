package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

/** @author taedium */
@ExternalDomain
public class ParameterizedValueObjectConverter
    implements DomainConverter<ParameterizedValueObject<?, ?>, String> {

  @Override
  public String fromDomainToValue(ParameterizedValueObject<?, ?> domain) {
    return null;
  }

  @Override
  public ParameterizedValueObject<?, ?> fromValueToDomain(String value) {
    return null;
  }
}
