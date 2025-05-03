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

import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.JdbcMappable;

/**
 * Represents a property of an entity class that can be mapped to a database column.
 *
 * <p>This interface defines methods for getting and setting property values on entity instances, as
 * well as converting between entity property values and database column values.
 *
 * <p>Implementations of this interface are typically generated at compile time by the Doma
 * annotation processor based on {@link org.seasar.doma.Entity} annotated classes.
 *
 * @param <ENTITY> the entity type that contains this property
 * @param <BASIC> the basic type that represents the property's value
 * @see org.seasar.doma.Entity
 * @see org.seasar.doma.Column
 */
public interface Property<ENTITY, BASIC> extends JdbcMappable<BASIC> {

  /**
   * Returns the current value of this property.
   *
   * <p>The returned value may be wrapped in an Optional if the property is nullable.
   *
   * @return the current value of this property, possibly wrapped in an Optional
   */
  Object get();

  /**
   * Returns the non-optional value of this property.
   *
   * <p>If the property value is wrapped in an Optional, this method unwraps it. This method may
   * throw an exception if the property value is null.
   *
   * @return the non-optional value of this property
   * @throws java.util.NoSuchElementException if the property value is null and wrapped in an
   *     Optional
   */
  Object getAsNonOptional();

  /**
   * Loads the value from the entity into this property.
   *
   * <p>This method extracts the property value from the given entity instance and stores it in this
   * property object for later use.
   *
   * @param entity the entity instance to load the value from
   * @return this property instance for method chaining
   */
  Property<ENTITY, BASIC> load(ENTITY entity);

  /**
   * Saves the current value of this property to the given entity instance.
   *
   * <p>This method updates the property value in the given entity instance with the current value
   * stored in this property object.
   *
   * @param entity the entity instance to save the value to
   * @return this property instance for method chaining
   */
  Property<ENTITY, BASIC> save(ENTITY entity);

  /**
   * Returns this property as an {@link InParameter} for use in SQL execution.
   *
   * <p>This method allows the property to be used as a parameter in SQL statements.
   *
   * @return an input parameter representing this property's value
   * @see org.seasar.doma.jdbc.query.Query
   */
  InParameter<BASIC> asInParameter();
}
