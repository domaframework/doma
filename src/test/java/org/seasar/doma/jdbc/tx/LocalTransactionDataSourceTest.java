package org.seasar.doma.jdbc.tx;

import java.sql.SQLException;
import javax.sql.DataSource;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;

public class LocalTransactionDataSourceTest extends TestCase {

  public void testGetConnection() throws Exception {
    var jdbcLogger = new UtilLoggingJdbcLogger();
    var dataSource = new LocalTransactionDataSource(new MockDataSource());
    dataSource.getLocalTransaction(jdbcLogger).begin();
    dataSource.getConnection();
    dataSource.getLocalTransaction(jdbcLogger).commit();
  }

  public void testGetConnection_notYetBegun() throws Exception {
    var dataSource = new LocalTransactionDataSource(new MockDataSource());
    try {
      dataSource.getConnection();
      fail();
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  public void testIsWrapperFor() throws Exception {
    DataSource dataSource = new LocalTransactionDataSource(new MockDataSource());
    assertTrue(dataSource.isWrapperFor(LocalTransactionDataSource.class));
    assertTrue(dataSource.isWrapperFor(MockDataSource.class));
    assertFalse(dataSource.isWrapperFor(Runnable.class));
  }

  public void testUnwrap() throws Exception {
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
