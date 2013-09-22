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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.util.DataSourceUtil;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.message.Message;

/**
 * ローカルトランザクションと連動するデータソースです。
 * <p>
 * このクラスはスレッドセーフです。
 * 
 * @see LocalTransaction
 * @author taedium
 * @since 1.1.0
 */
public final class LocalTransactionalDataSource implements DataSource {

    /** コネクションのホルダー */
    private final ThreadLocal<LocalTransactionContext> localTxContextHolder = new ThreadLocal<LocalTransactionContext>();

    /** データソース */
    private final DataSource dataSource;

    /**
     * インスタンスを構築します。
     * 
     * @param dataSource
     *            データソース
     */
    public LocalTransactionalDataSource(DataSource dataSource) {
        assertNotNull(dataSource);
        this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     * <p>
     * このメソッドを実行する前にローカルトランザクションを開始しておかなければいけません。
     * 
     * @see LocalTransaction
     * @throws LocalTransactionNotYetBegunException
     *             ローカルトランザクションがまだ開始されていない場合
     */
    @Override
    public Connection getConnection() throws SQLException {
        return getConnectionInternal();
    }

    /**
     * {@inheritDoc}
     * <p>
     * このメソッドを実行する前にローカルトランザクションを開始しておかなければいけません。
     * 
     * @see LocalTransaction
     * @throws LocalTransactionNotYetBegunException
     *             ローカルトランザクションがまだ開始されていない場合
     */
    @Override
    public Connection getConnection(String username, String password)
            throws SQLException {
        return getConnectionInternal();
    }

    private Connection getConnectionInternal() {
        LocalTransactionContext context = localTxContextHolder.get();
        if (context == null) {
            throw new LocalTransactionNotYetBegunException(Message.DOMA2048);
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

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return dataSource.unwrap(iface);
    }

    /**
     * ローカルトランザクションを返します。
     * 
     * @param jdbcLogger
     *            JDBCに関するロガー
     * @return ローカルトランザクション
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     */
    public LocalTransaction getLocalTransaction(JdbcLogger jdbcLogger) {
        if (jdbcLogger == null) {
            throw new DomaNullPointerException("jdbcLogger");
        }
        return new LocalTransaction(dataSource, localTxContextHolder,
                jdbcLogger);
    }

    /**
     * デフォルトのトランザクション分離レベルを指定してローカルトランザクションを返します。
     * 
     * @param jdbcLogger
     *            JDBCに関するロガー
     * @param transactionIsolationLevel
     *            デフォルトのトランザクション分離レベル
     * @return ローカルトランザクション
     * @throws DomaNullPointerException
     *             引数のいずれかが {@code null} の場合
     */
    public LocalTransaction getLocalTransaction(JdbcLogger jdbcLogger,
            TransactionIsolationLevel transactionIsolationLevel) {
        if (jdbcLogger == null) {
            throw new DomaNullPointerException("jdbcLogger");
        }
        if (transactionIsolationLevel == null) {
            throw new DomaNullPointerException("transactionIsolationLevel");
        }
        return new LocalTransaction(dataSource, localTxContextHolder,
                jdbcLogger, transactionIsolationLevel);
    }

    /**
     * 明示的に破棄されるまで接続を維持し続けるローカルトランザクションを返します。
     * 
     * @param jdbcLogger
     *            JDBCに関するロガー
     * @return ローカルトランザクション
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     */
    public KeepAliveLocalTransaction getKeepAliveLocalTransaction(
            JdbcLogger jdbcLogger) {
        if (jdbcLogger == null) {
            throw new DomaNullPointerException("jdbcLogger");
        }
        return new KeepAliveLocalTransaction(dataSource, localTxContextHolder,
                jdbcLogger);
    }

    /**
     * デフォルトのトランザクション分離レベルを指定して、明示的に破棄されるまで接続を維持し続けるローカルトランザクションを返します。
     * 
     * @param jdbcLogger
     *            JDBCに関するロガー
     * @param transactionIsolationLevel
     *            デフォルトのトランザクション分離レベル
     * @return ローカルトランザクション
     * @throws DomaNullPointerException
     *             引数のいずれかが {@code null} の場合
     */
    public KeepAliveLocalTransaction getKeepAliveLocalTransaction(
            JdbcLogger jdbcLogger,
            TransactionIsolationLevel transactionIsolationLevel) {
        if (jdbcLogger == null) {
            throw new DomaNullPointerException("jdbcLogger");
        }
        if (transactionIsolationLevel == null) {
            throw new DomaNullPointerException("transactionIsolationLevel");
        }
        return new KeepAliveLocalTransaction(dataSource, localTxContextHolder,
                jdbcLogger, transactionIsolationLevel);
    }

    @SuppressWarnings("all")
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return DataSourceUtil.getParentLogger(dataSource);
    }
}
