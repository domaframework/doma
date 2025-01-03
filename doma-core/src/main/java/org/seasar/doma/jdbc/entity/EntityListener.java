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

/**
 * A callback listener that is invoked when the corresponding entities are persisted.
 *
 * <p>The implementation class must have a public no-arg constructor.
 *
 * <p>The implementation class must be thread safe.
 *
 * @param <ENTITY> the entity type
 */
public interface EntityListener<ENTITY> {

  /**
   * Handles the entity before an insert.
   *
   * @param entity the entity
   * @param context the context
   * @see org.seasar.doma.Insert
   * @see org.seasar.doma.BatchInsert
   */
  default void preInsert(ENTITY entity, PreInsertContext<ENTITY> context) {}

  /**
   * Handles the entity before an update.
   *
   * @param entity the entity
   * @param context the context
   * @see org.seasar.doma.Update
   * @see org.seasar.doma.BatchUpdate
   */
  default void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context) {}

  /**
   * Handles the entity before a delete.
   *
   * @param entity the entity
   * @param context the context
   * @see org.seasar.doma.Delete
   * @see org.seasar.doma.BatchDelete
   */
  default void preDelete(ENTITY entity, PreDeleteContext<ENTITY> context) {}

  /**
   * Handles the entity after an insert.
   *
   * @param entity the entity
   * @param context the context
   * @see org.seasar.doma.Insert
   * @see org.seasar.doma.BatchInsert
   */
  default void postInsert(ENTITY entity, PostInsertContext<ENTITY> context) {}

  /**
   * Handles the entity after an update.
   *
   * @param entity the entity
   * @param context the context
   * @see org.seasar.doma.Update
   * @see org.seasar.doma.BatchUpdate
   */
  default void postUpdate(ENTITY entity, PostUpdateContext<ENTITY> context) {}

  /**
   * Handles the entity after a delete.
   *
   * @param entity the entity
   * @param context the context
   * @see org.seasar.doma.Delete
   * @see org.seasar.doma.BatchDelete
   */
  default void postDelete(ENTITY entity, PostDeleteContext<ENTITY> context) {}
}
