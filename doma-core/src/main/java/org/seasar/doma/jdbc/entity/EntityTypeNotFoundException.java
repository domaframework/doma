package org.seasar.doma.jdbc.entity;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/** Thrown to indicate that an entity description is not found. */
public class EntityTypeNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String entityClassName;

  private final String entityTypeClassName;

  public EntityTypeNotFoundException(
      Throwable cause, String entityClassName, String entityTypeClassName) {
    super(Message.DOMA2203, cause, entityClassName, entityTypeClassName, cause);
    this.entityClassName = entityClassName;
    this.entityTypeClassName = entityTypeClassName;
  }

  public String getEntityClassName() {
    return entityClassName;
  }

  public String getEntityTypeClassName() {
    return entityTypeClassName;
  }
}
