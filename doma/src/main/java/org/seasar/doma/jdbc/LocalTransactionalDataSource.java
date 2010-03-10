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
package org.seasar.doma.jdbc;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.doma.internal.message.Message;

/**
 * ローカルトランザクションと連動するデータソースです。
 * 
 * @author taedium
 * @since 1.1.0
 */
public class LocalTransactionalDataSource implements DataSource {

    /** コネクションのホルダー */
    protected final ThreadLocal<NeverClosedConnection> connectionHolder = new ThreadLocal<NeverClosedConnection>();

    /** データソース */
    protected final DataSource dataSource;

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

    @Override
    public Connection getConnection() throws SQLException {
        return getConnectionInternal();
    }

    @Override
    public Connection getConnection(String username, String password)
            throws SQLException {
        return getConnectionInternal();
    }

    /**
     * 内部的にコネクションを返します。
     * 
     * @return コネクション
     * @throws LocalTransactionNotYetBegunException
     *             ローカルトランザクションがまだ開始されていない場合
     */
    protected Connection getConnectionInternal() {
        NeverClosedConnection connection = connectionHolder.get();
        if (connection == null) {
            throw new LocalTransactionNotYetBegunException(Message.DOMA2048);
        }
        return connection;
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
     */
    public LocalTransaction getLocalTransaction(JdbcLogger jdbcLogger) {
        return new LocalTransaction(dataSource, connectionHolder, jdbcLogger);
    }
}
