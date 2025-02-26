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
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

  @Test
  public void testConstructor1() {
    LocalTransactionManager manager = new LocalTransactionManager(transaction);
    assertNotNull(manager);
  }

  @Test
  public void testConstructor2() {
    LocalTransactionManager manager = new LocalTransactionManager(dataSource, jdbcLogger);
    assertNotNull(manager);
  }

  @Test
  public void testConstructor3() {
    LocalTransactionManager manager =
        new LocalTransactionManager(dataSource, jdbcLogger, TransactionIsolationLevel.SERIALIZABLE);
    assertNotNull(manager);
  }
}
