package org.seasar.doma.jdbc.criteria.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.seasar.doma.jdbc.criteria.context.Context;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public class AliasManager {
  private final AliasManager parent;
  private final Map<EntityMetamodel<?>, String> entityAliasMap = new HashMap<>();
  private final Map<PropertyMetamodel<?>, String> propertyAliasMap = new HashMap<>();
  private final int index;

  public AliasManager(Context context) {
    this(context, null);
  }

  public AliasManager(Context context, AliasManager parent) {
    Objects.requireNonNull(context);
    this.parent = parent;
    int index = parent != null ? parent.index : 0;
    for (EntityMetamodel<?> entityMetamodel : context.getEntityMetamodels()) {
      String alias = "t" + index + "_";
      index++;
      entityAliasMap.put(entityMetamodel, alias);
      for (PropertyMetamodel<?> propertyMetamodel : entityMetamodel.allPropertyMetamodels()) {
        propertyAliasMap.put(propertyMetamodel, alias);
      }
    }
    this.index = index;
  }

  public String getAlias(EntityMetamodel<?> entityMetamodel) {
    return getAlias(entityMetamodel, AliasManager::getAlias, entityAliasMap::get);
  }

  public String getAlias(PropertyMetamodel<?> propertyMetamodel) {
    return getAlias(propertyMetamodel, AliasManager::getAlias, propertyAliasMap::get);
  }

  private <KEY> String getAlias(
      KEY key,
      BiFunction<AliasManager, KEY, String> getParentAlias,
      Function<KEY, String> getCurrentAlias) {
    if (parent != null) {
      String alias = getParentAlias.apply(parent, key);
      if (alias != null) {
        return alias;
      }
    }
    return getCurrentAlias.apply(key);
  }
}
