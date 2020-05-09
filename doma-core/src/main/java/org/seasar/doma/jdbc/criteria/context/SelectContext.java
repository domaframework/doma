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
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public class SelectContext implements Context {
  public final EntityDef<?> entityDef;
  public Projection projection;
  public boolean distinct;
  public final List<Join> joins = new ArrayList<>();
  public List<Criterion> where = new ArrayList<>();
  public final List<PropertyDef<?>> groupBy = new ArrayList<>();
  public List<Criterion> having = new ArrayList<>();
  public final List<Pair<OrderByItem, String>> orderBy = new ArrayList<>();
  public Integer limit;
  public Integer offset;
  public ForUpdate forUpdate;
  public final Map<Pair<EntityDef<?>, EntityDef<?>>, BiConsumer<Object, Object>> associations =
      new LinkedHashMap<>();
  public final SelectSettings options = new SelectSettings();

  public SelectContext(EntityDef<?> entityDef) {
    this(entityDef, Projection.All);
  }

  private SelectContext(EntityDef<?> entityDef, Projection projection) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(projection);
    this.entityDef = entityDef;
    this.projection = projection;
  }

  @Override
  public List<EntityDef<?>> getEntityDefs() {
    List<EntityDef<?>> entityDefs = new ArrayList<>();
    entityDefs.add(entityDef);
    List<EntityDef<?>> joinedEntityDefs = joins.stream().map(j -> j.entityDef).collect(toList());
    entityDefs.addAll(joinedEntityDefs);
    return entityDefs;
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
    return options;
  }

  public List<EntityDef<?>> allEntityDefs() {
    Stream<EntityDef<?>> a = Stream.of(entityDef);
    Stream<EntityDef<?>> b =
        associations.keySet().stream().flatMap(pair -> Stream.of(pair.fst, pair.snd));
    return Stream.concat(a, b).distinct().collect(toList());
  }

  public List<PropertyDef<?>> allPropertyDefs() {
    return projection.accept(
        new Projection.Visitor<List<PropertyDef<?>>>() {
          @Override
          public List<PropertyDef<?>> visit(Projection.All all) {
            return allEntityDefs().stream()
                .flatMap(it -> it.allPropertyDefs().stream())
                .collect(toList());
          }

          @Override
          public List<PropertyDef<?>> visit(Projection.List list) {
            if (list.propertyDefs.isEmpty()) {
              return visit(Projection.All);
            }
            return list.propertyDefs;
          }
        });
  }
}
