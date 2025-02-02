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
  private final EntityType<ENTITY> entityType;
  private final StreamReducer<RESULT, ENTITY> streamReducer;
  private final AggregateStrategyType aggregateStrategyType;

  public AggregateCommand(
      SelectQuery query,
      EntityType<ENTITY> entityType,
      StreamReducer<RESULT, ENTITY> streamReducer,
      AggregateStrategyType aggregateStrategyType) {
    this.query = Objects.requireNonNull(query);
    this.entityType = Objects.requireNonNull(entityType);
    this.streamReducer = Objects.requireNonNull(streamReducer);
    this.aggregateStrategyType = Objects.requireNonNull(aggregateStrategyType);
  }

  @Override
  public RESULT execute() {
    Set<EntityCacheKey> rootEntityKeys = new LinkedHashSet<>();
    Map<EntityCacheKey, Object> entityCache = new HashMap<>();
    SelectCommand<List<LinkableEntityPool>> command =
        new SelectCommand<>(
            query,
            new LinkableEntityPoolIterationHandler(
                entityType,
                aggregateStrategyType,
                query.isResultMappingEnsured(),
                rootEntityKeys,
                entityCache));
    List<LinkableEntityPool> entityPools = command.execute();

    Combinations<LinkableEntityKey> combinations = new Combinations<>();
    for (LinkableEntityPool entityPool : entityPools) {
      Map<PathKey, LinkableEntityPoolEntry> associationCandidate = new HashMap<>();
      for (LinkableEntityPoolEntry entry : entityPool) {
        associationCandidate.put(entry.entityKey().pathKey(), entry);
      }
      associate(entityCache, combinations, associationCandidate);
    }

    @SuppressWarnings("unchecked")
    Stream<ENTITY> stream =
        (Stream<ENTITY>)
            rootEntityKeys.stream()
                .map(entityCache::get)
                .filter(Objects::nonNull)
                .filter(entityType.getEntityClass()::isInstance);

    return streamReducer.reduce(stream);
  }

  /**
   * Establishes associations between source and target entities based on the provided combination
   * of linkage rules and updates the cache with newly associated entities.
   */
  private void associate(
      Map<EntityCacheKey, Object> entityCache,
      Combinations<LinkableEntityKey> combinations,
      Map<PathKey, LinkableEntityPoolEntry> associationCandidate) {

    for (AssociationLinkerType<?, ?> linkerType :
        aggregateStrategyType.getAssociationLinkerTypes()) {
      LinkableEntityPoolEntry source = associationCandidate.get(linkerType.getSourcePathKey());
      LinkableEntityPoolEntry target = associationCandidate.get(linkerType.getTargetPathKey());
      if (source == null || target == null) {
        continue;
      }

      Pair<LinkableEntityKey, LinkableEntityKey> keyPair =
          new Pair<>(source.entityKey(), target.entityKey());
      if (combinations.contains(keyPair)) {
        continue;
      }
      combinations.add(keyPair);

      @SuppressWarnings("unchecked")
      BiFunction<Object, Object, Object> linker =
          (BiFunction<Object, Object, Object>) linkerType.getLinker();
      Object entity = linker.apply(source.entity(), target.entity());
      if (entity != null) {
        EntityCacheKey cacheKey = EntityCacheKey.of(source.entityKey());
        entityCache.replace(cacheKey, entity);
        associationCandidate.replace(
            source.pathKey(), new LinkableEntityPoolEntry(source.entityKey(), entity));
      }
    }
  }

  @Override
  public Query getQuery() {
    return query;
  }
}
