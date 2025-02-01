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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    Map<LinkableEntityKey, Object> cache = new LinkedHashMap<>();
    Combinations<LinkableEntityKey> combinations = new Combinations<>();
    SelectCommand<List<LinkableEntityPool>> command =
        new SelectCommand<>(
            query,
            new LinkableEntityPoolIterationHandler(
                entityType, aggregateStrategyType, query.isResultMappingEnsured(), cache));
    List<LinkableEntityPool> entityPools = command.execute();
    for (LinkableEntityPool entityPool : entityPools) {
      Map<String, LinkableEntity> associationCandidate = new LinkedHashMap<>();
      for (LinkableEntity entry : entityPool) {
        associationCandidate.put(entry.key().propertyPath(), entry);
      }
      associate(cache, combinations, associationCandidate);
    }
    @SuppressWarnings("unchecked")
    Stream<ENTITY> stream =
        (Stream<ENTITY>)
            cache.entrySet().stream()
                .filter(e -> e.getKey().isRootEntityKey())
                .map(Map.Entry::getValue)
                .filter(entityType.getEntityClass()::isInstance);
    return streamReducer.reduce(stream);
  }

  /**
   * Establishes associations between source and target entities based on the provided combination
   * of linkage rules and updates the cache with newly associated entities.
   */
  private void associate(
      Map<LinkableEntityKey, Object> cache,
      Combinations<LinkableEntityKey> combinations,
      Map<String, LinkableEntity> associationCandidate) {
    for (AssociationLinkerType<?, ?> linkerType :
        aggregateStrategyType.getAssociationLinkerTypes()) {
      LinkableEntity source = associationCandidate.get(linkerType.getAncestorPath());
      LinkableEntity target = associationCandidate.get(linkerType.getPropertyPath());
      if (source == null || target == null) {
        continue;
      }
      Pair<LinkableEntityKey, LinkableEntityKey> keyPair = new Pair<>(source.key(), target.key());
      if (combinations.contains(keyPair)) {
        continue;
      }
      @SuppressWarnings("unchecked")
      BiFunction<Object, Object, Object> linker =
          (BiFunction<Object, Object, Object>) linkerType.getLinker();
      Object newEntity = linker.apply(source.entity(), target.entity());
      if (newEntity != null) {
        cache.replace(source.key(), newEntity);
        associationCandidate.replace(
            linkerType.getAncestorPath(), new LinkableEntity(source.key(), newEntity));
      }
      combinations.add(keyPair);
    }
  }

  @Override
  public Query getQuery() {
    return query;
  }
}
