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
import org.seasar.doma.jdbc.criteria.def.EntityDef;
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
    List<EntityDef<?>> entityDefs = context.allEntityDefs();
    SelectCommand<List<EntityPool>> command =
        new SelectCommand<>(query, new EntityPoolIterationHandler(entityDefs));
    List<EntityPool> entityPools = command.execute();
    for (EntityPool entityPool : entityPools) {
      Map<EntityDef<?>, Object> associationCandidate = new LinkedHashMap<>();
      for (Map.Entry<EntityKey, EntityData> e : entityPool.entrySet()) {
        EntityKey key = e.getKey();
        EntityData data = e.getValue();
        Object entity =
            cache.computeIfAbsent(
                key,
                k -> {
                  EntityDef<?> entityDef = k.getEntityDef();
                  EntityType<Object> entityType = (EntityType<Object>) entityDef.asType();
                  Object newEntity = entityType.newEntity(data.getStates());
                  if (!entityType.isImmutable()) {
                    entityType.saveCurrentStates(newEntity);
                  }
                  return newEntity;
                });
        associationCandidate.put(key.getEntityDef(), entity);
      }
      associate(associationCandidate);
    }
    return (List<ENTITY>)
        cache.entrySet().stream()
            .filter(e -> e.getKey().getEntityDef() == context.entityDef)
            .map(Map.Entry::getValue)
            .collect(toList());
  }

  private void associate(Map<EntityDef<?>, Object> associationCandidate) {
    for (Map.Entry<Pair<EntityDef<?>, EntityDef<?>>, BiConsumer<Object, Object>> e :
        context.associations.entrySet()) {
      Pair<EntityDef<?>, EntityDef<?>> pair = e.getKey();
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
