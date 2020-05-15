package org.seasar.doma.jdbc.criteria.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.Context;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public class AliasManager {
  private final AliasManager parent;
  private final Map<EntityMetamodel<?>, String> entityAliasMap = new HashMap<>();
  private final Map<PropertyMetamodel<?>, String> propertyAliasMap = new HashMap<>();
  private int index;

  public AliasManager(Context context) {
    this(context, null);
  }

  public AliasManager(Context context, AliasManager parent) {
    Objects.requireNonNull(context);
    this.parent = parent;
    index = parent != null ? parent.index : 0;
    for (EntityMetamodel<?> entityMetamodel : context.getEntityMetamodels()) {
      String alias = "t" + index + "_";
      index++;
      entityAliasMap.put(entityMetamodel, alias);
      for (PropertyMetamodel<?> propertyMetamodel : entityMetamodel.allPropertyMetamodels()) {
        propertyAliasMap.put(propertyMetamodel, alias);
      }
    }
  }

  public String getAlias(EntityMetamodel<?> entityMetamodel) {
    if (parent != null) {
      String alias = parent.getAlias(entityMetamodel);
      if (alias != null) {
        return alias;
      }
    }
    return entityAliasMap.get(entityMetamodel);
  }

  public String getAlias(PropertyMetamodel<?> propertyMetamodel) {
    if (parent != null) {
      String alias = parent.getAlias(propertyMetamodel);
      if (alias != null) {
        return alias;
      }
    }
    return propertyAliasMap.get(propertyMetamodel);
  }
}
