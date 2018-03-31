package org.seasar.doma.jdbc.entity;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/** Thrown to indicate that an entity description is not found. */
public class EntityDescNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String entityClassName;

  private final String entityDescClassName;

  public EntityDescNotFoundException(
      Throwable cause, String entityClassName, String entityDescClassName) {
    super(Message.DOMA2203, cause, entityClassName, entityDescClassName, cause);
    this.entityClassName = entityClassName;
    this.entityDescClassName = entityDescClassName;
  }

  public String getEntityClassName() {
    return entityClassName;
  }

  public String getEntityDescClassName() {
    return entityDescClassName;
  }
}
