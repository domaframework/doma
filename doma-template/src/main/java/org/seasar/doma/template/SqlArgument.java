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
package org.seasar.doma.template;

import java.util.Objects;

/** Represents a SQL argument. */
public class SqlArgument {
  private final Class<?> type;
  private final Object value;

  /**
   * @param type the variable type. Must not be null.
   * @param value the value. Can be null.
   */
  public SqlArgument(Class<?> type, Object value) {
    this.type = Objects.requireNonNull(type);
    this.value = value;
  }

  /**
   * Returns the value type.
   *
   * @return the value type. Must not be null.
   */
  public Class<?> getType() {
    return type;
  }

  /**
   * Returns the value.
   *
   * @return the value. Can be null.
   */
  public Object getValue() {
    return value;
  }
}
