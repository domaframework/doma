package org.seasar.doma.jdbc.criteria.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.def.EntityDef;

public class UpdateContext implements Context {
  public final EntityDef<?> entityDef;
  public final List<EntityDef<?>> entityDefs;
  public final Map<Operand.Prop, Operand.Param> set = new LinkedHashMap<>();
  public List<Criterion> where = new ArrayList<>();

  public UpdateContext(EntityDef<?> entityDef) {
    Objects.requireNonNull(entityDef);
    this.entityDef = entityDef;
    this.entityDefs = Collections.singletonList(entityDef);
  }

  @Override
  public List<EntityDef<?>> getEntityDefs() {
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
}
