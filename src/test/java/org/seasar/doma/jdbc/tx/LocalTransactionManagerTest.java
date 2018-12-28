/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.jdbc.tx;

import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConnection;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;

/** @author nakamura-to */
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

  public void testRequired_supplier() throws Exception {
    LocalTransactionManager manager = new LocalTransactionManager(transaction);
    String result =
        manager.required(
            () -> {
              return "aaa";
            });
    assertEquals("aaa", result);
  }

  public void testRequiresNew_blcok() throws Exception {
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

  public void testRequiresNew_supplier() throws Exception {
    LocalTransactionManager manager = new LocalTransactionManager(transaction);
    String result =
        manager.requiresNew(
            () -> {
              return "aaa";
            });
    assertEquals("aaa", result);
  }

  public void testNotSupported_block() throws Exception {
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

  public void testNotSupported_supplier() throws Exception {
    LocalTransactionManager manager = new LocalTransactionManager(transaction);
    String result =
        manager.notSupported(
            () -> {
              return "aaa";
            });
    assertEquals("aaa", result);
  }

  public void testNotSupported_in_tx() throws Exception {
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
