package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.Entity;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.util.Classes;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class EntityResultListHandler<I, E extends Entity<I>> implements
        ResultSetHandler<List<I>> {

    protected final Class<E> entityClass;

    public EntityResultListHandler(Class<E> entityClass) {
        assertNotNull(entityClass);
        this.entityClass = entityClass;
    }

    @Override
    public List<I> handle(ResultSet resultSet, Query query) throws SQLException {
        EntityFetcher fetcher = new EntityFetcher(query);
        List<I> entities = new ArrayList<I>();
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
            entities.add(entity.__asInterface());
        }
        return entities;
    }

}
