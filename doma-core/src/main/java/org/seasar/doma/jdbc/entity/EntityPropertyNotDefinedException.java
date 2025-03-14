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

/** Thrown to indicate that a property is not defined in an entity. */
public class EntityPropertyNotDefinedException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String entityClassName;

  private final String entityPropertyName;

  public EntityPropertyNotDefinedException(String entityClassName, String entityPropertyName) {
    super(Message.DOMA2207, entityClassName, entityPropertyName);
    this.entityClassName = entityClassName;
    this.entityPropertyName = entityPropertyName;
  }

  public String getEntityClassName() {
    return entityClassName;
  }

  public String getEntityPropertyName() {
    return entityPropertyName;
  }
}
