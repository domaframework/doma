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
package org.seasar.doma.jdbc.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the metadata for an embedded property within an entity.
 *
 * <p>This record encapsulates the configuration information for properties annotated with {@link
 * org.seasar.doma.Embedded}, including column name prefixes and column overrides. It is used
 * internally by the Doma framework to manage how embeddable objects are mapped to database columns
 * when embedded in entities.
 *
 * <p>The embedded type information is derived from the {@link org.seasar.doma.Embedded} annotation
 * and its attributes:
 *
 * <ul>
 *   <li>The prefix is applied to all column names of the embedded properties unless overridden
 *   <li>Column overrides provide fine-grained control over individual property mappings
 * </ul>
 *
 * <p>This record is typically created during annotation processing and used at runtime for SQL
 * generation and result set mapping.
 *
 * @param prefix the column name prefix to be prepended to embedded property columns
 * @param columnTypeMap a map of property names to their column type overrides
 * @see org.seasar.doma.Embedded
 * @see org.seasar.doma.ColumnOverride
 * @see EmbeddableType
 */
public record EmbeddedType(String prefix, Map<String, ColumnType> columnTypeMap) {
  /**
   * Creates a new EmbeddedType instance.
   *
   * @param prefix the column name prefix (must not be null, can be empty string)
   * @param columnTypeMap the map of column overrides (must not be null, can be empty)
   * @throws NullPointerException if prefix or columnTypeMap is null
   */
  public EmbeddedType {
    Objects.requireNonNull(prefix);
    Objects.requireNonNull(columnTypeMap);
  }

  /**
   * Merges this EmbeddedType with a parent EmbeddedType to create a new EmbeddedType instance.
   *
   * <p>This method is used to handle nested embeddable objects. When an embeddable object contains
   * another embeddable object, this method combines the configuration from both the parent and
   * child embedded types.
   *
   * <p>The merging process combines:
   *
   * <ul>
   *   <li>Prefixes are concatenated (parent prefix + this prefix)
   *   <li>Column type maps are merged with parent mappings taking precedence over child mappings
   * </ul>
   *
   * @param parent the parent EmbeddedType to merge with, can be null
   * @return a new EmbeddedType with merged configuration, or this instance if parent is null
   */
  public EmbeddedType merge(EmbeddedType parent) {
    if (parent == null) {
      return this;
    }
    var prefix = parent.prefix + this.prefix;
    var columnTypeMap = new HashMap<>(this.columnTypeMap);
    columnTypeMap.putAll(parent.columnTypeMap);
    return new EmbeddedType(prefix, Collections.unmodifiableMap(columnTypeMap));
  }
}
