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

import java.lang.reflect.Method;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.query.ReturningProperties;

/**
 * A context for pre-processing an entity before an update operation.
 *
 * <p>This interface provides methods to check if an entity or its properties have changed
 * since the last database read, as well as methods to access and modify the entity being updated.
 * It is typically used in entity listener implementations to perform custom logic before
 * an update operation.
 *
 * <p>The context is passed to the {@code preUpdate} method of entity listeners.
 *
 * @param <E> the entity type being updated
 * @see org.seasar.doma.jdbc.entity.EntityListener#preUpdate(Object, PreUpdateContext)
 * @see org.seasar.doma.Update
 * @see org.seasar.doma.OriginalStates
 */
public interface PreUpdateContext<E> {

  /**
   * Determines whether the entity has been changed since it was last read from the database.
   *
   * <p>This method checks if any property of the entity has been modified by comparing
   * the current values with the original values stored in the {@link OriginalStates} field.
   *
   * <p>This method always returns {@code true} when {@link Update#sqlFile()} returns {@code true},
   * as SQL file-based updates do not perform change detection.
   *
   * @return {@code true} if the entity has been changed, {@code false} otherwise
   * @see OriginalStates
   */
  boolean isEntityChanged();

  /**
   * Determines whether a specific property of the entity has been changed.
   *
   * <p>This method checks if the specified property has been modified by comparing
   * its current value with the original value stored in the {@link OriginalStates} field.
   *
   * <p>This method always returns {@code true} when {@link Update#sqlFile()} returns {@code true},
   * as SQL file-based updates do not perform change detection.
   *
   * @param propertyName the name of the property to check
   * @return {@code true} if the property has been changed, {@code false} otherwise
   * @throws EntityPropertyNotDefinedException if the property is not defined in the entity
   * @see OriginalStates
   */
  boolean isPropertyChanged(String propertyName);

  /**
   * Returns the entity type metadata for the entity being updated.
   *
   * <p>The entity type provides access to metadata about the entity class,
   * including its properties, naming conventions, and other configuration.
   *
   * @return the entity type metadata
   */
  EntityType<?> getEntityType();

  /**
   * Returns the DAO method that is annotated with {@link Update} and triggered this update operation.
   *
   * <p>This method provides access to the reflection Method object representing
   * the DAO method that initiated the update operation.
   *
   * @return the Method object representing the DAO method
   */
  Method getMethod();

  /**
   * Returns the Doma configuration associated with this update operation.
   *
   * <p>The configuration provides access to database connection, dialect,
   * and other runtime settings for the current operation.
   *
   * @return the Doma configuration
   */
  Config getConfig();

  /**
   * Returns the entity instance that is being updated.
   *
   * <p>This is the entity instance after any modifications made by entity listeners
   * in their preUpdate methods.
   *
   * @return the entity instance being updated
   */
  E getNewEntity();

  /**
   * Sets a new entity instance to be used for the update operation.
   *
   * <p>This method is primarily used with immutable entity classes, allowing
   * entity listeners to replace the entity instance with a modified version.
   *
   * @param newEntity the new entity instance to use for the update
   * @throws DomaNullPointerException if {@code newEntity} is {@code null}
   */
  void setNewEntity(E newEntity);

  /**
   * Returns the returning properties configuration for this update operation.
   *
   * <p>Returning properties specify which columns should be returned by the database
   * after an update operation, typically used with the RETURNING clause in SQL.
   *
   * @return the returning properties configuration, never {@code null}
   * @see org.seasar.doma.Returning
   */
  default ReturningProperties getReturningProperties() {
    return ReturningProperties.NONE;
  }
}
