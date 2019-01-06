package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.internal.apt.processor.entity.ValueObjectConverter.ValueObject;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class ValueObjectConverter implements DomainConverter<ValueObject, String> {

  @Override
  public String fromDomainToValue(ValueObject domain) {
    return null;
  }

  @Override
  public ValueObject fromValueToDomain(String value) {
    return null;
  }

  public static class ValueObject {}
}
