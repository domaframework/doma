/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.tx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.mock.MockConnection;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;

public class KeepAliveLocalTransactionTest {

  private final MockConnection connection = new MockConnection();

  private final LocalTransactionDataSource dataSource =
      new LocalTransactionDataSource(new MockDataSource(connection));

  private final UtilLoggingJdbcLogger jdbcLogger = new UtilLoggingJdbcLogger();

  private final KeepAliveLocalTransaction transaction =
      dataSource.getKeepAliveLocalTransaction(jdbcLogger);

  @Test
  public void testBegin() {
    transaction.begin();
    assertTrue(transaction.isActive());
    dataSource.getConnection();
    assertFalse(connection.autoCommit);
    assertEquals(TransactionIsolationLevel.READ_COMMITTED.getLevel(), connection.isolationLevel);
  }

  @Test
  public void testBeginImplicitDefaultTransactionIsolationLevel() {
    LocalTransaction transaction =
        dataSource.getKeepAliveLocalTransaction(jdbcLogger, TransactionIsolationLevel.SERIALIZABLE);
    transaction.begin();
    assertTrue(transaction.isActive());
    dataSource.getConnection();
    assertFalse(connection.autoCommit);
    assertEquals(TransactionIsolationLevel.SERIALIZABLE.getLevel(), connection.isolationLevel);
  }

  @Test
  public void testBeginWithTransactionIsolationLevel() {
    transaction.begin(TransactionIsolationLevel.SERIALIZABLE);
    assertTrue(transaction.isActive());
    dataSource.getConnection();
    assertFalse(connection.autoCommit);
    assertEquals(TransactionIsolationLevel.SERIALIZABLE.getLevel(), connection.isolationLevel);
  }

  @Test
  public void testBegin_alreadyBegun() {
    transaction.begin();
    try {
      transaction.begin();
      fail();
    } catch (TransactionAlreadyBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testBegin_failedToBegin() {
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
    LocalTransaction transaction = dataSource.getKeepAliveLocalTransaction(jdbcLogger);
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
  public void testBegin_failedToSetTransactionIsolation() {
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
    LocalTransaction transaction = dataSource.getKeepAliveLocalTransaction(jdbcLogger);
    try {
      transaction.begin(TransactionIsolationLevel.SERIALIZABLE);
      dataSource.getConnection();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(exception, expected.getCause());
    }
  }

  @Test
  public void testSetSavepoint() {
    transaction.begin();
    transaction.setSavepoint("hoge");
    assertTrue(transaction.isActive());
    assertFalse(connection.autoCommit);
    assertTrue(connection.savepointNames.contains("hoge"));
  }

  @Test
  public void testSetSavepoint_alreadyExists() {
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
  public void testSetSavepoint_notYetBegun() {
    try {
      transaction.setSavepoint("hoge");
      fail();
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testSetSavepoint_nullPointer() {
    transaction.begin();
    try {
      transaction.setSavepoint(null);
      fail();
    } catch (DomaNullPointerException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testHasSavepoint() {
    transaction.begin();
    assertFalse(transaction.hasSavepoint("hoge"));
    transaction.setSavepoint("hoge");
    assertTrue(transaction.isActive());
    assertFalse(connection.autoCommit);
    assertTrue(transaction.hasSavepoint("hoge"));
  }

  @Test
  public void testHasSavepoint_notYetBegun() {
    try {
      transaction.hasSavepoint("hoge");
      fail();
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testHasSavepoint_nullPointer() {
    transaction.begin();
    try {
      transaction.hasSavepoint(null);
      fail();
    } catch (DomaNullPointerException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testReleaseSavepoint() {
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
  public void testReleaseSavepoint_notYetBegun() {
    try {
      transaction.releaseSavepoint("hoge");
      fail();
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testReleaseSavepoint_notFound() {
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
  public void testReleaseSavepoint_nullPointer() {
    transaction.begin();
    try {
      transaction.releaseSavepoint(null);
      fail();
    } catch (DomaNullPointerException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testCommit() {
    transaction.begin();
    dataSource.getConnection();
    transaction.commit();
    assertFalse(transaction.isActive());
    assertFalse(connection.autoCommit);
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
  public void testCommit_notYetBegun() {
    try {
      transaction.commit();
      fail();
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testRollback() {
    transaction.begin();
    dataSource.getConnection();
    transaction.rollback();
    assertFalse(transaction.isActive());
    assertFalse(connection.autoCommit);
    assertTrue(connection.rolledback);
  }

  @Test
  public void testRollback_ConnectionUnused() {
    transaction.begin();
    transaction.rollback();
    assertFalse(transaction.isActive());
    assertTrue(connection.autoCommit);
    assertFalse(connection.rolledback);
  }

  @Test
  public void testRollback_notYetBegun() {
    transaction.rollback();
  }

  @Test
  public void testRollbackSavepoint() {
    transaction.begin();
    transaction.setSavepoint("hoge");
    transaction.rollback("hoge");
    assertTrue(transaction.isActive());
    assertFalse(connection.autoCommit);
    assertFalse(connection.savepointNames.contains("hoge"));
  }

  @Test
  public void testRollbackSavepoint_notYetBegun() {
    try {
      transaction.rollback("hoge");
    } catch (TransactionNotYetBegunException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testRollbackSavepoint_notFound() {
    transaction.begin();
    transaction.setSavepoint("hoge");
    try {
      transaction.rollback("foo");
      fail();
    } catch (SavepointNotFoundException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Test
  public void testInitAndDestroy() {
    assertNull(transaction.localTxContextHolder.get());
    transaction.init();
    assertNotNull(transaction.localTxContextHolder.get());
    transaction.destroy();
    assertNull(transaction.localTxContextHolder.get());
  }
}
