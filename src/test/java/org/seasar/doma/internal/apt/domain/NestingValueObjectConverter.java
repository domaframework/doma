package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.internal.apt.domain.NestingValueObjectConverter.NestingValueObject;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class NestingValueObjectConverter implements DomainConverter<NestingValueObject, String> {

  public static class NestingValueObject {}

  @Override
  public String fromDomainToValue(NestingValueObject domain) {
    return null;
  }

  @Override
  public NestingValueObject fromValueToDomain(String value) {
    return null;
  }
}
