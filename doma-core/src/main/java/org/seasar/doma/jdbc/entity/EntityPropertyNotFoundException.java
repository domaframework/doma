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
 * Exception thrown when a property is not found in an entity class.
 *
 * <p>This exception is typically thrown when attempting to access a property
 * that does not exist in an entity, either through reflection or when mapping
 * query results to entity objects.
 *
 * @see org.seasar.doma.jdbc.entity.EntityType
 * @see org.seasar.doma.jdbc.entity.EntityPropertyType
 */
public class EntityPropertyNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String entityClassName;

  private final String entityPropertyName;

  /**
   * Constructs a new exception with the specified entity class name and property name.
   *
   * @param entityClassName the fully qualified name of the entity class
   * @param entityPropertyName the name of the property that was not found
   */
  public EntityPropertyNotFoundException(String entityClassName, String entityPropertyName) {
    super(Message.DOMA2211, entityClassName, entityPropertyName);
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
   * Returns the name of the property that was not found.
   *
   * @return the entity property name
   */
  public String getEntityPropertyName() {
    return entityPropertyName;
  }
}
