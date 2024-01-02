package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class ColorConverter implements DomainConverter<Color, String> {
  @Override
  public String fromDomainToValue(Color color) {
    return null;
  }

  @Override
  public Color fromValueToDomain(String value) {
    return null;
  }
}
