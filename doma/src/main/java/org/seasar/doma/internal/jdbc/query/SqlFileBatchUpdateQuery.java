package org.seasar.doma.internal.jdbc.query;

import org.seasar.doma.internal.jdbc.Entity;

/**
 * @author taedium
 * 
 */
public class SqlFileBatchUpdateQuery<I, E extends Entity<I>> extends
        SqlFileBatchModifyQuery<I, E> implements BatchUpdateQuery {

    public SqlFileBatchUpdateQuery(Class<E> entityClass) {
        super(entityClass);
    }

    @Override
    public void incrementVersions() {
    }

}
