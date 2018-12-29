package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.internal.apt.domain.AbstractDomainConverter.ValueObject;
import org.seasar.doma.jdbc.domain.DomainConverter;

/** @author taedium */
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
