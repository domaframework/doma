package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class StringArrayConverter implements DomainConverter<String[], Object> {

  @Override
  public Object fromDomainToValue(String[] domain) {
    return null;
  }

  @Override
  public String[] fromValueToDomain(Object value) {
    return null;
  }
}
