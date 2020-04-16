package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class NotPersistentValueObjectConverter
    implements DomainConverter<NotPersistentValueObject, StringBuilder> {

  @Override
  public StringBuilder fromDomainToValue(NotPersistentValueObject domain) {
    return null;
  }

  @Override
  public NotPersistentValueObject fromValueToDomain(StringBuilder value) {
    return null;
  }
}
