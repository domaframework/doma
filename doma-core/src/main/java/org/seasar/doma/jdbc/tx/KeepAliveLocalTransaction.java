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

import javax.sql.DataSource;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcLogger;

/**
 * A local transaction that keeps a JDBC connection open until the transaction is explicitly
 * destroyed.
 *
 * <p>But the connection is closed, if any exceptions are thrown.
 *
 * <p>This instance is thread safe.
 *
 * <pre>
 * KeepAliveLocalTransaction tx = DbConfig.singleton().getKeepAliveLocalTransaction();
 * tx.init();
 * try {
 *     try {
 *         tx.begin();
 *         Employee employee = dao.selectById(1);
 *         employee.setName(&quot;SMITH&quot;);
 *         employee.setJobType(JobType.PRESIDENT);
 *         dao.update(employee);
 *         tx.commit();
 *     } finally {
 *         tx.rollback();
 *     }
 *     try {
 *         tx.begin();
 *         Employee employee = dao.selectById(2);
 *         employee.setName(&quot;foo&quot;);
 *         employee.setJobType(JobType.SALESMAN);
 *         dao.update(employee);
 *         tx.commit();
 *     } finally {
 *         tx.rollback();
 *     }
 * } finally {
 *     tx.destroy();
 * }
 * </pre>
 */
public class KeepAliveLocalTransaction extends LocalTransaction {

  /**
   * Creates an instance.
   *
   * @param dataSource the data source
   * @param localTxContextHolder the holder of the transaction context
   * @param jdbcLogger the logger
   */
  protected KeepAliveLocalTransaction(
      DataSource dataSource,
      ThreadLocal<LocalTransactionContext> localTxContextHolder,
      JdbcLogger jdbcLogger) {
    super(dataSource, localTxContextHolder, jdbcLogger);
  }

  /**
   * Creates an instance with the specified transaction isolation level.
   *
   * @param dataSource the data source
   * @param localTxContextHolder the holder of the transaction context
   * @param jdbcLogger the logger
   * @param defaultTransactionIsolationLevel the default transaction isolation level
   */
  protected KeepAliveLocalTransaction(
      DataSource dataSource,
      ThreadLocal<LocalTransactionContext> localTxContextHolder,
      JdbcLogger jdbcLogger,
      TransactionIsolationLevel defaultTransactionIsolationLevel) {
    super(dataSource, localTxContextHolder, jdbcLogger, defaultTransactionIsolationLevel);
  }

  /**
   * Initializes a transaction context.
   *
   * <p>This method establishes a JDBC connection.
   *
   * <p>If you invoke {@link #begin()} before invoking this method, the {@link #begin()} method
   * establishes a JDBC connection.
   *
   * @throws JdbcException if a JDBC related error occurs
   */
  public void init() {
    getLocalTransactionContext();
  }

  @Override
  protected LocalTransactionContext getLocalTransactionContext() {
    LocalTransactionContext context = localTxContextHolder.get();
    if (context != null) {
      return context;
    }
    return super.getLocalTransactionContext();
  }

  /**
   * Destroy the transaction context.
   *
   * <p>This method closes the JDBC connection.
   *
   * <p>This method does not throw any exceptions.
   */
  public void destroy() {
    LocalTransactionContext context = localTxContextHolder.get();
    if (context == null) {
      return;
    }
    release(context, "destroy");
  }

  /** Ends the transaction, but does not close the JDBC connection. */
  @Override
  protected void endInternal(LocalTransactionContext context, String callerMethodName) {
    jdbcLogger.logTransactionEnded(className, callerMethodName, context.getId());
  }

  @Override
  protected boolean isActiveInternal(LocalTransactionContext context) {
    return context != null && context.getId() != null;
  }
}
