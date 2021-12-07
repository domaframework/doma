package org.seasar.doma.it.domain;

import java.util.UUID;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class UUIDConverter implements DomainConverter<UUID, Object> {

  @Override
  public Object fromDomainToValue(UUID domain) {
    return domain;
  }

  @Override
  public UUID fromValueToDomain(Object value) {
    return (UUID) value;
  }
}
