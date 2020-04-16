package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

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
