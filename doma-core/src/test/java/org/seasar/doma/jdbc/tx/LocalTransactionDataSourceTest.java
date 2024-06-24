package org.seasar.doma.jdbc.tx;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;

public class LocalTransactionDataSourceTest {

  @Test
  public void testGetConnection() throws Exception {
    UtilLoggingJdbcLogger jdbcLogger = new UtilLoggingJdbcLogger();
    LocalTransactionDataSource dataSource = new LocalTransactionDataSource(new MockDataSource());
    dataSource.getLocalTransaction(jdbcLogger).begin();
    Connection connection = dataSource.getConnection();
    assertFalse(connection.getAutoCommit());
    dataSource.getLocalTransaction(jdbcLogger).commit();
  }

  @Test
  public void testGetConnection_notYetBegun() throws Exception {
    LocalTransactionDataSource dataSource = new LocalTransactionDataSource(new MockDataSource());
    Connection connection = dataSource.getConnection();
    assertTrue(connection.getAutoCommit());
  }

  @Test
  public void testIsWrapperFor() throws SQLException {
    DataSource dataSource = new LocalTransactionDataSource(new MockDataSource());
    assertTrue(dataSource.isWrapperFor(LocalTransactionDataSource.class));
    assertTrue(dataSource.isWrapperFor(MockDataSource.class));
    assertFalse(dataSource.isWrapperFor(Runnable.class));
  }

  @Test
  public void testUnwrap() throws SQLException {
    DataSource dataSource = new LocalTransactionDataSource(new MockDataSource());
    assertNotNull(dataSource.unwrap(LocalTransactionDataSource.class));
    assertNotNull(dataSource.unwrap(MockDataSource.class));
    try {
      dataSource.unwrap(Runnable.class);
      fail();
    } catch (SQLException ignored) {
    }
  }
}
