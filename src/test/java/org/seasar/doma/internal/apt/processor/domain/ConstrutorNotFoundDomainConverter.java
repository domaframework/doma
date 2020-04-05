package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.internal.apt.processor.domain.ConstrutorNotFoundDomainConverter.ValueObject;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class ConstrutorNotFoundDomainConverter implements DomainConverter<ValueObject, String> {

  private ConstrutorNotFoundDomainConverter() {}

  @Override
  public String fromDomainToValue(ValueObject domain) {
    return null;
  }

  @Override
  public ValueObject fromValueToDomain(String value) {
    return null;
  }

  static class ValueObject {}
}
