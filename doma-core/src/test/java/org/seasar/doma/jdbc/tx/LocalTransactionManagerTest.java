package org.seasar.doma.jdbc.tx;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConnection;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;

public class LocalTransactionManagerTest {

  static int counter;

  private final MockConnection connection = new MockConnection();

  private final LocalTransactionDataSource dataSource =
      new LocalTransactionDataSource(new MockDataSource(connection));

  private final UtilLoggingJdbcLogger jdbcLogger =
      new UtilLoggingJdbcLogger() {

        @Override
        public void logTransactionBegun(
            String callerClassName, String callerMethodName, String transactionId) {
          LocalTransactionManagerTest.counter++;
        }

        @Override
        public void logTransactionEnded(
            String callerClassName, String callerMethodName, String transactionId) {
          LocalTransactionManagerTest.counter--;
        }
      };

  private final LocalTransaction transaction = dataSource.getLocalTransaction(jdbcLogger);

  @BeforeEach
  protected void setUp() {
    LocalTransactionManagerTest.counter = 0;
  }

  @Test
  public void testRequired_block() {
    TransactionManager manager = new LocalTransactionManager(transaction);
    StringBuilder log = new StringBuilder();
    log.append(LocalTransactionManagerTest.counter);
    manager.required(
        () -> {
          log.append(LocalTransactionManagerTest.counter);
          manager.required(
              () -> {
                log.append(LocalTransactionManagerTest.counter);
              });
          log.append(LocalTransactionManagerTest.counter);
        });
    log.append(LocalTransactionManagerTest.counter);
    assertEquals("01110", log.toString());
  }

  @Test
  public void testRequired_supplier() {
    LocalTransactionManager manager = new LocalTransactionManager(transaction);
    String result = manager.required(() -> "aaa");
    assertEquals("aaa", result);
  }

  @Test
  public void testRequiresNew_block() {
    TransactionManager manager = new LocalTransactionManager(transaction);
    StringBuilder log = new StringBuilder();
    log.append(LocalTransactionManagerTest.counter);
    manager.requiresNew(
        () -> {
          log.append(LocalTransactionManagerTest.counter);
          manager.requiresNew(
              () -> {
                log.append(LocalTransactionManagerTest.counter);
              });
          log.append(LocalTransactionManagerTest.counter);
        });
    log.append(LocalTransactionManagerTest.counter);
    assertEquals("01210", log.toString());
  }

  @Test
  public void testRequiresNew_supplier() {
    LocalTransactionManager manager = new LocalTransactionManager(transaction);
    String result = manager.requiresNew(() -> "aaa");
    assertEquals("aaa", result);
  }

  @Test
  public void testNotSupported_block() {
    TransactionManager manager = new LocalTransactionManager(transaction);
    StringBuilder log = new StringBuilder();
    log.append(LocalTransactionManagerTest.counter);
    manager.notSupported(
        () -> {
          log.append(LocalTransactionManagerTest.counter);
          manager.notSupported(
              () -> {
                log.append(LocalTransactionManagerTest.counter);
              });
          log.append(LocalTransactionManagerTest.counter);
        });
    log.append(LocalTransactionManagerTest.counter);
    assertEquals("00000", log.toString());
  }

  @Test
  public void testNotSupported_supplier() {
    LocalTransactionManager manager = new LocalTransactionManager(transaction);
    String result = manager.notSupported(() -> "aaa");
    assertEquals("aaa", result);
  }

  @Test
  public void testNotSupported_in_tx() {
    LocalTransactionManager manager = new LocalTransactionManager(transaction);
    StringBuilder log = new StringBuilder();
    log.append(LocalTransactionManagerTest.counter);
    manager.required(
        () -> {
          log.append(LocalTransactionManagerTest.counter);
          manager.notSupported(
              () -> {
                log.append(LocalTransactionManagerTest.counter);
              });
          log.append(LocalTransactionManagerTest.counter);
        });
    log.append(LocalTransactionManagerTest.counter);
    assertEquals("01110", log.toString());
  }
}
