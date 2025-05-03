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
 * Exception thrown when access to an entity property fails.
 *
 * <p>This exception is typically thrown when there is an error accessing a property
 * of an entity, such as when reflection fails due to security restrictions or when
 * a getter or setter method throws an exception.
 *
 * @see org.seasar.doma.jdbc.entity.EntityType
 * @see org.seasar.doma.jdbc.entity.EntityPropertyType
 */
public class EntityPropertyAccessException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String entityClassName;

  private final String entityPropertyName;

  /**
   * Constructs a new exception with the specified cause, entity class name, and property name.
   *
   * @param cause the cause of this exception
   * @param entityClassName the fully qualified name of the entity class
   * @param entityPropertyName the name of the property that could not be accessed
   */
  public EntityPropertyAccessException(
      Throwable cause, String entityClassName, String entityPropertyName) {
    super(Message.DOMA2208, cause, entityClassName, entityPropertyName, cause);
    this.entityClassName = entityClassName;
    this.entityPropertyName = entityPropertyName;
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
   * Returns the name of the property that could not be accessed.
   *
   * @return the entity property name
   */
  public String getEntityPropertyName() {
    return entityPropertyName;
  }
}
