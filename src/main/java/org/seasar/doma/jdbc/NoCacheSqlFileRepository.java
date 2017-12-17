package org.seasar.doma.jdbc;

import java.lang.reflect.Method;

import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * An SQL file repository that does not cache the results of SQL parsing.
 */
public class NoCacheSqlFileRepository extends AbstractSqlFileRepository {

    @Override
    protected SqlFile getSqlFileWithCacheControl(Method method, String path, Dialect dialect) {
        return createSqlFile(path, dialect);
    }

}
