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
import org.seasar.doma.Insert;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.query.DuplicateKeyType;
import org.seasar.doma.jdbc.query.ReturningProperties;

/**
 * A context for pre-processing an entity before an insert operation.
 *
 * <p>This interface provides methods to access and modify the entity being inserted, as well as
 * methods to access configuration and metadata about the insert operation. It is typically used in
 * entity listener implementations to perform custom logic before an insert operation.
 *
 * <p>The context is passed to the {@code preInsert} method of entity listeners.
 *
 * @param <E> the entity type being inserted
 * @see org.seasar.doma.jdbc.entity.EntityListener#preInsert(Object, PreInsertContext)
 * @see org.seasar.doma.Insert
 */
public interface PreInsertContext<E> {

  /**
   * Returns the entity type metadata for the entity being inserted.
   *
   * <p>The entity type provides access to metadata about the entity class, including its
   * properties, naming conventions, and other configuration.
   *
   * @return the entity type metadata
   */
  EntityType<E> getEntityType();

  /**
   * Returns the DAO method that is annotated with {@link Insert} and triggered this insert
   * operation.
   *
   * <p>This method provides access to the reflection Method object representing the DAO method that
   * initiated the insert operation.
   *
   * @return the Method object representing the DAO method
   */
  Method getMethod();

  /**
   * Returns the Doma configuration associated with this insert operation.
   *
   * <p>The configuration provides access to database connection, dialect, and other runtime
   * settings for the current operation.
   *
   * @return the Doma configuration
   */
  Config getConfig();

  /**
   * Returns the entity instance that is about to be inserted.
   *
   * <p>This is the entity instance before any modifications made by entity listeners in their
   * preInsert methods.
   *
   * @return the entity instance to be inserted
   */
  E getNewEntity();

  /**
   * Sets a new entity instance to be used for the insert operation.
   *
   * <p>This method is primarily used with immutable entity classes, allowing entity listeners to
   * replace the entity instance with a modified version before it is inserted into the database.
   *
   * @param newEntity the new entity instance to use
   * @throws DomaNullPointerException if {@code newEntity} is {@code null}
   */
  void setNewEntity(E newEntity);

  /**
   * Returns the duplicate key handling strategy for this insert operation.
   *
   * <p>This method indicates how the insert operation should handle duplicate key violations, such
   * as by updating the existing record, ignoring the error, or throwing an exception.
   *
   * @return the duplicate key handling strategy
   * @see org.seasar.doma.jdbc.query.DuplicateKeyType
   * @see org.seasar.doma.Insert#duplicateKeyType()
   */
  DuplicateKeyType getDuplicateKeyType();

  /**
   * Returns the returning properties configuration for this insert operation.
   *
   * <p>Returning properties specify which columns should be returned by the database after an
   * insert operation, typically used with the RETURNING clause in SQL.
   *
   * @return the returning properties configuration, never {@code null}
   * @see org.seasar.doma.Returning
   */
  default ReturningProperties getReturningProperties() {
    return ReturningProperties.NONE;
  }
}
