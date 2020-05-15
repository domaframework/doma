package org.seasar.doma.jdbc.criteria.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

public class UpdateContext implements Context {
  public final EntityMetamodel<?> entityMetamodel;
  public final List<EntityMetamodel<?>> entityMetamodels;
  public final Map<Operand.Prop, Operand.Param> set = new LinkedHashMap<>();
  public List<Criterion> where = new ArrayList<>();
  public final UpdateSettings settings = new UpdateSettings();

  public UpdateContext(EntityMetamodel<?> entityMetamodel) {
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entityMetamodels = Collections.singletonList(entityMetamodel);
  }

  @Override
  public List<EntityMetamodel<?>> getEntityMetamodels() {
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
  public UpdateSettings getSettings() {
    return settings;
  }
}
