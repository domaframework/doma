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

import java.util.Optional;
import org.seasar.doma.jdbc.type.JdbcType;

/** A hint about mapping. */
public interface JdbcMappingHint {

  /**
   * Returns the domain class if this object is mapped to the domain class.
   *
   * @return the domain class
   */
  Optional<Class<?>> getDomainClass();

  /**
   * Returns the JDBC type if the target value is mapped to the JDBC type.
   *
   * @return the JDBC type
   */
  default Optional<JdbcType<Object>> getJdbcType() {
    return Optional.empty();
  }
}
