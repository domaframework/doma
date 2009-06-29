package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.Entity;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.util.Classes;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class EntityIterationHandler<R, I, E extends Entity<I>> implements
        ResultSetHandler<R> {

    protected final Class<E> entityClass;

    protected final IterationCallback<R, I> iterationCallback;

    public EntityIterationHandler(Class<E> entityClass,
            IterationCallback<R, I> iterationCallback) {
        assertNotNull(entityClass, iterationCallback);
        this.entityClass = entityClass;
        this.iterationCallback = iterationCallback;
    }

    @Override
    public R handle(ResultSet resultSet, Query query) throws SQLException {
        EntityFetcher fetcher = new EntityFetcher(query);
        IterationContext iterationContext = new IterationContext();
        R result = null;
        while (resultSet.next()) {
            E entity = null;
            try {
                entity = Classes.newInstance(entityClass);
            } catch (WrapException e) {
                Throwable cause = e.getCause();
                throw new JdbcException(MessageCode.DOMA2005, cause,
                        entityClass.getName(), cause);
            }
            fetcher.fetch(resultSet, entity);
            result = iterationCallback
                    .iterate(entity.__asInterface(), iterationContext);
            if (iterationContext.isExited()) {
                return result;
            }
        }
        return result;
    }

}
