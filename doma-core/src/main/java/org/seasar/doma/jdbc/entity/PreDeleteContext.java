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
import org.seasar.doma.Delete;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.query.ReturningProperties;

/**
 * A context for pre-processing an entity before a delete operation.
 *
 * <p>This interface provides methods to access and modify the entity being deleted, as well as
 * methods to access configuration and metadata about the delete operation. It is typically used in
 * entity listener implementations to perform custom logic before a delete operation.
 *
 * <p>The context is passed to the {@code preDelete} method of entity listeners.
 *
 * @param <E> the entity type being deleted
 * @see org.seasar.doma.jdbc.entity.EntityListener#preDelete(Object, PreDeleteContext)
 * @see org.seasar.doma.Delete
 */
public interface PreDeleteContext<E> {

  /**
   * Returns the entity type metadata for the entity being deleted.
   *
   * <p>The entity type provides access to metadata about the entity class, including its
   * properties, naming conventions, and other configuration.
   *
   * @return the entity type metadata
   */
  EntityType<E> getEntityType();

  /**
   * Returns the DAO method that is annotated with {@link Delete} and triggered this delete
   * operation.
   *
   * <p>This method provides access to the reflection Method object representing the DAO method that
   * initiated the delete operation.
   *
   * @return the Method object representing the DAO method
   */
  Method getMethod();

  /**
   * Returns the Doma configuration associated with this delete operation.
   *
   * <p>The configuration provides access to database connection, dialect, and other runtime
   * settings for the current operation.
   *
   * @return the Doma configuration
   */
  Config getConfig();

  /**
   * Returns the entity instance that is about to be deleted.
   *
   * <p>This is the entity instance before any modifications made by entity listeners in their
   * preDelete methods.
   *
   * @return the entity instance to be deleted
   */
  E getNewEntity();

  /**
   * Sets a new entity instance to be used for the delete operation.
   *
   * <p>This method is primarily used with immutable entity classes, allowing entity listeners to
   * replace the entity instance with a modified version before it is deleted from the database.
   *
   * @param newEntity the new entity instance to use
   * @throws DomaNullPointerException if {@code newEntity} is {@code null}
   */
  void setNewEntity(E newEntity);

  /**
   * Returns the returning properties configuration for this delete operation.
   *
   * <p>Returning properties specify which columns should be returned by the database after a delete
   * operation, typically used with the RETURNING clause in SQL.
   *
   * @return the returning properties configuration, never {@code null}
   * @see org.seasar.doma.Returning
   */
  default ReturningProperties getReturningProperties() {
    return ReturningProperties.NONE;
  }
}
