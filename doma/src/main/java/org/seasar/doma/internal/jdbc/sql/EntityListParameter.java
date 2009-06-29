package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.Assertions.*;

import java.util.List;

import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.Entity;
import org.seasar.doma.internal.util.Classes;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class EntityListParameter<I, E extends Entity<I>> implements
        ListParameter<E> {

    protected final Class<E> entityClass;

    protected final List<I> entities;

    public EntityListParameter(Class<E> entityClass, List<I> entities) {
        assertNotNull(entityClass, entities);
        this.entityClass = entityClass;
        this.entities = entities;
    }

    public E add() {
        E entity = createEntity();
        entities.add(entity.__asInterface());
        return entity;
    }

    protected E createEntity() {
        try {
            return Classes.newInstance(entityClass);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new JdbcException(MessageCode.DOMA2005, cause, entityClass
                    .getName(), cause);
        }
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitEntityListParameter(this, p);
    }

}
