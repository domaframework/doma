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

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * Exception thrown when an entity type implementation is not found.
 *
 * <p>This exception is typically thrown when Doma cannot locate the generated
 * implementation class for an entity type. This usually happens when the annotation
 * processor did not generate the implementation class or when the class is not
 * accessible at runtime.
 *
 * @see org.seasar.doma.jdbc.entity.EntityType
 * @see org.seasar.doma.Entity
 */
public class EntityTypeNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String entityClassName;

  private final String entityTypeClassName;

  /**
   * Constructs a new exception with the specified cause, entity class name, and entity type class name.
   *
   * @param cause the cause of this exception
   * @param entityClassName the fully qualified name of the entity class
   * @param entityTypeClassName the fully qualified name of the entity type implementation class that was not found
   */
  public EntityTypeNotFoundException(
      Throwable cause, String entityClassName, String entityTypeClassName) {
    super(Message.DOMA2203, cause, entityClassName, entityTypeClassName, cause);
    this.entityClassName = entityClassName;
    this.entityTypeClassName = entityTypeClassName;
  }

  /**
   * Returns the fully qualified name of the entity class.
   *
   * @return the entity class name
   */
  public String getEntityClassName() {
    return entityClassName;
  }

  /**
   * Returns the fully qualified name of the entity type implementation class that was not found.
   *
   * @return the entity type class name
   */
  public String getEntityTypeClassName() {
    return entityTypeClassName;
  }
}
