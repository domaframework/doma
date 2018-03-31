package org.seasar.doma.jdbc.tx;

import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConnection;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;

public class LocalTransactionManagerTest extends TestCase {

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

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    LocalTransactionManagerTest.counter = 0;
  }

  public void testRequired_blcok() throws Exception {
    TransactionManager manager = new LocalTransactionManager(transaction);
    var log = new StringBuilder();
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

  public void testRequired_supplier() throws Exception {
    var manager = new LocalTransactionManager(transaction);
    var result = manager.required(() -> "aaa");
    assertEquals("aaa", result);
  }

  public void testRequiresNew_blcok() throws Exception {
    TransactionManager manager = new LocalTransactionManager(transaction);
    var log = new StringBuilder();
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

  public void testRequiresNew_supplier() throws Exception {
    var manager = new LocalTransactionManager(transaction);
    var result = manager.requiresNew(() -> "aaa");
    assertEquals("aaa", result);
  }

  public void testNotSupported_block() throws Exception {
    TransactionManager manager = new LocalTransactionManager(transaction);
    var log = new StringBuilder();
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

  public void testNotSupported_supplier() throws Exception {
    var manager = new LocalTransactionManager(transaction);
    var result = manager.notSupported(() -> "aaa");
    assertEquals("aaa", result);
  }

  public void testNotSupported_in_tx() throws Exception {
    var manager = new LocalTransactionManager(transaction);
    var log = new StringBuilder();
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
