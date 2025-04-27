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
 * A context for a post process of an insert.
 *
 * @param <E> the entity type
 */
public interface PostInsertContext<E> {

  /**
   * Returns the entity description.
   *
   * @return the entity description
   */
  EntityType<E> getEntityType();

  /**
   * The method that is annotated with {@link Insert}.
   *
   * @return the method
   */
  Method getMethod();

  /**
   * Returns the configuration.
   *
   * @return the configuration
   */
  Config getConfig();

  /**
   * Returns the new entity.
   *
   * @return the new entity
   */
  E getNewEntity();

  /**
   * Sets the new entity.
   *
   * <p>This method is available, when the entity is immutable.
   *
   * @param newEntity the entity
   * @throws DomaNullPointerException if {@code newEntity} is {@code null}
   */
  void setNewEntity(E newEntity);

  /**
   * Retrieves the type of the duplicate key when inserting a new entity.
   *
   * @return the type of the duplicate key
   */
  DuplicateKeyType getDuplicateKeyType();

  /**
   * Returns the instance of {@code ReturningProperties} associated with the context.
   *
   * @return the {@code ReturningProperties} instance, never {@code null}
   */
  default ReturningProperties getReturningProperties() {
    return ReturningProperties.NONE;
  }
}
