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
 * A callback listener interface that is invoked during entity lifecycle events.
 *
 * <p>This interface defines methods that are called before and after entity
 * persistence operations (insert, update, delete). Entity listeners can be used
 * to implement cross-cutting concerns such as validation, auditing, or setting
 * default values.
 *
 * <p>Entity listeners are associated with entity classes using the
 * {@link org.seasar.doma.Entity#listener()} attribute.
 *
 * <p>The implementation class must have a public no-arg constructor.
 *
 * <p>The implementation class must be thread safe.
 *
 * @param <ENTITY> the entity type this listener operates on
 * @see org.seasar.doma.Entity#listener()
 * @see org.seasar.doma.EntityListeners
 */
public interface EntityListener<ENTITY> {

  /**
   * Called before an entity is inserted into the database.
   *
   * <p>This method allows implementing pre-insert logic such as setting
   * creation timestamps, generating IDs, or validating entity state.
   *
   * @param entity the entity instance about to be inserted
   * @param context the context containing information about the insert operation
   * @see org.seasar.doma.Insert
   * @see org.seasar.doma.BatchInsert
   */
  default void preInsert(ENTITY entity, PreInsertContext<ENTITY> context) {}

  /**
   * Called before an entity is updated in the database.
   *
   * <p>This method allows implementing pre-update logic such as setting
   * modification timestamps, validating entity state, or implementing
   * business rules that must be enforced before updates.
   *
   * @param entity the entity instance about to be updated
   * @param context the context containing information about the update operation
   * @see org.seasar.doma.Update
   * @see org.seasar.doma.BatchUpdate
   */
  default void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context) {}

  /**
   * Called before an entity is deleted from the database.
   *
   * <p>This method allows implementing pre-delete logic such as
   * validating that the entity can be deleted, logging deletion attempts,
   * or implementing business rules that must be enforced before deletions.
   *
   * @param entity the entity instance about to be deleted
   * @param context the context containing information about the delete operation
   * @see org.seasar.doma.Delete
   * @see org.seasar.doma.BatchDelete
   */
  default void preDelete(ENTITY entity, PreDeleteContext<ENTITY> context) {}

  /**
   * Called after an entity has been successfully inserted into the database.
   *
   * <p>This method allows implementing post-insert logic such as
   * processing generated IDs, triggering related operations, or
   * performing additional validations after the insert operation.
   *
   * @param entity the entity instance that was inserted
   * @param context the context containing information about the insert operation
   * @see org.seasar.doma.Insert
   * @see org.seasar.doma.BatchInsert
   */
  default void postInsert(ENTITY entity, PostInsertContext<ENTITY> context) {}

  /**
   * Called after an entity has been successfully updated in the database.
   *
   * <p>This method allows implementing post-update logic such as
   * triggering related operations, performing additional validations,
   * or executing business logic that depends on the updated state.
   *
   * @param entity the entity instance that was updated
   * @param context the context containing information about the update operation
   * @see org.seasar.doma.Update
   * @see org.seasar.doma.BatchUpdate
   */
  default void postUpdate(ENTITY entity, PostUpdateContext<ENTITY> context) {}

  /**
   * Called after an entity has been successfully deleted from the database.
   *
   * <p>This method allows implementing post-delete logic such as
   * cleaning up related resources, triggering cascading operations,
   * or logging successful deletions for audit purposes.
   *
   * @param entity the entity instance that was deleted
   * @param context the context containing information about the delete operation
   * @see org.seasar.doma.Delete
   * @see org.seasar.doma.BatchDelete
   */
  default void postDelete(ENTITY entity, PostDeleteContext<ENTITY> context) {}
}
