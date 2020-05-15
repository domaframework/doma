package org.seasar.doma.jdbc.criteria.context;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
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
  public Projection projection = Projection.All;
  public DistinctOption distinct = DistinctOption.DISABLED;
  public final List<Join> joins = new ArrayList<>();
  public List<Criterion> where = new ArrayList<>();
  public final List<PropertyMetamodel<?>> groupBy = new ArrayList<>();
  public List<Criterion> having = new ArrayList<>();
  public final List<Pair<OrderByItem, String>> orderBy = new ArrayList<>();
  public Integer limit;
  public Integer offset;
  public ForUpdate forUpdate = new ForUpdate(ForUpdateOption.DISABLED);
  public final Map<Pair<EntityMetamodel<?>, EntityMetamodel<?>>, BiConsumer<Object, Object>>
      associations = new LinkedHashMap<>();
  public final SelectSettings settings = new SelectSettings();

  public SelectContext(EntityMetamodel<?> entityMetamodel) {
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
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
  public List<Criterion> getWhere() {
    return where;
  }

  @Override
  public void setWhere(List<Criterion> where) {
    this.where = where;
  }

  @Override
  public SelectSettings getSettings() {
    return settings;
  }

  public List<EntityMetamodel<?>> allEntityDefs() {
    Stream<EntityMetamodel<?>> a = Stream.of(entityMetamodel);
    Stream<EntityMetamodel<?>> b =
        associations.keySet().stream().flatMap(pair -> Stream.of(pair.fst, pair.snd));
    return Stream.concat(a, b).distinct().collect(toList());
  }

  public List<PropertyMetamodel<?>> allPropertyMetamodels() {
    return projection.accept(
        new Projection.Visitor<List<PropertyMetamodel<?>>>() {
          @Override
          public List<PropertyMetamodel<?>> visit(Projection.All all) {
            return allEntityDefs().stream()
                .flatMap(it -> it.allPropertyMetamodels().stream())
                .collect(toList());
          }

          @Override
          public List<PropertyMetamodel<?>> visit(Projection.List list) {
            if (list.propertyMetamodels.isEmpty()) {
              return visit(Projection.All);
            }
            return list.propertyMetamodels;
          }
        });
  }
}
