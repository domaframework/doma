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

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import org.seasar.doma.internal.util.Combinations;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.EntityId;
import org.seasar.doma.jdbc.EntityRef;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * The AggregateCommand class represents a command designed to execute an aggregate operation on a
 * stream of entities and return a result.
 *
 * @param <RESULT> the result type of the aggregation
 * @param <ENTITY> the root entity type used within the aggregation
 */
public class AggregateCommand<RESULT, ENTITY> implements Command<RESULT> {
  private final SelectQuery query;
  private final EntityType<ENTITY> rootEntityType;
  private final StreamReducer<RESULT, ENTITY> streamReducer;
  private final AggregateStrategyType aggregateStrategyType;

  public AggregateCommand(
      SelectQuery query,
      EntityType<ENTITY> rootEntityType,
      StreamReducer<RESULT, ENTITY> streamReducer,
      AggregateStrategyType aggregateStrategyType) {
    this.query = Objects.requireNonNull(query);
    this.rootEntityType = Objects.requireNonNull(rootEntityType);
    this.streamReducer = Objects.requireNonNull(streamReducer);
    this.aggregateStrategyType = Objects.requireNonNull(aggregateStrategyType);
  }

  @Override
  public RESULT execute() {
    Set<EntityId> rootEntityIds = new LinkedHashSet<>();
    Map<EntityId, EntityRef> entityCache = new HashMap<>();
    Combinations<AssociationEntityKey> combinations = new Combinations<>();

    SelectCommand<List<AssociationEntityPool>> command =
        new SelectCommand<>(
            query,
            new AssociationEntityPoolIterationHandler(
                rootEntityType,
                aggregateStrategyType,
                query.isResultMappingEnsured(),
                rootEntityIds,
                entityCache));
    List<AssociationEntityPool> entityPools = command.execute();
    for (AssociationEntityPool entityPool : entityPools) {
      associate(combinations, entityPool);
    }

    @SuppressWarnings("unchecked")
    Stream<ENTITY> stream =
        (Stream<ENTITY>)
            rootEntityIds.stream()
                .map(entityCache::get)
                .map(EntityRef::getEntity)
                .filter(Objects::nonNull)
                .filter(rootEntityType.getEntityClass()::isInstance);

    return streamReducer.reduce(stream);
  }

  /**
   * Establishes associations between source and target entities based on the provided combination
   * of linkage rules and updates the cache with newly associated entities.
   */
  private void associate(
      Combinations<AssociationEntityKey> combinations, AssociationEntityPool entityPool) {

    for (AssociationLinkerType<?, ?> linkerType :
        aggregateStrategyType.getAssociationLinkerTypes()) {
      AssociationEntityPoolEntry source = entityPool.get(linkerType.getSourcePathKey());
      AssociationEntityPoolEntry target = entityPool.get(linkerType.getTargetPathKey());
      if (source == null || target == null) {
        continue;
      }

      Pair<AssociationEntityKey, AssociationEntityKey> keyPair =
          new Pair<>(source.entityKey(), target.entityKey());
      if (combinations.contains(keyPair)) {
        continue;
      }
      combinations.add(keyPair);

      @SuppressWarnings("unchecked")
      BiFunction<Object, Object, Object> linker =
          (BiFunction<Object, Object, Object>) linkerType.getLinker();
      EntityRef sourceEntityRef = source.entityRef();
      EntityRef targetEntityRef = target.entityRef();
      Object sourceEntity = sourceEntityRef.getEntity();
      Object targetEntity = targetEntityRef.getEntity();
      Object resultEntity = linker.apply(sourceEntity, targetEntity);
      if (resultEntity != null && resultEntity != sourceEntity) {
        // Update a reference to an immutable entity
        sourceEntityRef.setEntity(resultEntity);
      }
    }
  }

  @Override
  public Query getQuery() {
    return query;
  }
}
