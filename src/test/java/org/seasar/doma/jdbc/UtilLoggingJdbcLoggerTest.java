package org.seasar.doma.jdbc;

import java.util.Collections;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class UtilLoggingJdbcLoggerTest extends TestCase {

    public void testLogSql() throws Exception {
        PreparedSql sql = new PreparedSql(SqlKind.SELECT, "aaa", "bbb", "ccc",
                Collections.<InParameter<?>> emptyList(), SqlLogType.FORMATTED);
        UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
        logger.logSql("ddd", "eee", sql);
    }

    public void testLogLocalTransactionBegun() throws Exception {
        UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
        logger.logTransactionBegun("ddd", "eee", "fff");
    }

    public void testLogLocalTransactionCommitted() throws Exception {
        UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
        logger.logTransactionCommitted("ddd", "eee", "fff");
    }

    public void testLogLocalTransactionRolledback() throws Exception {
        UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
        logger.logTransactionRolledback("ddd", "eee", "fff");
    }
}
