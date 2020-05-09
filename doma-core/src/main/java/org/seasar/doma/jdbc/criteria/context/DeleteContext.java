package org.seasar.doma.jdbc.criteria.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.def.EntityDef;

public class DeleteContext implements Context {
  public final EntityDef<?> entityDef;
  public final List<EntityDef<?>> entityDefs;
  public List<Criterion> where = new ArrayList<>();
  public final DeleteSettings options = new DeleteSettings();

  public DeleteContext(EntityDef<?> entityDef) {
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

  @Override
  public DeleteSettings getSettings() {
    return options;
  }
}
