package org.seasar.doma.jdbc.criteria.context;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.option.DistinctOption;
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption;

public class SelectContext implements Context {
  public final EntityMetamodel<?> entityMetamodel;
  public Projection projection;
  public DistinctOption distinct = DistinctOption.none();
  public final List<Join> joins = new ArrayList<>();
  public List<Criterion> where = new ArrayList<>();
  public final List<PropertyMetamodel<?>> groupBy = new ArrayList<>();
  public List<Criterion> having = new ArrayList<>();
  public final List<Pair<OrderByItem, String>> orderBy = new ArrayList<>();
  public Integer limit;
  public Integer offset;
  public ForUpdate forUpdate = new ForUpdate(ForUpdateOption.none());
  public final Map<Pair<EntityMetamodel<?>, EntityMetamodel<?>>, BiConsumer<Object, Object>>
      associations = new LinkedHashMap<>();
  public final SelectSettings settings = new SelectSettings();

  public SelectContext(EntityMetamodel<?> entityMetamodel) {
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.projection = new Projection.EntityMetamodels(entityMetamodel);
  }

  @Override
  public List<EntityMetamodel<?>> getEntityMetamodels() {
    List<EntityMetamodel<?>> entityMetamodels = new ArrayList<>();
    entityMetamodels.add(entityMetamodel);
    List<EntityMetamodel<?>> joinedEntityMetamodels =
        joins.stream().map(j -> j.entityMetamodel).collect(toList());
    entityMetamodels.addAll(joinedEntityMetamodels);
    return entityMetamodels;
  }

  @Override
  public SelectSettings getSettings() {
    return settings;
  }

  public Map<EntityMetamodel<?>, List<PropertyMetamodel<?>>> getProjectionEntityMetamodels() {
    return projection.accept(
        new Projection.Visitor<Map<EntityMetamodel<?>, List<PropertyMetamodel<?>>>>() {
          @Override
          public Map<EntityMetamodel<?>, List<PropertyMetamodel<?>>> visit(
              Projection.EntityMetamodels entityMetamodels) {
            Map<EntityMetamodel<?>, List<PropertyMetamodel<?>>> map =
                new LinkedHashMap<>(entityMetamodels.map);
            associations.keySet().stream()
                .flatMap(pair -> Stream.of(pair.fst, pair.snd))
                .forEach(
                    it -> {
                      if (!map.containsKey(it)) {
                        map.put(it, it.allPropertyMetamodels());
                      }
                    });
            return map;
          }

          @Override
          public Map<EntityMetamodel<?>, List<PropertyMetamodel<?>>> visit(
              Projection.PropertyMetamodels propertyMetamodels) {
            throw new IllegalStateException();
          }
        });
  }

  public List<PropertyMetamodel<?>> getProjectionPropertyMetamodels() {
    return projection.accept(
        new Projection.Visitor<List<PropertyMetamodel<?>>>() {

          @Override
          public List<PropertyMetamodel<?>> visit(Projection.EntityMetamodels entityMetamodels) {
            return getProjectionEntityMetamodels().values().stream()
                .flatMap(Collection::stream)
                .collect(toList());
          }

          @Override
          public List<PropertyMetamodel<?>> visit(
              Projection.PropertyMetamodels propertyMetamodels) {
            return propertyMetamodels.propertyMetamodels;
          }
        });
  }
}
