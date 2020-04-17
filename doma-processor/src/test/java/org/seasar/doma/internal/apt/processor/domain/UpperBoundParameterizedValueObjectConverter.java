package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class UpperBoundParameterizedValueObjectConverter
    implements DomainConverter<UpperBoundParameterizedValueObject<?>, String> {

  @Override
  public String fromDomainToValue(UpperBoundParameterizedValueObject<?> domain) {
    return null;
  }

  @Override
  public UpperBoundParameterizedValueObject<?> fromValueToDomain(String value) {
    return null;
  }
}
