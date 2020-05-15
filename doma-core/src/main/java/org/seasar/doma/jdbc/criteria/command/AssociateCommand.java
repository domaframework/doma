package org.seasar.doma.jdbc.criteria.command;

import static java.util.stream.Collectors.toList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
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

  public AssociateCommand(SelectContext context, SelectQuery query) {
    this.context = Objects.requireNonNull(context);
    this.query = Objects.requireNonNull(query);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<ENTITY> execute() {
    Map<EntityKey, Object> cache = new LinkedHashMap<>();
    List<EntityMetamodel<?>> entityMetamodels = context.allEntityDefs();
    SelectCommand<List<EntityPool>> command =
        new SelectCommand<>(query, new EntityPoolIterationHandler(entityMetamodels));
    List<EntityPool> entityPools = command.execute();
    for (EntityPool entityPool : entityPools) {
      Map<EntityMetamodel<?>, Object> associationCandidate = new LinkedHashMap<>();
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
        associationCandidate.put(key.getEntityMetamodel(), entity);
      }
      associate(associationCandidate);
    }
    return (List<ENTITY>)
        cache.entrySet().stream()
            .filter(e -> e.getKey().getEntityMetamodel() == context.entityMetamodel)
            .map(Map.Entry::getValue)
            .collect(toList());
  }

  private void associate(Map<EntityMetamodel<?>, Object> associationCandidate) {
    for (Map.Entry<Pair<EntityMetamodel<?>, EntityMetamodel<?>>, BiConsumer<Object, Object>> e :
        context.associations.entrySet()) {
      Pair<EntityMetamodel<?>, EntityMetamodel<?>> pair = e.getKey();
      BiConsumer<Object, Object> associator = e.getValue();
      Object entity1 = associationCandidate.get(pair.fst);
      Object entity2 = associationCandidate.get(pair.snd);
      if (entity1 == null || entity2 == null) {
        continue;
      }
      associator.accept(entity1, entity2);
    }
  }

  @Override
  public Query getQuery() {
    return query;
  }
}
