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
package org.seasar.doma.jdbc.criteria.command;

import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import org.seasar.doma.internal.util.Combinations;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.EntityId;
import org.seasar.doma.jdbc.EntityRef;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.query.SelectQuery;

public class AssociateCommand<ENTITY> implements Command<List<ENTITY>> {
  private final SelectContext context;
  private final SelectQuery query;
  private final EntityMetamodel<ENTITY> entityMetamodel;

  public AssociateCommand(
      SelectContext context, SelectQuery query, EntityMetamodel<ENTITY> entityMetamodel) {
    this.context = Objects.requireNonNull(context);
    this.query = Objects.requireNonNull(query);
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<ENTITY> execute() {
    Set<EntityId> rootEntityIds = new LinkedHashSet<>();
    Map<EntityId, EntityRef> cache = new HashMap<>();
    Combinations<EntityKey> combinations = new Combinations<>();
    SelectCommand<List<EntityPool>> command =
        new SelectCommand<>(
            query,
            new EntityPoolIterationHandler(
                entityMetamodel, rootEntityIds, context.getProjectionEntityMetamodels()));
    List<EntityPool> entityPools = command.execute();
    for (EntityPool entityPool : entityPools) {
      Map<EntityMetamodel<?>, Pair<EntityKey, EntityRef>> associationCandidate =
          new LinkedHashMap<>();
      for (Map.Entry<EntityKey, EntityData> e : entityPool.entrySet()) {
        EntityKey key = e.getKey();
        EntityData data = e.getValue();
        EntityId entityId = key.asEntityId();
        EntityRef entityRef =
            cache.computeIfAbsent(
                entityId,
                k -> {
                  EntityType<Object> entityType = (EntityType<Object>) k.entityType();
                  Object entity = entityType.newEntity(data.getStates());
                  if (!entityType.isImmutable()) {
                    entityType.saveCurrentStates(entity);
                  }
                  return new EntityRef(entity);
                });
        associationCandidate.put(key.getEntityMetamodel(), new Pair<>(key, entityRef));
      }
      associate(combinations, associationCandidate);
    }
    return (List<ENTITY>)
        rootEntityIds.stream()
            .map(cache::get)
            .map(EntityRef::getEntity)
            .filter(Objects::nonNull)
            .collect(toList());
  }

  private void associate(
      Combinations<EntityKey> combinations,
      Map<EntityMetamodel<?>, Pair<EntityKey, EntityRef>> associationCandidate) {
    for (Map.Entry<Pair<EntityMetamodel<?>, EntityMetamodel<?>>, BiFunction<Object, Object, Object>>
        e : context.associations.entrySet()) {
      Pair<EntityMetamodel<?>, EntityMetamodel<?>> metamodelPair = e.getKey();
      BiFunction<Object, Object, Object> associator = e.getValue();
      Pair<EntityKey, EntityRef> source = associationCandidate.get(metamodelPair.fst);
      Pair<EntityKey, EntityRef> target = associationCandidate.get(metamodelPair.snd);
      if (source == null || target == null) {
        continue;
      }
      Pair<EntityKey, EntityKey> keyPair = new Pair<>(source.fst, target.fst);
      if (combinations.contains(keyPair)) {
        continue;
      }
      EntityRef sourceEntityRef = source.snd;
      EntityRef targetEntityRef = target.snd;
      Object sourceEntity = sourceEntityRef.getEntity();
      Object targetEntity = targetEntityRef.getEntity();
      Object resultEntity = associator.apply(sourceEntity, targetEntity);
      if (resultEntity != null && resultEntity != sourceEntity) {
        // Update a reference to an immutable entity
        sourceEntityRef.setEntity(resultEntity);
      }
      combinations.add(keyPair);
    }
  }

  @Override
  public Query getQuery() {
    return query;
  }
}
