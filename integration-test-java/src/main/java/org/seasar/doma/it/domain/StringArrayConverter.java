package org.seasar.doma.it.domain;

import java.sql.Array;
import java.sql.SQLException;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class StringArrayConverter implements DomainConverter<String[], Object> {

  @Override
  public Object fromDomainToValue(String[] domain) {
    return domain;
  }

  @Override
  public String[] fromValueToDomain(Object value) {

    if (value instanceof String[]) {
      return (String[]) value;
    }
    if (value instanceof Array) {
      Array a = (Array) value;
      try {
        return (String[]) a.getArray();
      } catch (SQLException e) {
        return null;
      }
    }
    return null;
  }
}
