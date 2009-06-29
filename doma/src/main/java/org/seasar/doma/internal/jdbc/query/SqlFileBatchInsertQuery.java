package org.seasar.doma.internal.jdbc.query;

import java.sql.Statement;

import org.seasar.doma.internal.jdbc.Entity;


/**
 * @author taedium
 * 
 */
public class SqlFileBatchInsertQuery<I, E extends Entity<I>> extends
        SqlFileBatchModifyQuery<I, E> implements BatchInsertQuery {

    public SqlFileBatchInsertQuery(Class<E> entityClass) {
        super(entityClass);
    }

    @Override
    public boolean isBatchSupported() {
        return true;
    }

    @Override
    public void generateId(Statement statement, int index) {
    }

}
