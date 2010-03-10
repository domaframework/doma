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

import javax.sql.DataSource;

import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.internal.message.Message;

/**
 * ローカルトランザクションです。
 * 
 * @author taedium
 * @since 1.1.0
 */
public class LocalTransaction {

    /** データソース */
    protected final DataSource dataSource;

    /** コネクションのホルダー */
    protected final ThreadLocal<NeverClosedConnection> connectionHolder;

    /** JDBCに関するロガー */
    protected final JdbcLogger jdbcLogger;

    /**
     * インスタンスを構築します。
     * 
     * @param dataSource
     *            データソース
     * @param connectionHolder
     *            コネクションのホルダー
     * @param jdbcLogger
     *            JDBCに関するロガー
     */
    protected LocalTransaction(DataSource dataSource,
            ThreadLocal<NeverClosedConnection> connectionHolder,
            JdbcLogger jdbcLogger) {
        assertNotNull(dataSource);
        assertNotNull(connectionHolder);
        assertNotNull(jdbcLogger);
        this.dataSource = dataSource;
        this.connectionHolder = connectionHolder;
        this.jdbcLogger = jdbcLogger;
    }

    /**
     * ローカルトランザクションを開始します。
     * <p>
     * このメソッドを呼び出した場合、{@link #commit()} もしくは {@link #rollback()}
     * を呼び出しローカルトランザクションを終了する必要があります。このとき、{@link #connectionHolder}
     * で管理されるコネクションが同一であれば、 このインスタンスとは別のインスタンスの {@link #commit()} もしくは
     * {@link #rollback()} でも構いません。
     * 
     * @throws LocalTransactionAlreadyBegunException
     *             ローカルトランザクションがすでに開始されている場合
     * @throws JdbcException
     *             JDBCに関する例外が発生した場合
     */
    public void begin() {
        if (isActiveInternal()) {
            throw new LocalTransactionAlreadyBegunException();
        }
        NeverClosedConnection connection = new NeverClosedConnection(JdbcUtil
                .getConnection(dataSource));
        connectionHolder.set(connection);
        try {
            JdbcUtil.disableAutoCommit(connection);
        } catch (JdbcException e) {
            release(connection);
            throw e;
        }
        jdbcLogger.logLocalTransactionBegun(getClass().getName(), "begin",
                getId(connection));
    }

    /**
     * ローカルトランザクションをコミットします。
     * <p>
     * このメソッドを呼び出す前に {@link #begin()} を呼び出し、ローカルトランザクションを開始しておく必要があります。 このとき、
     * {@link #connectionHolder} で管理されるコネクションが同一であれば、 このインスタンスとは別のインスタンスの
     * {@link #begin()} でも構いません。
     * 
     * @throws LocalTransactionNotYetBegunException
     *             ローカルトランザクションがまだ開始されていない場合
     * @throws JdbcException
     *             JDBCに関する例外が発生した場合
     */
    public void commit() {
        NeverClosedConnection connection = connectionHolder.get();
        if (connection == null) {
            throw new LocalTransactionNotYetBegunException(Message.DOMA2046);
        }
        try {
            JdbcUtil.commit(connectionHolder.get());
            JdbcUtil.enableAutoCommit(connectionHolder.get());
        } finally {
            release(connection);
        }
        jdbcLogger.logLocalTransactionCommitted(getClass().getName(), "commit",
                getId(connection));
    }

    /**
     * ローカルトランザクションをロールバックします。
     * <p>
     * ローカルトランザクションが開始されていない場合、何もおこないません。
     * 
     * @throws JdbcException
     *             JDBCに関する例外が発生した場合
     */
    public void rollback() {
        NeverClosedConnection connection = connectionHolder.get();
        if (connection == null) {
            return;
        }
        try {
            JdbcUtil.rollback(connectionHolder.get());
            JdbcUtil.enableAutoCommit(connectionHolder.get());
        } finally {
            release(connection);
        }
        jdbcLogger.logLocalTransactionRolledback(getClass().getName(),
                "rollback", getId(connection));
    }

    /**
     * ローカルトランザクションがアクティブな場合 {@code true} を返します。
     * 
     * @return ローカルトランザクションがアクティブな場合 {@code true}
     */
    public boolean isActive() {
        return isActiveInternal();
    }

    /**
     * ローカルトランザクションがアクティブな場合 {@code true} を返します。
     * 
     * @return ローカルトランザクションがアクティブな場合 {@code true}
     */
    protected boolean isActiveInternal() {
        return connectionHolder.get() != null;
    }

    /**
     * コネクションを開放します。
     * 
     * @param connection
     *            コネクション
     */
    protected void release(NeverClosedConnection connection) {
        connectionHolder.set(null);
        JdbcUtil.close(connection.getWrappedConnection(), jdbcLogger);
    }

    @Override
    public String toString() {
        NeverClosedConnection connection = connectionHolder.get();
        return "{LocalTransaction transactionId=" + getId(connection) + "}";
    }

    /**
     * ローカルトランザクションの識別子を返します。
     * 
     * @param connection
     *            コネクション
     * @returnローカル トランザクションの識別子、ローカルトランザクションが存在しない場合 {@code null}
     */
    protected String getId(NeverClosedConnection connection) {
        if (connection == null) {
            return null;
        }
        return String.valueOf(connection.hashCode());
    }
}
