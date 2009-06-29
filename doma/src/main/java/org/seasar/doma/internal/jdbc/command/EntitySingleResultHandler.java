package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.Entity;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.util.Classes;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.NonUniqueResultException;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class EntitySingleResultHandler<I, E extends Entity<I>> implements
        ResultSetHandler<I> {

    protected final Class<E> entityClass;

    public EntitySingleResultHandler(Class<E> entityClass) {
        assertNotNull(entityClass);
        this.entityClass = entityClass;
    }

    @Override
    public I handle(ResultSet resultSet, Query query) throws SQLException {
        EntityFetcher fetcher = new EntityFetcher(query);
        E entity = null;
        if (resultSet.next()) {
            try {
                entity = Classes.newInstance(entityClass);
            } catch (WrapException e) {
                Throwable cause = e.getCause();
                throw new JdbcException(MessageCode.DOMA2005, cause,
                        entityClass.getName(), cause);
            }
            fetcher.fetch(resultSet, entity);
            if (resultSet.next()) {
                throw new NonUniqueResultException(query.getSql());
            }
            return entity.__asInterface();
        }
        return null;
    }

}
