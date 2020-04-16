package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.internal.apt.processor.domain.AbstractDomainConverter.ValueObject;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public abstract class AbstractDomainConverter implements DomainConverter<ValueObject, String> {

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
