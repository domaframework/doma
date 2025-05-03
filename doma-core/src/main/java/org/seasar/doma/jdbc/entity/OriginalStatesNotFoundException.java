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

import org.seasar.doma.OriginalStates;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * Exception thrown when a field annotated with {@link OriginalStates} is not found in an entity.
 *
 * <p>This exception is typically thrown when Doma attempts to access the original states
 * of an entity but cannot find a field annotated with {@link OriginalStates}. The original
 * states field is required for optimistic concurrency control and for determining which
 * properties have changed since the entity was loaded.
 *
 * @see org.seasar.doma.OriginalStates
 * @see org.seasar.doma.jdbc.entity.EntityType
 */
public class OriginalStatesNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String entityClassName;

  private final String fieldName;

  /**
   * Constructs a new exception with the specified cause, entity class name, and field name.
   *
   * @param cause the cause of this exception
   * @param entityClassName the fully qualified name of the entity class
   * @param fieldName the name of the field that was expected to be annotated with {@link OriginalStates}
   */
  public OriginalStatesNotFoundException(
      Throwable cause, String entityClassName, String fieldName) {
    super(Message.DOMA2213, cause, entityClassName, fieldName, cause);
    this.entityClassName = entityClassName;
    this.fieldName = fieldName;
  }

  public String getEntityClassName() {
    return entityClassName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
