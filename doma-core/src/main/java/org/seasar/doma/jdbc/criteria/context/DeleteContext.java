package org.seasar.doma.jdbc.criteria.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

public class DeleteContext implements Context {
  public final EntityMetamodel<?> entityMetamodel;
  public final List<EntityMetamodel<?>> entityMetamodels;
  public List<Criterion> where = new ArrayList<>();
  public final DeleteSettings settings;

  public DeleteContext(EntityMetamodel<?> entityMetamodel) {
    this(entityMetamodel, new DeleteSettings());
  }

  public DeleteContext(EntityMetamodel<?> entityMetamodel, DeleteSettings settings) {
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entityMetamodels = Collections.singletonList(entityMetamodel);
    this.settings = Objects.requireNonNull(settings);
  }

  @Override
  public List<EntityMetamodel<?>> getEntityMetamodels() {
    return entityMetamodels;
  }

  @Override
  public DeleteSettings getSettings() {
    return settings;
  }
}
