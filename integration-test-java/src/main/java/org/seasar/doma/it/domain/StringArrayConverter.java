/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.it.domain;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
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
        Object[] objects = (Object[]) a.getArray();
        return Arrays.stream(objects).map(Object::toString).toArray(String[]::new);
      } catch (SQLException e) {
        return null;
      }
    }
    return null;
  }
}
