package org.seasar.doma.jdbc.entity;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/** Thrown to indicate that a property is not found in an entity. */
public class EntityPropertyNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String entityClassName;

  private final String entityPropertyName;

  public EntityPropertyNotFoundException(String entityClassName, String entityPropertyName) {
    super(Message.DOMA2211, entityClassName, entityPropertyName);
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
