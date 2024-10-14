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
  public final Map<Operand.Prop, Operand> set = new LinkedHashMap<>();
  public List<Criterion> where = new ArrayList<>();
  public final UpdateSettings settings;

  public UpdateContext(EntityMetamodel<?> entityMetamodel) {
    this(entityMetamodel, new UpdateSettings());
  }

  public UpdateContext(EntityMetamodel<?> entityMetamodel, UpdateSettings settings) {
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entityMetamodels = Collections.singletonList(entityMetamodel);
    this.settings = Objects.requireNonNull(settings);
  }

  @Override
  public List<EntityMetamodel<?>> getEntityMetamodels() {
    return entityMetamodels;
  }

  @Override
  public UpdateSettings getSettings() {
    return settings;
  }
}
