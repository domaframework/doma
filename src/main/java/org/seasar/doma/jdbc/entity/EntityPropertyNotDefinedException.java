package org.seasar.doma.jdbc.entity;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that a property is not defined in an entity.
 */
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
