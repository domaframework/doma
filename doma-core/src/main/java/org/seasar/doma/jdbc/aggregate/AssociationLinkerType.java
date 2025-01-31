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

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.seasar.doma.jdbc.entity.EntityType;

public class AssociationLinkerType<S, T> {

  private final String propertyPath;
  private final String tableAlias;
  private final EntityType<S> source;
  private final String sourceName;
  private final EntityType<T> target;
  private final String targetName;
  private final int depth;
  private final BiFunction<S, T, S> linker;

  private AssociationLinkerType(
      String propertyPath,
      String tableAlias,
      EntityType<S> source,
      String sourceName,
      EntityType<T> target,
      String targetName,
      int depth,
      BiFunction<S, T, S> linker) {
    this.propertyPath = Objects.requireNonNull(propertyPath);
    this.tableAlias = Objects.requireNonNull(tableAlias);
    this.source = Objects.requireNonNull(source);
    this.sourceName = Objects.requireNonNull(sourceName);
    this.target = Objects.requireNonNull(target);
    this.targetName = Objects.requireNonNull(targetName);
    this.depth = depth;
    this.linker = Objects.requireNonNull(linker);
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

  public String getSourceName() {
    return sourceName;
  }

  public EntityType<T> getTarget() {
    return target;
  }

  public String getTargetName() {
    return targetName;
  }

  public int getDepth() {
    return depth;
  }

  public BiFunction<S, T, S> getLinker() {
    return linker;
  }

  public static <S, T> AssociationLinkerType<S, T> of(
      String propertyPath,
      String tableAlias,
      EntityType<S> source,
      EntityType<T> target,
      BiFunction<S, T, S> function) {

    String sourceName;
    String[] segments = propertyPath.split("\\.");

    if (segments.length == 0) {
      throw new IllegalArgumentException("propertyPath");
    } else if (segments.length == 1) {
      sourceName = "";
    } else {
      sourceName = Arrays.stream(segments, 0, segments.length - 1).collect(Collectors.joining("."));
    }

    int depth = segments.length;

    return new AssociationLinkerType<>(
        propertyPath, tableAlias, source, sourceName, target, propertyPath, depth, function);
  }
}
