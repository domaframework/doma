package org.seasar.doma.jdbc.criteria.context;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.def.EntityDef;

public class InsertContext implements Context {
  public final EntityDef<?> entityDef;
  public final List<EntityDef<?>> entityDefs;
  public Map<Operand.Prop, Operand.Param> values = new LinkedHashMap<>();
  public final InsertSettings options = new InsertSettings();

  public InsertContext(EntityDef<?> entityDef) {
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
    throw new UnsupportedOperationException();
  }

  @Override
  public void setWhere(List<Criterion> where) {
    throw new UnsupportedOperationException();
  }

  @Override
  public InsertSettings getSettings() {
    return options;
  }
}
