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
