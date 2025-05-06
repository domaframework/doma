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

import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * A class that resolves property names to entity property types for returning clauses.
 *
 * <p>This class implements {@link ReturningProperties} to provide a way to specify which entity
 * properties should be returned from database operations using property names. It supports including
 * specific properties and excluding others.
 */
public class ReturningPropertyNames implements ReturningProperties {

  /** The names of properties to include in the returning clause. */
  private final List<String> includedNames;
  
  /** The names of properties to exclude from the returning clause. */
  private final List<String> excludedNames;

  /**
   * Constructs a new {@code ReturningPropertyNames} with the specified included and excluded names.
   *
   * @param includedNames the names of properties to include
   * @param excludedNames the names of properties to exclude
   */
  private ReturningPropertyNames(List<String> includedNames, List<String> excludedNames) {
    this.includedNames = Objects.requireNonNull(includedNames);
    this.excludedNames = Objects.requireNonNull(excludedNames);
  }

  /** {@inheritDoc} */
  @Override
  public List<? extends EntityPropertyType<?, ?>> resolve(EntityType<?> entityType) {
    Objects.requireNonNull(entityType);
    var includedPropertyTypes = extractIncludedPropertyTypes(entityType);
    var excludedPropertyTypes = extractExcludedPropertyTypes(entityType);
    return includedPropertyTypes.stream()
        .filter(it -> !excludedPropertyTypes.contains(it))
        .toList();
  }

  /**
   * Extracts the included property types from the entity type.
   *
   * @param entityType the entity type
   * @return the list of included property types
   */
  private List<? extends EntityPropertyType<?, ?>> extractIncludedPropertyTypes(
      EntityType<?> entityType) {
    var list = includedNames.stream().map(entityType::getEntityPropertyType).toList();
    return list.isEmpty() ? entityType.getEntityPropertyTypes() : list;
  }

  /**
   * Extracts the excluded property types from the entity type.
   *
   * @param entityType the entity type
   * @return the list of excluded property types
   */
  private List<? extends EntityPropertyType<?, ?>> extractExcludedPropertyTypes(
      EntityType<?> entityType) {
    return excludedNames.stream().map(entityType::getEntityPropertyType).toList();
  }

  /**
   * Creates a new {@code ReturningProperties} instance with the specified included and excluded
   * names.
   *
   * @param includedNames the names of properties to include
   * @param excludedNames the names of properties to exclude
   * @return a new {@code ReturningProperties} instance
   */
  public static ReturningProperties of(List<String> includedNames, List<String> excludedNames) {
    if (includedNames.isEmpty() && excludedNames.isEmpty()) {
      return ALL;
    }
    return new ReturningPropertyNames(includedNames, excludedNames);
  }
}
