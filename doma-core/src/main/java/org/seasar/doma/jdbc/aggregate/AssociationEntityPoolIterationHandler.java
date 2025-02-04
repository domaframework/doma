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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.seasar.doma.internal.jdbc.command.AbstractIterationHandler;
import org.seasar.doma.internal.jdbc.command.ResultListCallback;
import org.seasar.doma.jdbc.EntityId;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * Handles the iteration of {@link AssociationEntityPool} results from a query execution and manages
 * the creation and configuration of object providers used to process the results.
 */
public class AssociationEntityPoolIterationHandler
    extends AbstractIterationHandler<AssociationEntityPool, List<AssociationEntityPool>> {

  private final EntityType<?> rootEntityType;
  private final AggregateStrategyType aggregateStrategyType;
  private final boolean resultMappingEnsured;
  private final Set<EntityId> rootEntityIds;
  private final Map<EntityId, Object> entityCache;

  public AssociationEntityPoolIterationHandler(
      EntityType<?> rootEntityType,
      AggregateStrategyType aggregateStrategyType,
      boolean resultMappingEnsured,
      Set<EntityId> rootEntityIds,
      Map<EntityId, Object> entityCache) {
    super(new ResultListCallback<>());
    this.rootEntityType = Objects.requireNonNull(rootEntityType);
    this.aggregateStrategyType = Objects.requireNonNull(aggregateStrategyType);
    this.resultMappingEnsured = resultMappingEnsured;
    this.rootEntityIds = Objects.requireNonNull(rootEntityIds);
    this.entityCache = Objects.requireNonNull(entityCache);
  }

  @Override
  protected ObjectProvider<AssociationEntityPool> createObjectProvider(SelectQuery query) {
    return new AssociationEntityPoolProvider(
        rootEntityType,
        aggregateStrategyType,
        query,
        resultMappingEnsured,
        rootEntityIds,
        entityCache);
  }
}
