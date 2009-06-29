package org.seasar.doma.internal.jdbc;

import java.util.Collections;

import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;
import org.seasar.doma.jdbc.StandardJdbcLogger;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class StandardJdbcLoggerTest extends TestCase {

    public void test() throws Exception {
        PreparedSql sql = new PreparedSql("aaa", "bbb", Collections
                .<PreparedSqlParameter> emptyList());
        StandardJdbcLogger logger = new StandardJdbcLogger();
        logger.logSql("ccc", "ddd", sql);
    }
}
