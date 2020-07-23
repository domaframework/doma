package org.seasar.doma.jdbc.tx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.mock.MockConnection;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;

public class LocalTransactionTest {

  private final MockConnection connection = new MockConnection();

  private final LocalTransactionDataSource dataSource =
      new LocalTransactionDataSource(new MockDataSource(connection));

  private final UtilLoggingJdbcLogger jdbcLogger = new UtilLoggingJdbcLogger();

  private final LocalTransaction transaction = dataSource.getLocalTransaction(jdbcLogger);

  @Test
  public void testBegin() throws Exception {
    transaction.begin();
    assertTrue(transaction.isActive());
    dataSource.getConnection();
    assertFalse(connection.autoCommit);
    assertEquals(TransactionIsolationLevel.READ_COMMITTED.getLevel(), connection.isolationLevel);
  }

  @Test
  public void testBeginImlicitDefaultTransactionIsolationLevel() throws Exception {
    LocalTransaction transaction =
        dataSource.getLocalTransaction(jdbcLogger, TransactionIsolationLevel.SERIALIZABLE);
    transaction.begin();
    assertTrue(transaction.isActive());
    dataSource.getConnection();
    assertFalse(connection.autoCommit);
    assertEquals(TransactionIsolationLevel.SERIALIZABLE.getLevel(), connection.isolationLevel);
  }

  @Test
  public void testBeginWithTransactionIsolationLevel() throws Exception {
    transaction.begin(TransactionIsolationLevel.SERIALIZABLE);
    assertTrue(transaction.isActive());
    dataSource.getConnection();
    assertFalse(connection.autoCommit);
    assertEquals(TransactionIsolationLevel.SERIALIZABLE.getLevel(), connection.isolationLevel);
  }

  @Test
  public void testBegin_alreadyBegun() throws Exception {
    transaction.begin();
    try {
      transaction.begin();
      fail();
    } catch (TransactionAlreadyBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testBeginAndGetConnection_failedToSetAutoCommit() throws Exception {
    final SQLException exception = new SQLException();
    MockConnection connection =
        new MockConnection() {

          @Override
          public void setAutoCommit(boolean autoCommit) throws SQLException {
            throw exception;
          }
        };
    LocalTransactionDataSource dataSource =
        new LocalTransactionDataSource(new MockDataSource(connection));
    LocalTransaction transaction = dataSource.getLocalTransaction(jdbcLogger);

    try {
      transaction.begin();
      dataSource.getConnection();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(exception, expected.getCause());
    }
  }

  @Test
  public void testBegin_failedToSetTransactionIsolation() throws Exception {
    final SQLException exception = new SQLException();
    MockConnection connection =
        new MockConnection() {

          @Override
          public void setTransactionIsolation(int level) throws SQLException {
            throw exception;
          }
        };
    LocalTransactionDataSource dataSource =
        new LocalTransactionDataSource(new MockDataSource(connection));
    LocalTransaction transaction = dataSource.getLocalTransaction(jdbcLogger);

    try {
      transaction.begin(TransactionIsolationLevel.READ_COMMITTED);
      dataSource.getConnection();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(exception, expected.getCause());
    }
  }

  @Test
  public void testSetSavepoint() throws Exception {
    transaction.begin();
    transaction.setSavepoint("hoge");
    assertTrue(transaction.isActive());
    assertFalse(connection.autoCommit);
    assertTrue(connection.savepointNames.contains("hoge"));
  }

  @Test
  public void testSetSavepoint_alreadyExists() throws Exception {
    transaction.begin();
    transaction.setSavepoint("hoge");
    try {
      transaction.setSavepoint("hoge");
      fail();
    } catch (SavepointAlreadyExistsException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testSetSavepoint_notYetBegun() throws Exception {
    try {
      transaction.setSavepoint("hoge");
      fail();
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testSetSavepoint_nullPointer() throws Exception {
    transaction.begin();
    try {
      transaction.setSavepoint(null);
      fail();
    } catch (DomaNullPointerException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testHasSavepoint() throws Exception {
    transaction.begin();
    assertFalse(transaction.hasSavepoint("hoge"));
    transaction.setSavepoint("hoge");
    assertTrue(transaction.isActive());
    assertFalse(connection.autoCommit);
    assertTrue(transaction.hasSavepoint("hoge"));
  }

  @Test
  public void testHasSavepoint_notYetBegun() throws Exception {
    try {
      transaction.hasSavepoint("hoge");
      fail();
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testHasSavepoint_nullPointer() throws Exception {
    transaction.begin();
    try {
      transaction.hasSavepoint(null);
      fail();
    } catch (DomaNullPointerException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testReleaseSavepoint() throws Exception {
    transaction.begin();
    transaction.setSavepoint("hoge");
    transaction.setSavepoint("foo");
    transaction.setSavepoint("bar");
    transaction.releaseSavepoint("foo");
    assertTrue(transaction.isActive());
    assertFalse(connection.autoCommit);
    assertFalse(connection.savepointNames.contains("hoge"));
    assertFalse(connection.savepointNames.contains("foo"));
    assertTrue(connection.savepointNames.contains("bar"));
  }

  @Test
  public void testReleaseSavepoint_notYetBegun() throws Exception {
    try {
      transaction.releaseSavepoint("hoge");
      fail();
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testReleaseSavepoint_notFound() throws Exception {
    transaction.begin();
    transaction.setSavepoint("hoge");
    try {
      transaction.releaseSavepoint("foo");
      fail();
    } catch (SavepointNotFoundException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testReleaseSavepoint_nullPointer() throws Exception {
    transaction.begin();
    try {
      transaction.releaseSavepoint(null);
      fail();
    } catch (DomaNullPointerException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testCommit() throws Exception {
    transaction.begin();
    dataSource.getConnection();
    transaction.commit();
    assertFalse(transaction.isActive());
    assertTrue(connection.autoCommit);
    assertTrue(connection.committed);
  }

  @Test
  public void testCommit_ConnectionUnused() {
    transaction.begin();
    transaction.commit();
    assertFalse(transaction.isActive());
    assertTrue(connection.autoCommit);
    assertFalse(connection.committed);
  }

  @Test
  public void testCommit_notYetBegun() throws Exception {
    try {
      transaction.commit();
      fail();
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testRollback() throws Exception {
    transaction.begin();
    dataSource.getConnection();
    transaction.rollback();
    assertFalse(transaction.isActive());
    assertTrue(connection.autoCommit);
    assertTrue(connection.rolledback);
  }

  @Test
  public void testRollback_ConnectionUnused() throws Exception {
    transaction.begin();
    transaction.rollback();
    assertFalse(transaction.isActive());
    assertTrue(connection.autoCommit);
    assertFalse(connection.rolledback);
  }

  @Test
  public void testRollback_notYetBegun() throws Exception {
    transaction.rollback();
  }

  @Test
  public void testRollbackSavepoint() throws Exception {
    transaction.begin();
    transaction.setSavepoint("hoge");
    transaction.rollback("hoge");
    assertTrue(transaction.isActive());
    assertFalse(connection.autoCommit);
    assertFalse(connection.savepointNames.contains("hoge"));
  }

  @Test
  public void testRollbackSavepoint_notYetBegun() throws Exception {
    try {
      transaction.rollback("hoge");
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testRollbackSavepoint_notFound() throws Exception {
    transaction.begin();
    transaction.setSavepoint("hoge");
    try {
      transaction.rollback("foo");
      fail();
    } catch (SavepointNotFoundException expected) {
      System.out.println(expected.getMessage());
    }
  }
}
