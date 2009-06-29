package org.seasar.doma.internal.jdbc.query;

import org.seasar.doma.internal.jdbc.Entity;

/**
 * @author taedium
 * 
 */
public class SqlFileBatchDeleteQuery<I, E extends Entity<I>> extends
        SqlFileBatchModifyQuery<I, E> implements BatchDeleteQuery {

    protected boolean versionIgnored;

    protected boolean optimisticLockExceptionSuppressed;

    public SqlFileBatchDeleteQuery(Class<E> entityClass) {
        super(entityClass);
    }

}
