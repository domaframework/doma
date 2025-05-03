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

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Describes a property of an entity class that maps to a database column.
 *
 * <p>This interface defines methods for accessing property metadata, such as property name, column
 * name, and various property characteristics (ID, version, etc.). It also provides methods for
 * creating property instances and copying property values.
 *
 * <p>Implementations of this interface are typically generated at compile time by the Doma
 * annotation processor based on {@link org.seasar.doma.Entity} annotated classes.
 *
 * <p>This instance is not required to be thread safe.
 *
 * @param <ENTITY> the entity type that contains this property
 * @param <BASIC> the basic type that represents the property's value
 * @see org.seasar.doma.Entity
 * @see org.seasar.doma.Column
 */
public interface EntityPropertyType<ENTITY, BASIC> {

  /**
   * Returns the name of this property.
   *
   * <p>The property name typically corresponds to the field or method name in the entity class that
   * is mapped to a database column.
   *
   * @return the property name
   */
  String getName();

  /**
   * Returns the database column name that this property maps to.
   *
   * <p>The column name is determined based on the {@link org.seasar.doma.Column} annotation if
   * present, or derived from the property name using the entity's naming convention.
   *
   * @return the database column name
   * @see org.seasar.doma.Column
   */
  String getColumnName();

  /**
   * Returns the database column name with optional quotation marks applied.
   *
   * <p>This method allows customizing how quotation marks are applied to the column name, which is
   * useful when generating SQL for different database dialects.
   *
   * @param quoteFunction the function that applies quotation marks to the column name
   * @return the column name with quotation marks applied as specified
   * @see #isQuoteRequired()
   */
  String getColumnName(Function<String, String> quoteFunction);

  /**
   * Returns the database column name with naming convention applied.
   *
   * <p>This method allows customizing how naming conventions are applied to the column name, which
   * is useful when generating SQL for different database naming styles.
   *
   * @param namingFunction the function that applies naming convention to the column name
   * @return the column name with naming convention applied
   * @see NamingType
   */
  String getColumnName(BiFunction<NamingType, String, String> namingFunction);

  /**
   * Returns the database column name with both naming convention and quotation marks applied.
   *
   * <p>This method combines the functionality of {@link #getColumnName(BiFunction)} and {@link
   * #getColumnName(Function)} to apply both naming conventions and quotation marks to the column
   * name.
   *
   * @param namingFunction the function that applies naming convention to the column name
   * @param quoteFunction the function that applies quotation marks to the column name
   * @return the column name with both naming convention and quotation marks applied
   * @see NamingType
   * @see #isQuoteRequired()
   */
  String getColumnName(
      BiFunction<NamingType, String, String> namingFunction,
      Function<String, String> quoteFunction);

  /**
   * Determines whether quotation marks are required for the column name.
   *
   * <p>This method indicates if the column name should be quoted in SQL statements, which is
   * typically needed for reserved words or column names with special characters.
   *
   * @return {@code true} if quotation marks are required, {@code false} otherwise
   * @see org.seasar.doma.Column#quote()
   */
  boolean isQuoteRequired();

  /**
   * Determines whether this property represents a primary key (identifier).
   *
   * <p>This method indicates if the property is annotated with {@link org.seasar.doma.Id}, which
   * marks it as a primary key column in the database table.
   *
   * @return {@code true} if this property is a primary key, {@code false} otherwise
   * @see org.seasar.doma.Id
   */
  boolean isId();

  /**
   * Determines whether this property represents a version column for optimistic locking.
   *
   * <p>This method indicates if the property is annotated with {@link org.seasar.doma.Version},
   * which marks it as a version column used for optimistic concurrency control.
   *
   * @return {@code true} if this property is a version column, {@code false} otherwise
   * @see org.seasar.doma.Version
   */
  boolean isVersion();

  /**
   * Determines whether this property represents a tenant identifier for multi-tenancy.
   *
   * <p>This method indicates if the property is annotated with {@link org.seasar.doma.TenantId},
   * which marks it as a tenant identifier column used for multi-tenant database access.
   *
   * @return {@code true} if this property is a tenant identifier, {@code false} otherwise
   * @see org.seasar.doma.TenantId
   */
  boolean isTenantId();

  /**
   * Determines whether the column mapped to this property is included in SQL INSERT statements.
   *
   * <p>This method indicates if the property should be included when generating INSERT statements.
   * Properties may be excluded from INSERT statements if they are auto-generated or should not be
   * explicitly set.
   *
   * @return {@code true} if the property is included in SQL INSERT statements, {@code false}
   *     otherwise
   * @see org.seasar.doma.Column#insertable()
   */
  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  boolean isInsertable();

  /**
   * Determines whether the column mapped to this property is included in SQL UPDATE statements.
   *
   * <p>This method indicates if the property should be included when generating UPDATE statements.
   * Properties may be excluded from UPDATE statements if they are read-only or should not be
   * modified after creation.
   *
   * @return {@code true} if the property is included in SQL UPDATE statements, {@code false}
   *     otherwise
   * @see org.seasar.doma.Column#updatable()
   */
  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  boolean isUpdatable();

  /**
   * Creates a property instance for this property type.
   *
   * <p>The created property instance can be used to get and set property values on entity
   * instances, as well as to convert between entity property values and database column values.
   *
   * @return a new property instance
   * @see Property
   */
  Property<ENTITY, BASIC> createProperty();

  /**
   * Copies the value of this property from one entity instance to another.
   *
   * <p>This method performs a deep copy of the property value. However, it does not copy
   * JDBC-specific objects such as {@code Blob}, {@code Clob}, or {@code SQLXML}.
   *
   * <p>This method is useful for creating copies of entity instances while preserving the values of
   * their properties.
   *
   * @param dest the entity instance that is the copy destination
   * @param src the entity instance that is the copy source
   */
  void copy(ENTITY dest, ENTITY src);
}
