package org.seasar.doma.jdbc.criteria.query;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.context.Context;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

/**
 * The {@code EmptyAliasManager} class is a specialized implementation of {@code AliasManager} where
 * all aliases for entity and property metamodels are resolved to empty strings by default.
 *
 * <p>This class can be used when aliasing is not required, providing a simple and minimal alias
 * management strategy.
 *
 * <p>It provides functionality to manage alias mappings in a way that clears existing mappings and
 * replaces them with the provided or default alias resolution logic.
 */
class EmptyAliasManager extends AliasManager {

  private final Config config;

  EmptyAliasManager(Config config, Context context) {
    this(config, context, (__) -> "");
  }

  @SuppressWarnings("deprecation")
  private EmptyAliasManager(
      Config config, Context context, Function<EntityMetamodel<?>, String> aliasResolver) {
    super(context);
    this.config = Objects.requireNonNull(config);
    replaceAliases(context.getEntityMetamodels(), entityAliasMap, propertyAliasMap, aliasResolver);
  }

  private static void replaceAliases(
      List<EntityMetamodel<?>> entityMetamodels,
      Map<EntityMetamodel<?>, String> entityAliasMap,
      Map<PropertyMetamodel<?>, String> propertyAliasMap,
      Function<EntityMetamodel<?>, String> aliasResolver) {
    entityAliasMap.clear();
    propertyAliasMap.clear();
    for (EntityMetamodel<?> entityMetamodel : entityMetamodels) {
      String alias = aliasResolver.apply(entityMetamodel);
      entityAliasMap.put(entityMetamodel, alias);
      for (PropertyMetamodel<?> propertyMetamodel : entityMetamodel.allPropertyMetamodels()) {
        propertyAliasMap.put(propertyMetamodel, alias);
      }
    }
  }

  @Override
  protected AliasManager asParent() {
    Function<EntityMetamodel<?>, String> aliasResolver = (m) -> getQualifiedTableName(config, m);
    return new EmptyAliasManager(config, context, aliasResolver);
  }

  private static String getQualifiedTableName(Config config, EntityMetamodel<?> entityMetamodel) {
    return entityMetamodel
        .asType()
        .getQualifiedTableName(config.getNaming()::apply, config.getDialect()::applyQuote);
  }
}
