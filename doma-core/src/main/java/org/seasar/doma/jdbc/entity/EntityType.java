/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.entity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.seasar.doma.Entity;

/**
 * Describes metadata for an entity class that maps to a database table.
 *
 * <p>This interface provides methods for accessing entity metadata, such as
 * table name, property types, and entity lifecycle methods. It also provides
 * methods for creating entity instances and managing entity state.
 *
 * <p>Implementations of this interface are typically generated at compile time by the
 * Doma annotation processor based on {@link org.seasar.doma.Entity} annotated classes.
 *
 * <p>The implementation instance must be thread safe.
 *
 * @param <ENTITY> the entity type this metadata describes
 * @see org.seasar.doma.Entity
 * @see org.seasar.doma.Table
 */
public interface EntityType<ENTITY> {

  /**
   * Determines whether the entity class is immutable.
   *
   * <p>An immutable entity has all its properties set through its constructor
   * and provides no setters to modify its state after creation. Immutable entities
   * are typically implemented using final fields.
   *
   * @return {@code true} if the entity is immutable, {@code false} otherwise
   * @see org.seasar.doma.Entity#immutable()
   */
  boolean isImmutable();

  /**
   * Returns the name of this entity.
   *
   * <p>The entity name typically corresponds to the simple name of the entity class,
   * unless explicitly specified using the {@link org.seasar.doma.Entity#name()} attribute.
   *
   * @return the entity name
   * @see org.seasar.doma.Entity#name()
   */
  String getName();

  /**
   * Returns the database catalog name for this entity.
   *
   * <p>The catalog name is determined based on the {@link org.seasar.doma.Table#catalog()}
   * attribute if present, or may be empty if not specified.
   *
   * @return the database catalog name, or an empty string if not specified
   * @see org.seasar.doma.Table#catalog()
   */
  String getCatalogName();

  /**
   * Returns the database schema name for this entity.
   *
   * <p>The schema name is determined based on the {@link org.seasar.doma.Table#schema()}
   * attribute if present, or may be empty if not specified.
   *
   * @return the database schema name, or an empty string if not specified
   * @see org.seasar.doma.Table#schema()
   */
  String getSchemaName();

  /**
   * Returns the database table name for this entity with naming convention applied.
   *
   * <p>The table name is determined based on the {@link org.seasar.doma.Table#name()}
   * attribute if present, or derived from the entity name using the specified
   * naming convention function.
   *
   * @param namingFunction the function that applies naming convention to the table name
   * @return the database table name with naming convention applied
   * @see org.seasar.doma.Table#name()
   * @see #getNamingType()
   */
  String getTableName(BiFunction<NamingType, String, String> namingFunction);

  /**
   * Returns the fully qualified database table name for this entity.
   *
   * <p>The qualified table name includes the catalog and schema names if specified,
   * with both naming convention and quotation marks applied as needed.
   *
   * @param namingFunction the function that applies naming convention to the table name
   * @param quoteFunction the function that applies quotation marks to the table name
   * @return the fully qualified table name (catalog.schema.table)
   * @see #getCatalogName()
   * @see #getSchemaName()
   * @see #getTableName(BiFunction)
   * @see #isQuoteRequired()
   */
  String getQualifiedTableName(
      BiFunction<NamingType, String, String> namingFunction,
      Function<String, String> quoteFunction);

  /**
   * Determines whether quotation marks are required for catalog, schema, and table names.
   *
   * <p>This method indicates if the catalog, schema, and table names should be quoted
   * in SQL statements, which is typically needed for reserved words or names with
   * special characters.
   *
   * @return {@code true} if quotation marks are required, {@code false} otherwise
   * @see org.seasar.doma.Table#quote()
   */
  boolean isQuoteRequired();

  /**
   * Returns the naming convention type used for this entity.
   *
   * <p>The naming convention determines how entity and property names are
   * converted to table and column names when not explicitly specified.
   *
   * @return the naming convention type, or {@code null} if not specified
   * @see Entity#naming()
   * @see NamingType
   */
  NamingType getNamingType();

  /**
   * Returns the property type for the generated identity (primary key) property.
   *
   * <p>This method returns the property type for a primary key property that has its
   * value automatically generated, such as an auto-increment column or a sequence-generated value.
   *
   * @return the generated identity property type, or {@code null} if none exists
   * @see org.seasar.doma.GeneratedValue
   * @see org.seasar.doma.Id
   */
  GeneratedIdPropertyType<ENTITY, ?, ?> getGeneratedIdPropertyType();

  /**
   * Returns the property type for the version property used for optimistic locking.
   *
   * <p>This method returns the property type for a version property that is
   * automatically incremented during update operations to implement optimistic
   * concurrency control.
   *
   * @return the version property type, or {@code null} if none exists
   * @see org.seasar.doma.Version
   */
  VersionPropertyType<ENTITY, ?, ?> getVersionPropertyType();

  /**
   * Returns the property type for the tenant identifier property used for multi-tenancy.
   *
   * <p>This method returns the property type for a tenant identifier property that is
   * used to implement multi-tenant database access, where a single database instance
   * serves multiple logical tenants.
   *
   * @return the tenant identifier property type, or {@code null} if none exists
   * @see org.seasar.doma.TenantId
   */
  TenantIdPropertyType<ENTITY, ?, ?> getTenantIdPropertyType();

  /**
   * Returns a list of property types for all primary key properties in this entity.
   *
   * <p>This method returns property types for all properties annotated with
   * {@link org.seasar.doma.Id}, which mark them as primary key columns in the
   * database table.
   *
   * @return a list of primary key property types, or an empty list if none exist
   * @see org.seasar.doma.Id
   */
  List<EntityPropertyType<ENTITY, ?>> getIdPropertyTypes();

  /**
   * Returns the property type for a specific property by name.
   *
   * <p>This method allows looking up a property type by its name, which is useful
   * for dynamic property access and metadata inspection.
   *
   * @param __name the name of the property to look up
   * @return the property type, or {@code null} if no property with the given name exists
   * @see #getEntityPropertyTypes()
   */
  EntityPropertyType<ENTITY, ?> getEntityPropertyType(String __name);

  /**
   * Returns a list of all property types defined for this entity.
   *
   * <p>This method returns property types for all properties in the entity class,
   * including regular properties, ID properties, version properties, and tenant ID properties.
   *
   * @return a list of all entity property types
   * @see #getEntityPropertyType(String)
   * @see #getIdPropertyTypes()
   */
  List<EntityPropertyType<ENTITY, ?>> getEntityPropertyTypes();

  /**
   * Returns a list of association property types defined for this entity.
   *
   * <p>This method returns property types for all properties annotated with
   * {@link org.seasar.doma.Association}, which define relationships between
   * this entity and other entities.
   *
   * @return a list of association property types, or an empty list if no associations are defined
   * @see org.seasar.doma.Association
   */
  default List<AssociationPropertyType> getAssociationPropertyTypes() {
    return Collections.emptyList();
  }

  /**
   * Creates a new instance of the entity class.
   *
   * <p>This method instantiates a new entity instance using the provided property values.
   * It is typically used by the framework to create entity instances from database query results.
   *
   * @param __args a map of property names to property values
   * @return a new entity instance
   */
  ENTITY newEntity(Map<String, Property<ENTITY, ?>> __args);

  /**
   * Returns the Java Class object for this entity type.
   *
   * <p>This method returns the Class object that represents the entity class
   * this EntityType describes. It can be used for reflection operations or
   * type checking.
   *
   * @return the entity class
   */
  Class<ENTITY> getEntityClass();

  /**
   * Saves the current state of an entity for later comparison.
   *
   * <p>This method is used to implement optimistic concurrency control and
   * dirty property detection. It stores the current state of the entity so that
   * it can be compared with future states to determine what has changed.
   *
   * @param entity the entity instance whose state should be saved
   * @see #getOriginalStates(Object)
   * @see org.seasar.doma.OriginalStates
   */
  void saveCurrentStates(ENTITY entity);

  /**
   * Returns the original state of an entity that was previously saved.
   *
   * <p>This method retrieves the original state of an entity that was previously
   * saved using {@link #saveCurrentStates(Object)}. It is used to implement
   * optimistic concurrency control and dirty property detection by comparing
   * the current state with the original state.
   *
   * @param entity the entity instance whose original state should be retrieved
   * @return the original state of the entity, or {@code null} if no state was saved
   * @see #saveCurrentStates(Object)
   * @see org.seasar.doma.OriginalStates
   */
  ENTITY getOriginalStates(ENTITY entity);

  /**
   * Called before an entity is inserted into the database.
   *
   * <p>This method delegates to the appropriate entity listener's preInsert method
   * to implement pre-insert logic such as setting creation timestamps, generating IDs,
   * or validating entity state.
   *
   * @param entity the entity instance about to be inserted
   * @param context the context containing information about the insert operation
   * @see org.seasar.doma.jdbc.entity.EntityListener#preInsert(Object, PreInsertContext)
   */
  void preInsert(ENTITY entity, PreInsertContext<ENTITY> context);

  /**
   * Called before an entity is updated in the database.
   *
   * <p>This method delegates to the appropriate entity listener's preUpdate method
   * to implement pre-update logic such as setting modification timestamps,
   * validating entity state, or implementing business rules.
   *
   * @param entity the entity instance about to be updated
   * @param context the context containing information about the update operation
   * @see org.seasar.doma.jdbc.entity.EntityListener#preUpdate(Object, PreUpdateContext)
   */
  void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context);

  /**
   * Called before an entity is deleted from the database.
   *
   * <p>This method delegates to the appropriate entity listener's preDelete method
   * to implement pre-delete logic such as validating that the entity can be deleted,
   * logging deletion attempts, or implementing business rules.
   *
   * @param entity the entity instance about to be deleted
   * @param context the context containing information about the delete operation
   * @see org.seasar.doma.jdbc.entity.EntityListener#preDelete(Object, PreDeleteContext)
   */
  void preDelete(ENTITY entity, PreDeleteContext<ENTITY> context);

  /**
   * Called after an entity has been successfully inserted into the database.
   *
   * <p>This method delegates to the appropriate entity listener's postInsert method
   * to implement post-insert logic such as processing generated IDs, triggering
   * related operations, or performing additional validations.
   *
   * @param entity the entity instance that was inserted
   * @param context the context containing information about the insert operation
   * @see org.seasar.doma.jdbc.entity.EntityListener#postInsert(Object, PostInsertContext)
   */
  void postInsert(ENTITY entity, PostInsertContext<ENTITY> context);

  /**
   * Called after an entity has been successfully updated in the database.
   *
   * <p>This method delegates to the appropriate entity listener's postUpdate method
   * to implement post-update logic such as triggering related operations,
   * performing additional validations, or executing business logic.
   *
   * @param entity the entity instance that was updated
   * @param context the context containing information about the update operation
   * @see org.seasar.doma.jdbc.entity.EntityListener#postUpdate(Object, PostUpdateContext)
   */
  void postUpdate(ENTITY entity, PostUpdateContext<ENTITY> context);

  /**
   * Called after an entity has been successfully deleted from the database.
   *
   * <p>This method delegates to the appropriate entity listener's postDelete method
   * to implement post-delete logic such as cleaning up related resources,
   * triggering cascading operations, or logging successful deletions.
   *
   * @param entity the entity instance that was deleted
   * @param context the context containing information about the delete operation
   * @see org.seasar.doma.jdbc.entity.EntityListener#postDelete(Object, PostDeleteContext)
   */
  void postDelete(ENTITY entity, PostDeleteContext<ENTITY> context);
}
