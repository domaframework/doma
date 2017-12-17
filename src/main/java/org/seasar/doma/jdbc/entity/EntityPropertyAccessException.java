package org.seasar.doma.jdbc.entity;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that the access to an entity property is failed.
 */
public class EntityPropertyAccessException extends JdbcException {

    private static final long serialVersionUID = 1L;

    private final String entityClassName;

    private final String entityPropertyName;

    public EntityPropertyAccessException(Throwable cause, String entityClassName,
            String entityPropertyName) {
        super(Message.DOMA2208, cause, entityClassName, entityPropertyName, cause);
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
