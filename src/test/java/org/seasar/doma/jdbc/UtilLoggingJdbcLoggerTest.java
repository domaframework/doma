package org.seasar.doma.jdbc;

import java.util.Collections;
import junit.framework.TestCase;

public class UtilLoggingJdbcLoggerTest extends TestCase {

  public void testLogSql() throws Exception {
    var sql =
        new PreparedSql(
            SqlKind.SELECT,
            "aaa",
            "bbb",
            "ccc",
            Collections.<InParameter<?>>emptyList(),
            SqlLogType.FORMATTED);
    var logger = new UtilLoggingJdbcLogger();
    logger.logSql("ddd", "eee", sql);
  }

  public void testLogLocalTransactionBegun() throws Exception {
    var logger = new UtilLoggingJdbcLogger();
    logger.logTransactionBegun("ddd", "eee", "fff");
  }

  public void testLogLocalTransactionCommitted() throws Exception {
    var logger = new UtilLoggingJdbcLogger();
    logger.logTransactionCommitted("ddd", "eee", "fff");
  }

  public void testLogLocalTransactionRolledback() throws Exception {
    var logger = new UtilLoggingJdbcLogger();
    logger.logTransactionRolledback("ddd", "eee", "fff");
  }
}
