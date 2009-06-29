package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.Assertions.*;

import java.util.ArrayList;
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
public class EntityListResultParameter<I, E extends Entity<I>> implements
        ResultParameter<List<I>>, ListParameter<E> {

    protected final Class<E> entityClass;

    protected final List<I> entities = new ArrayList<I>();

    public EntityListResultParameter(Class<E> entityClass) {
        assertNotNull(entityClass);
        this.entityClass = entityClass;
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
    public List<I> getResult() {
        return entities;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitEntityListResultParameter(this, p);
    }

}
