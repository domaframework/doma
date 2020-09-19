package org.seasar.doma.jdbc;

import java.util.Collections;
import org.junit.jupiter.api.Test;

public class UtilLoggingJdbcLoggerTest {

  @Test
  public void testLogSql() {
    PreparedSql sql =
        new PreparedSql(
            SqlKind.SELECT, "aaa", "bbb", "ccc", Collections.emptyList(), SqlLogType.FORMATTED);
    UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
    logger.logSql("ddd", "eee", sql);
  }

  @Test
  public void testLogLocalTransactionBegun() {
    UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
    logger.logTransactionBegun("ddd", "eee", "fff");
  }

  @Test
  public void testLogLocalTransactionCommitted() {
    UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
    logger.logTransactionCommitted("ddd", "eee", "fff");
  }

  @Test
  public void testLogLocalTransactionRolledback() {
    UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
    logger.logTransactionRolledback("ddd", "eee", "fff");
  }
}
