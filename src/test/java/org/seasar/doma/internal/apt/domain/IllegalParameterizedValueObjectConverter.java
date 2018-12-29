package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class IllegalParameterizedValueObjectConverter
    implements DomainConverter<ParameterizedValueObject<?, String>, String> {

  @Override
  public String fromDomainToValue(ParameterizedValueObject<?, String> domain) {
    return null;
  }

  @Override
  public ParameterizedValueObject<?, String> fromValueToDomain(String value) {
    return null;
  }
}
