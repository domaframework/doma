package org.seasar.doma.jdbc.entity;

import org.seasar.doma.OriginalStates;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that the field that is annotated with {@link OriginalStates} is not found in
 * an entity.
 */
public class OriginalStatesNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String entityClassName;

  private final String fieldName;

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
