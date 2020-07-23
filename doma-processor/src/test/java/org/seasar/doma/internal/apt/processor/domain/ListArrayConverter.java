package org.seasar.doma.internal.apt.processor.domain;

import java.util.List;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class ListArrayConverter implements DomainConverter<List<?>[], Object> {

  @Override
  public Object fromDomainToValue(List<?>[] domain) {
    return null;
  }

  @Override
  public List<?>[] fromValueToDomain(Object value) {
    return null;
  }
}
