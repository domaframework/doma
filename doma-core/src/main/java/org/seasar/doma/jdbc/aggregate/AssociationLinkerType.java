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
package org.seasar.doma.jdbc.aggregate;

import java.util.Objects;
import java.util.function.BiFunction;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * Represents an association linker that connects two entity types based on a property path.
 *
 * @param <S> the source entity type
 * @param <T> the target entity type
 */
public class AssociationLinkerType<S, T> {

  private final String propertyPath;
  private final String tableAlias;
  private final EntityType<S> source;
  private final String ancestorPath;
  private final EntityType<T> target;
  private final int propertyPathDepth;
  private final BiFunction<S, T, S> linker;

  private AssociationLinkerType(
      String ancestorPath,
      String propertyPath,
      int propertyPathDepth,
      String tableAlias,
      EntityType<S> source,
      EntityType<T> target,
      BiFunction<S, T, S> linker) {
    this.ancestorPath = ancestorPath;
    this.propertyPath = propertyPath;
    this.propertyPathDepth = propertyPathDepth;
    this.tableAlias = tableAlias;
    this.source = source;
    this.target = target;
    this.linker = linker;
  }

  public String getPropertyPath() {
    return propertyPath;
  }

  public String getTableAlias() {
    return tableAlias;
  }

  public EntityType<S> getSource() {
    return source;
  }

  public String getAncestorPath() {
    return ancestorPath;
  }

  public EntityType<T> getTarget() {
    return target;
  }

  public int getPropertyPathDepth() {
    return propertyPathDepth;
  }

  public BiFunction<S, T, S> getLinker() {
    return linker;
  }

  public static <S, T> AssociationLinkerType<S, T> of(
      String ancestorPath,
      String propertyPath,
      int propertyPathDepth,
      String tableAlias,
      EntityType<S> source,
      EntityType<T> target,
      BiFunction<S, T, S> linker) {
    Objects.requireNonNull(ancestorPath);
    Objects.requireNonNull(propertyPath);
    Objects.requireNonNull(tableAlias);
    Objects.requireNonNull(source);
    Objects.requireNonNull(target);
    Objects.requireNonNull(linker);
    return new AssociationLinkerType<>(
        ancestorPath, propertyPath, propertyPathDepth, tableAlias, source, target, linker);
  }
}
