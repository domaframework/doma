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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.SimpleDataSource;

/**
 * A data source for local transactions.
 *
 * <p>This instance is thread safe.
 *
 * @see LocalTransaction
 */
public final class LocalTransactionDataSource implements DataSource {

  private final ThreadLocal<LocalTransactionContext> localTxContextHolder = new ThreadLocal<>();

  private final DataSource dataSource;

  /**
   * Creates an instance.
   *
   * @param dataSource the internal data source
   * @throws DomaNullPointerException if the {@code dataSource} is {@code null}
   */
  public LocalTransactionDataSource(DataSource dataSource) {
    if (dataSource == null) {
      throw new DomaNullPointerException("dataSource");
    }
    this.dataSource = dataSource;
  }

  /**
   * Creates an instance with information about the connection.
   *
   * @param url a database url
   * @param user the database user
   * @param password the user's password
   * @throws DomaNullPointerException if the {@code url} is {@code null}
   */
  public LocalTransactionDataSource(String url, String user, String password) {
    if (url == null) {
      throw new DomaNullPointerException("url");
    }
    SimpleDataSource simpleDataSource = new SimpleDataSource();
    simpleDataSource.setUrl(url);
    if (user != null) {
      simpleDataSource.setUser(user);
    }
    if (password != null) {
      simpleDataSource.setPassword(password);
    }
    this.dataSource = simpleDataSource;
  }

  /**
   * Returns a connection.
   *
   * <p>If a transaction has started, it returns the connection under transaction management; if
   * not, it returns the connection provided by the internal data source.
   *
   * @return the connection
   * @see LocalTransaction
   */
  @Override
  public Connection getConnection() {
    return getConnectionInternal();
  }

  /**
   * Returns a connection.
   *
   * <p>If a transaction has started, it returns the connection under transaction management; if
   * not, it returns the connection provided by the internal data source.
   *
   * @return the connection
   * @see LocalTransaction
   */
  @Override
  public Connection getConnection(String username, String password) {
    return getConnectionInternal();
  }

  private Connection getConnectionInternal() {
    LocalTransactionContext context = localTxContextHolder.get();
    if (context == null) {
      return JdbcUtil.getConnection(dataSource);
    }
    return context.getConnection();
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return dataSource.getLoginTimeout();
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return dataSource.getLogWriter();
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    if (iface == null) {
      return false;
    }
    if (iface.isAssignableFrom(getClass())) {
      return true;
    }
    return dataSource.isWrapperFor(iface);
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    dataSource.setLoginTimeout(seconds);
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    dataSource.setLogWriter(out);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    if (iface == null) {
      throw new SQLException("iface must not be null");
    }
    if (iface.isAssignableFrom(getClass())) {
      return (T) this;
    }
    return dataSource.unwrap(iface);
  }

  /**
   * Returns a local transaction.
   *
   * @param jdbcLogger the logger
   * @return the transaction
   * @throws DomaNullPointerException if the {@code jdbcLogger} is {@code null}
   */
  public LocalTransaction getLocalTransaction(JdbcLogger jdbcLogger) {
    if (jdbcLogger == null) {
      throw new DomaNullPointerException("jdbcLogger");
    }
    return new LocalTransaction(dataSource, localTxContextHolder, jdbcLogger);
  }

  /**
   * Returns a local transaction with the specified transaction level.
   *
   * @param jdbcLogger the logger
   * @param transactionIsolationLevel the transaction isolation level
   * @return the transaction
   * @throws DomaNullPointerException if any arguments are {@code null}
   */
  public LocalTransaction getLocalTransaction(
      JdbcLogger jdbcLogger, TransactionIsolationLevel transactionIsolationLevel) {
    if (jdbcLogger == null) {
      throw new DomaNullPointerException("jdbcLogger");
    }
    if (transactionIsolationLevel == null) {
      throw new DomaNullPointerException("transactionIsolationLevel");
    }
    return new LocalTransaction(
        dataSource, localTxContextHolder, jdbcLogger, transactionIsolationLevel);
  }

  /**
   * Returns a keep alive local transaction.
   *
   * @param jdbcLogger the logger
   * @return the transaction
   * @throws DomaNullPointerException if the {@code jdbcLogger} is {@code null}
   */
  public KeepAliveLocalTransaction getKeepAliveLocalTransaction(JdbcLogger jdbcLogger) {
    if (jdbcLogger == null) {
      throw new DomaNullPointerException("jdbcLogger");
    }
    return new KeepAliveLocalTransaction(dataSource, localTxContextHolder, jdbcLogger);
  }

  /**
   * Returns a keep alive local transaction with the specified transaction level.
   *
   * @param jdbcLogger the logger
   * @param transactionIsolationLevel the isolation level
   * @return the transaction
   * @throws DomaNullPointerException if any arguments are {@code null}
   */
  public KeepAliveLocalTransaction getKeepAliveLocalTransaction(
      JdbcLogger jdbcLogger, TransactionIsolationLevel transactionIsolationLevel) {
    if (jdbcLogger == null) {
      throw new DomaNullPointerException("jdbcLogger");
    }
    if (transactionIsolationLevel == null) {
      throw new DomaNullPointerException("transactionIsolationLevel");
    }
    return new KeepAliveLocalTransaction(
        dataSource, localTxContextHolder, jdbcLogger, transactionIsolationLevel);
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return dataSource.getParentLogger();
  }
}
