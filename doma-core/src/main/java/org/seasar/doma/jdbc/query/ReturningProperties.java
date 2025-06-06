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
package org.seasar.doma.jdbc.query;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * An interface that represents properties to be returned from database operations.
 *
 * <p>This interface is used to specify which entity properties should be returned from database
 * operations that support returning clauses, such as INSERT, UPDATE, or DELETE with RETURNING. It
 * provides a way to select specific properties or all properties to be included in the returning
 * clause.
 *
 * <p>The interface includes predefined instances for common cases:
 *
 * <ul>
 *   <li>{@link #NONE} - represents no properties to be returned
 *   <li>{@link #ALL} - represents all properties to be returned
 * </ul>
 *
 * <p>Custom implementations can be created to return specific subsets of properties.
 */
public interface ReturningProperties {

  /** A predefined instance of {@link ReturningProperties} that represents no properties. */
  ReturningProperties NONE = __ -> Collections.emptyList();

  /** A predefined instance of {@link ReturningProperties} that represents all properties. */
  ReturningProperties ALL =
      it -> {
        Objects.requireNonNull(it);
        return it.getEntityPropertyTypes();
      };

  /**
   * Checks if the current {@code ReturningProperties} instance represents no properties.
   *
   * @return {@code true} if this instance is equal to {@code NONE}, otherwise {@code false}
   */
  default boolean isNone() {
    return this == NONE;
  }

  /**
   * Checks if the current {@code ReturningProperties} instance represents all properties.
   *
   * @return {@code true} if this instance is equal to {@code ALL}, otherwise {@code false}
   */
  default boolean isAll() {
    return this == ALL;
  }

  /**
   * Resolves a list of entity property types for the given entity type.
   *
   * @param entityType the entity type for which to resolve property types
   * @return a list of entity property types associated with the given entity type
   */
  List<? extends EntityPropertyType<?, ?>> resolve(EntityType<?> entityType);
}
