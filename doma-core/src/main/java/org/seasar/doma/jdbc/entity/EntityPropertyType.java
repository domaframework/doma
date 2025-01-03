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
 * A entity property description.
 *
 * <p>This instance is not required to be thread safe.
 *
 * @param <ENTITY> the entity type
 * @param <BASIC> the basic type
 */
public interface EntityPropertyType<ENTITY, BASIC> {

  /**
   * Returns the property name.
   *
   * @return the property name
   */
  String getName();

  /**
   * Return the column name.
   *
   * @return the column name
   */
  String getColumnName();

  /**
   * Return the column name.
   *
   * @param quoteFunction the function that applies quotation marks
   * @return the name of the column.
   */
  String getColumnName(Function<String, String> quoteFunction);

  /**
   * Return the column name.
   *
   * @param namingFunction the function that applies naming convention
   * @return the name of the column.
   */
  String getColumnName(BiFunction<NamingType, String, String> namingFunction);

  /**
   * Return the column name.
   *
   * @param namingFunction the function that applies naming convention
   * @param quoteFunction the function that applies quotation marks
   * @return the name of the column
   */
  String getColumnName(
      BiFunction<NamingType, String, String> namingFunction,
      Function<String, String> quoteFunction);

  /**
   * Whether quotation marks are required to the column name.
   *
   * @return {@code true} if required
   */
  boolean isQuoteRequired();

  /**
   * Whether this property is an identifier.
   *
   * @return {@code true} if this property is an identifier
   */
  boolean isId();

  /**
   * Whether this property is a version.
   *
   * @return {@code true} if this property is a version
   */
  boolean isVersion();

  /**
   * Whether this property is a tenant id.
   *
   * @return {@code true} if this property is a tenant id
   */
  boolean isTenantId();

  /**
   * Whether the column that is mapped to this property is included in SQL INSERT statements.
   *
   * @return {@code true} if included in SQL INSERT statements.
   */
  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  boolean isInsertable();

  /**
   * Whether the column that is mapped to this property is included in SQL UPDATE statements.
   *
   * @return {@code true} if included in SQL UPDATE statements.
   */
  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  boolean isUpdatable();

  /**
   * Creates the property.
   *
   * @return the property
   */
  Property<ENTITY, BASIC> createProperty();

  /**
   * Copies this property value.
   *
   * <p>This method does deep copy. But this method does not copy JDBC specific objects such as
   * {@code Blob}.
   *
   * @param dest the entity that is the copy destination
   * @param src the entity that is the copy source
   */
  void copy(ENTITY dest, ENTITY src);
}
