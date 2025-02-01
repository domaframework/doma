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

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.entity.EntityType;

public abstract class AbstractAggregateStrategyType implements AggregateStrategyType {

  private final EntityType<?> root;
  private final String tableAlias;
  private final List<AssociationLinkerType<?, ?>> associationLinkerTypes;

  protected AbstractAggregateStrategyType(
      EntityType<?> root,
      String tableAlias,
      List<AssociationLinkerType<?, ?>> associationLinkerTypes) {
    Objects.requireNonNull(tableAlias);
    this.root = Objects.requireNonNull(root);
    this.tableAlias = Objects.requireNonNull(tableAlias);
    Objects.requireNonNull(associationLinkerTypes);
    this.associationLinkerTypes = sortAssociationLinkerTypes(associationLinkerTypes);
  }

  /**
   * Sorts a list of {@code AssociationLinkerType} objects in descending order based on their depth.
   *
   * @param associationLinkerTypes the list of {@code AssociationLinkerType} objects to be sorted
   * @return a sorted list of {@code AssociationLinkerType} objects in descending order of their
   *     depth
   */
  private static List<AssociationLinkerType<?, ?>> sortAssociationLinkerTypes(
      List<AssociationLinkerType<?, ?>> associationLinkerTypes) {
    Comparator<AssociationLinkerType<?, ?>> reversedComparator =
        Comparator.<AssociationLinkerType<?, ?>>comparingInt(AssociationLinkerType::getDepth)
            .reversed();
    return associationLinkerTypes.stream().sorted(reversedComparator).toList();
  }

  @Override
  public EntityType<?> getRoot() {
    return root;
  }

  @Override
  public String getTableAlias() {
    return tableAlias;
  }

  @Override
  public List<AssociationLinkerType<?, ?>> getAssociationLinkerTypes() {
    return associationLinkerTypes;
  }
}
