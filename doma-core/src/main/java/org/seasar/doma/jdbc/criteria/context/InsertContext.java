package org.seasar.doma.jdbc.criteria.context;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class InsertContext implements Context {
  public final EntityMetamodel<?> entityMetamodel;
  public final List<EntityMetamodel<?>> entityMetamodels;
  public final Map<Operand.Prop, Operand.Param> values = new LinkedHashMap<>();
  public final InsertSettings settings = new InsertSettings();
  public SelectContext selectContext;
  public OnDuplicateContext onDuplicateContext = new OnDuplicateContext();

  public InsertContext(EntityMetamodel<?> entityMetamodel) {
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entityMetamodels = Collections.singletonList(entityMetamodel);
  }

  public class OnDuplicateContext {
    public DuplicateKeyType duplicateKeyType = DuplicateKeyType.EXCEPTION;
    public List<PropertyMetamodel<?>> keys = Collections.emptyList();
    public final Map<Operand.Prop, Operand> setValues = new LinkedHashMap<>();
  }

  @Override
  public List<EntityMetamodel<?>> getEntityMetamodels() {
    return entityMetamodels;
  }

  @Override
  public InsertSettings getSettings() {
    return settings;
  }
}
