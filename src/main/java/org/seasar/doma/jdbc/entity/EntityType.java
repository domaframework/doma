package org.seasar.doma.jdbc.entity;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.seasar.doma.Entity;

/**
 * A description for an entity class.
 *
 * <p>The implementation instance must be thread safe.
 *
 * @param <ENTITY> the entity type
 */
public interface EntityType<ENTITY> {

  /**
   * Whether the entity is immutable.
   *
   * @return {@code true} if the entity is immutable
   */
  boolean isImmutable();

  /**
   * Returns the entity name
   *
   * @return the entity name
   */
  String getName();

  /**
   * Returns the catalog name.
   *
   * @return the catalog name
   */
  String getCatalogName();

  /**
   * Returns the schema name.
   *
   * @return the schema name
   */
  String getSchemaName();

  /**
   * Returns the table name.
   *
   * @return the table name
   */
  String getTableName();

  /**
   * Returns the table name.
   *
   * @param namingFunction the function that applies naming convention
   * @return the table name
   */
  String getTableName(BiFunction<NamingType, String, String> namingFunction);

  /**
   * Returns the qualified table name.
   *
   * @return the qualified table name
   */
  String getQualifiedTableName();

  /**
   * Returns the qualified table name.
   *
   * @param quoteFunction the function that applies quotation marks
   * @return the qualified table name
   */
  String getQualifiedTableName(Function<String, String> quoteFunction);

  /**
   * Returns the qualified table name.
   *
   * @param namingFunction the function that applies naming convention
   * @param quoteFunction the function that applies quotation marks
   * @return the qualified table name
   */
  String getQualifiedTableName(
      BiFunction<NamingType, String, String> namingFunction,
      Function<String, String> quoteFunction);

  /**
   * Returns quotation marks are required for catalog, schema and table.
   *
   * @return {@code true} if quotation marks are required
   */
  boolean isQuoteRequired();

  /**
   * Returns the naming convention.
   *
   * @return the naming convention or {@code null}
   * @see Entity#naming()
   */
  NamingType getNamingType();

  /**
   * Returns the identity property description whose value is generated.
   *
   * @return the identity property description
   */
  GeneratedIdPropertyType<? super ENTITY, ENTITY, ?, ?> getGeneratedIdPropertyType();

  /**
   * Returns the version property description
   *
   * @return the version property description
   */
  VersionPropertyType<? super ENTITY, ENTITY, ?, ?> getVersionPropertyType();

  /**
   * Returns the tenant id property description
   *
   * @return the tenant id property description
   */
  TenantIdPropertyType<? super ENTITY, ENTITY, ?, ?> getTenantIdPropertyType();

  /**
   * Returns the identity property descriptions
   *
   * @return the identity property descriptions
   */
  List<EntityPropertyType<ENTITY, ?>> getIdPropertyTypes();

  /**
   * Returns the property description with the name.
   *
   * @param __name the name of the property
   * @return the name of the property or {@code null} if the property is not found
   */
  EntityPropertyType<ENTITY, ?> getEntityPropertyType(String __name);

  /**
   * Returns the property descriptions.
   *
   * @return the property descriptions
   */
  List<EntityPropertyType<ENTITY, ?>> getEntityPropertyTypes();

  /**
   * Instantiate a new entity.
   *
   * @param __args the arguments
   * @return an entity
   */
  ENTITY newEntity(Map<String, Property<ENTITY, ?>> __args);

  /**
   * Returns the entity class.
   *
   * @return the entity class
   */
  Class<ENTITY> getEntityClass();

  /**
   * Saves the current states.
   *
   * @param entity the current states
   */
  void saveCurrentStates(ENTITY entity);

  /**
   * Returns the original states.
   *
   * @param entity the original states
   * @return the original states or {@code null} if it does not exist
   */
  ENTITY getOriginalStates(ENTITY entity);

  /**
   * Handles the entity before an insert.
   *
   * @param entity the entity
   * @param context the context
   */
  void preInsert(ENTITY entity, PreInsertContext<ENTITY> context);

  /**
   * Handles the entity before an update.
   *
   * @param entity the entity
   * @param context the context
   */
  void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context);

  /**
   * Handles the entity before a delete.
   *
   * @param entity the entity
   * @param context the context
   */
  void preDelete(ENTITY entity, PreDeleteContext<ENTITY> context);

  /**
   * Handles the entity after an insert.
   *
   * @param entity the entity
   * @param context the context
   */
  void postInsert(ENTITY entity, PostInsertContext<ENTITY> context);

  /**
   * Handles the entity after an update.
   *
   * @param entity the entity
   * @param context the context
   */
  void postUpdate(ENTITY entity, PostUpdateContext<ENTITY> context);

  /**
   * Handles the entity after a delete.
   *
   * @param entity the entity
   * @param context the context
   */
  void postDelete(ENTITY entity, PostDeleteContext<ENTITY> context);
}
