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
package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.MapKeyNamingType;

/**
 * A naming convention controller for the keys contained in a {@code Map<String, Object>} object.
 */
public interface MapKeyNaming {

  /**
   * Applies the naming convention.
   *
   * @param method the DAO method or {@code null} if the SQL is built with the query builder
   * @param mapKeyNamingType the naming convention
   * @param text the text
   * @return the converted text
   * @throws DomaNullPointerException if {@code mapKeyNamingType} or {@code text} is {@code null}
   */
  default String apply(Method method, MapKeyNamingType mapKeyNamingType, String text) {
    if (mapKeyNamingType == null) {
      throw new DomaNullPointerException("mapKeyNamingType");
    }
    if (text == null) {
      throw new DomaNullPointerException("text");
    }
    return mapKeyNamingType.apply(text);
  }
}
