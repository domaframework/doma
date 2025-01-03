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
 * Thrown to indicate that the access to the filed that is annotated with {@link OriginalStates} is
 * failed.
 */
public class OriginalStatesAccessException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String entityClassName;

  private final String fieldName;

  public OriginalStatesAccessException(Throwable cause, String entityClassName, String fieldName) {
    super(Message.DOMA2212, cause, entityClassName, fieldName, cause);
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
