package org.seasar.doma.jdbc.criteria.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.Context;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public class AliasManager {
  private final AliasManager parent;
  private final Map<EntityDef<?>, String> entityAliasMap = new HashMap<>();
  private final Map<PropertyDef<?>, String> propertyAliasMap = new HashMap<>();
  private int index;

  public AliasManager(Context context) {
    this(context, null);
  }

  public AliasManager(Context context, AliasManager parent) {
    Objects.requireNonNull(context);
    this.parent = parent;
    index = parent != null ? parent.index : 0;
    for (EntityDef<?> entityDef : context.getEntityDefs()) {
      String alias = "t" + index + "_";
      index++;
      entityAliasMap.put(entityDef, alias);
      for (PropertyDef<?> propertyDef : entityDef.allPropertyDefs()) {
        propertyAliasMap.put(propertyDef, alias);
      }
    }
  }

  public String getAlias(EntityDef<?> entityDef) {
    if (parent != null) {
      String alias = parent.getAlias(entityDef);
      if (alias != null) {
        return alias;
      }
    }
    return entityAliasMap.get(entityDef);
  }

  public String getAlias(PropertyDef<?> propertyDef) {
    if (parent != null) {
      String alias = parent.getAlias(propertyDef);
      if (alias != null) {
        return alias;
      }
    }
    return propertyAliasMap.get(propertyDef);
  }
}
