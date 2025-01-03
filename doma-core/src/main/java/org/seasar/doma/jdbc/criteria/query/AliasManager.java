package org.seasar.doma.jdbc.criteria.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.context.Context;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

/**
 * The {@code AliasManager} class is responsible for managing alias mappings for entity and property
 * metamodels. It helps in generating and retrieving unique aliases for entities and their
 * associated properties to ensure consistency in database queries.
 *
 * <p>This class provides both static factory methods for instantiation and functionality to link to
 * a parent {@code AliasManager} to resolve aliases hierarchically.
 */
public class AliasManager {
  protected final Context context;
  protected final AliasManager parent;
  protected final Map<EntityMetamodel<?>, String> entityAliasMap = new HashMap<>();
  protected final Map<PropertyMetamodel<?>, String> propertyAliasMap = new HashMap<>();
  private final int index;

  /**
   * Constructs an instance of {@code AliasManager}.
   *
   * <p>This constructor is marked as {@code @Deprecated}. Usage of {@code of(Config, Context)} or
   * {@code of(Config, Context, AliasManager)} is recommended.
   *
   * @param context the context containing the entity metamodels; must not be null
   * @throws NullPointerException if {@code context} is null
   */
  @Deprecated
  public AliasManager(Context context) {
    this(context, null);
  }

  /**
   * Constructs an instance of {@code AliasManager}.
   *
   * <p>This constructor is marked as {@code @Deprecated}. Usage of {@code of(Config, Context)} or
   * {@code of(Config, Context, AliasManager)} is recommended.
   *
   * @param context the context containing the entity metamodels; must not be null
   * @param parent the parent {@code AliasManager}, or null if none
   * @throws NullPointerException if {@code context} is null
   */
  @Deprecated
  public AliasManager(Context context, AliasManager parent) {
    this.context = Objects.requireNonNull(context);
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

  /**
   * Retrieves the alias corresponding to the given {@code EntityMetamodel}.
   *
   * @param entityMetamodel the entity metamodel for which the alias is to be retrieved; must not be
   *     null
   * @return the alias as a {@code String}, or {@code null} if no alias is found
   */
  public String getAlias(EntityMetamodel<?> entityMetamodel) {
    return getAlias(entityMetamodel, AliasManager::getAlias, entityAliasMap::get);
  }

  /**
   * Retrieves the alias for the given {@code PropertyMetamodel}.
   *
   * @param propertyMetamodel the property metamodel for which the alias is to be retrieved; must
   *     not be null
   * @return the alias as a {@code String}, or {@code null} if no alias is found
   */
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

  protected AliasManager asParent() {
    return this;
  }

  /**
   * Creates a new instance of {@code AliasManager} using the provided configuration and context.
   *
   * @param config the runtime configuration for DAOs; must not be null
   * @param context the context containing the entity metamodels; must not be null
   * @return a new instance of {@code AliasManager}
   * @throws NullPointerException if {@code config} or {@code context} is null
   */
  public static AliasManager create(Config config, Context context) {
    // The config will be used in future refactoring.
    Objects.requireNonNull(config);
    return new AliasManager(context);
  }

  /**
   * Creates an empty instance of {@code AliasManager} with the provided configuration and context.
   *
   * @param config the runtime configuration for DAOs; must not be null
   * @param context the context containing the entity metamodels; must not be null
   * @return a new instance of {@code EmptyAliasManager} with a default aliasing strategy
   * @throws NullPointerException if {@code config} or {@code context} is null
   */
  public static AliasManager createEmpty(Config config, Context context) {
    return new EmptyAliasManager(config, context);
  }

  /**
   * Creates a child instance of {@code AliasManager} using the provided configuration, context, and
   * parent {@code AliasManager}.
   *
   * @param config the runtime configuration for DAOs; must not be null
   * @param context the context containing the entity metamodels; must not be null
   * @param parent the parent {@code AliasManager}; must not be null
   * @return a new child instance of {@code AliasManager}
   * @throws NullPointerException if {@code config}, {@code context}, or {@code parent} is null
   */
  public static AliasManager createChild(Config config, Context context, AliasManager parent) {
    // The config will be used in future refactoring.
    Objects.requireNonNull(config);
    Objects.requireNonNull(context);
    Objects.requireNonNull(parent);
    return new AliasManager(context, parent.asParent());
  }
}
