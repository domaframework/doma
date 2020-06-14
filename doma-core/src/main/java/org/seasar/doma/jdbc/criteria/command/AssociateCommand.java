package org.seasar.doma.jdbc.criteria.command;

import static java.util.stream.Collectors.toList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import org.seasar.doma.internal.util.Pair;
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
    Map<EntityKey, Object> cache = new LinkedHashMap<>();
    SelectCommand<List<EntityPool>> command =
        new SelectCommand<>(
            query, new EntityPoolIterationHandler(context.getProjectionEntityMetamodels()));
    List<EntityPool> entityPools = command.execute();
    for (EntityPool entityPool : entityPools) {
      Map<EntityMetamodel<?>, Pair<EntityKey, Object>> associationCandidate = new LinkedHashMap<>();
      for (Map.Entry<EntityKey, EntityData> e : entityPool.entrySet()) {
        EntityKey key = e.getKey();
        EntityData data = e.getValue();
        Object entity =
            cache.computeIfAbsent(
                key,
                k -> {
                  EntityMetamodel<?> entityMetamodel = k.getEntityMetamodel();
                  EntityType<Object> entityType = (EntityType<Object>) entityMetamodel.asType();
                  Object newEntity = entityType.newEntity(data.getStates());
                  if (!entityType.isImmutable()) {
                    entityType.saveCurrentStates(newEntity);
                  }
                  return newEntity;
                });
        associationCandidate.put(key.getEntityMetamodel(), new Pair<>(key, entity));
      }
      associate(cache, associationCandidate);
    }
    return (List<ENTITY>)
        cache.entrySet().stream()
            .filter(e -> e.getKey().getEntityMetamodel() == entityMetamodel)
            .map(Map.Entry::getValue)
            .collect(toList());
  }

  private void associate(
      Map<EntityKey, Object> cache,
      Map<EntityMetamodel<?>, Pair<EntityKey, Object>> associationCandidate) {
    for (Map.Entry<Pair<EntityMetamodel<?>, EntityMetamodel<?>>, BiFunction<Object, Object, Object>>
        e : context.associations.entrySet()) {
      Pair<EntityMetamodel<?>, EntityMetamodel<?>> metamodelPair = e.getKey();
      BiFunction<Object, Object, Object> associator = e.getValue();
      Pair<EntityKey, Object> keyAndEntity1 = associationCandidate.get(metamodelPair.fst);
      Pair<EntityKey, Object> keyAndEntity2 = associationCandidate.get(metamodelPair.snd);
      if (keyAndEntity1 == null || keyAndEntity2 == null) {
        continue;
      }
      Object newEntity = associator.apply(keyAndEntity1.snd, keyAndEntity2.snd);
      if (newEntity != null) {
        cache.replace(keyAndEntity1.fst, newEntity);
        associationCandidate.replace(metamodelPair.fst, new Pair<>(keyAndEntity1.fst, newEntity));
      }
    }
  }

  @Override
  public Query getQuery() {
    return query;
  }
}
