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

import java.sql.Connection;
import java.sql.Savepoint;

import javax.sql.DataSource;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.internal.message.Message;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcLogger;

/**
 * ローカルトランザクションです。
 * 
 * @author taedium
 * @since 1.1.0
 */
public final class LocalTransaction {

    /** データソース */
    private final DataSource dataSource;

    /** ローカルトランザクションコンテキストのホルダー */
    private final ThreadLocal<LocalTransactionContext> localTxContextHolder;

    /** JDBCに関するロガー */
    private final JdbcLogger jdbcLogger;

    /** デフォルトのトランザクション分離レベル、指定されない場合 {@code null} */
    private final TransactionIsolationLevel defaultTransactionIsolationLevel;

    /**
     * インスタンスを構築します。
     * 
     * @param dataSource
     *            データソース
     * @param localTxContextHolder
     *            ローカルトランザクションコンテキストのホルダー
     * @param jdbcLogger
     *            JDBCに関するロガー
     */
    LocalTransaction(DataSource dataSource,
            ThreadLocal<LocalTransactionContext> localTxContextHolder,
            JdbcLogger jdbcLogger) {
        this(dataSource, localTxContextHolder, jdbcLogger, null);
    }

    /**
     * デフォルトのトランザクション分離レベルを指定してインスタンスを構築します。
     * 
     * @param dataSource
     *            データソース
     * @param localTxContextHolder
     *            ローカルトランザクションコンテキストのホルダー
     * @param jdbcLogger
     *            JDBCに関するロガー
     * @param defaultTransactionIsolationLevel
     *            デフォルトのトランザクション分離レベル
     */
    LocalTransaction(DataSource dataSource,
            ThreadLocal<LocalTransactionContext> localTxContextHolder,
            JdbcLogger jdbcLogger,
            TransactionIsolationLevel defaultTransactionIsolationLevel) {
        assertNotNull(dataSource, localTxContextHolder, jdbcLogger);
        this.dataSource = dataSource;
        this.localTxContextHolder = localTxContextHolder;
        this.jdbcLogger = jdbcLogger;
        this.defaultTransactionIsolationLevel = defaultTransactionIsolationLevel;
    }

    /**
     * ローカルトランザクションを開始します。
     * <p>
     * このメソッドを呼び出した場合、{@link #commit()} もしくは {@link #rollback()}
     * を呼び出し、ローカルトランザクションを終了する必要があります。同一スレッド内であれば、 異なるインスタンスの {@link #commit()}
     * もしくは {@link #rollback()} でも構いません。
     * 
     * @throws LocalTransactionAlreadyBegunException
     *             ローカルトランザクションがすでに開始されている場合
     * @throws JdbcException
     *             JDBCに関する例外が発生した場合
     */
    public void begin() {
        beginInternal(defaultTransactionIsolationLevel);
    }

    /**
     * トランザクション分離レベルを指定してローカルトランザクションを開始します。
     * <p>
     * このメソッドを呼び出した場合、{@link #commit()} もしくは {@link #rollback()}
     * を呼び出し、ローカルトランザクションを終了する必要があります。同一スレッド内であれば、 異なるインスタンスの {@link #commit()}
     * もしくは {@link #rollback()} でも構いません。
     * 
     * @param transactionIsolationLevel
     *            トランザクション分離レベル
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @throws LocalTransactionAlreadyBegunException
     *             ローカルトランザクションがすでに開始されている場合
     * @throws JdbcException
     *             JDBCに関する例外が発生した場合
     */
    public void begin(TransactionIsolationLevel transactionIsolationLevel) {
        if (transactionIsolationLevel == null) {
            throw new DomaNullPointerException("transactionIsolationLevel");
        }
        beginInternal(transactionIsolationLevel);
    }

    private void beginInternal(
            TransactionIsolationLevel transactionIsolationLevel) {
        if (isActiveInternal()) {
            throw new LocalTransactionAlreadyBegunException();
        }
        LocalTransactionalConnection connection = new LocalTransactionalConnection(
                JdbcUtil.getConnection(dataSource));
        try {
            LocalTransactionContext context = createLocalTransactionContext(connection);
            localTxContextHolder.set(context);
            if (transactionIsolationLevel != null) {
                JdbcUtil.setTransactionIsolation(connection,
                        transactionIsolationLevel.getLevel());
            }
            JdbcUtil.disableAutoCommit(connection);
            jdbcLogger.logLocalTransactionBegun(getClass().getName(), "begin",
                    context.getId());
        } catch (RuntimeException e) {
            releaseConnection(connection);
            throw e;
        }
    }

    private LocalTransactionContext createLocalTransactionContext(
            LocalTransactionalConnection connection) {
        assertNotNull(connection);
        int transactionIsolationLevel = JdbcUtil
                .getTransactionIsolation(connection);
        boolean autoCommit = JdbcUtil.getAutoCommit(connection);
        return new LocalTransactionContext(connection,
                transactionIsolationLevel, autoCommit);
    }

    /**
     * ローカルトランザクションをコミットします。
     * <p>
     * このメソッドを呼び出す前に {@link #begin()} または
     * {@link #begin(TransactionIsolationLevel)}
     * を呼び出し、ローカルトランザクションを開始しておく必要があります。 同一スレッド内であれば、 異なるインスタンスの
     * {@link #begin()} または {@link #begin(TransactionIsolationLevel)} でも構いません。
     * 
     * @throws LocalTransactionNotYetBegunException
     *             ローカルトランザクションがまだ開始されていない場合
     * @throws JdbcException
     *             JDBCに関する例外が発生した場合
     */
    public void commit() {
        LocalTransactionContext context = localTxContextHolder.get();
        if (context == null) {
            throw new LocalTransactionNotYetBegunException(Message.DOMA2046);
        }
        LocalTransactionalConnection connection = context.getConnection();
        try {
            JdbcUtil.commit(connection);
            restoreConnectionSettings(context, connection);
            jdbcLogger.logLocalTransactionCommitted(getClass().getName(),
                    "commit", context.getId());
        } finally {
            releaseConnection(connection);
        }
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
        LocalTransactionContext context = localTxContextHolder.get();
        if (context == null) {
            return;
        }
        LocalTransactionalConnection connection = context.getConnection();
        try {
            JdbcUtil.rollback(connection);
            restoreConnectionSettings(context, connection);
            jdbcLogger.logLocalTransactionRolledback(getClass().getName(),
                    "rollback", context.getId());
        } finally {
            releaseConnection(connection);
        }
    }

    /**
     * ローカルトランザクションのセーブポイントを作成します。
     * <p>
     * このメソッドを呼び出す前に {@link #begin()} または
     * {@link #begin(TransactionIsolationLevel)}
     * を呼び出し、ローカルトランザクションを開始しておく必要があります。 同一スレッド内であれば、 異なるインスタンスの
     * {@link #begin()} または {@link #begin(TransactionIsolationLevel)} でも構いません。
     * 
     * @param savepointName
     *            セーブポイントの名前
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @throws LocalTransactionNotYetBegunException
     *             ローカルトランザクションがまだ開始されていない場合
     * @throws SavepointAleadyExistsException
     *             セーブポイントがすでに存在する場合
     * @throws JdbcException
     *             JDBCに関する例外が発生した場合
     */
    public void setSavepoint(String savepointName) {
        LocalTransactionContext context = localTxContextHolder.get();
        if (context == null) {
            throw new LocalTransactionNotYetBegunException(Message.DOMA2053,
                    savepointName);
        }
        LocalTransactionalConnection connection = context.getConnection();
        if (savepointName == null) {
            releaseConnection(connection);
            throw new DomaNullPointerException("savepointName");
        }
        Savepoint savepoint = context.getSavepoint(savepointName);
        if (savepoint != null) {
            releaseConnection(connection);
            throw new SavepointAleadyExistsException(savepointName);
        }
        try {
            savepoint = JdbcUtil.setSavepoint(connection, savepointName);
            context.addSavepoint(savepointName, savepoint);
            jdbcLogger.logLocalTransactionSavepointCreated(
                    getClass().getName(), "setSavepoint", context.getId(),
                    savepointName);
        } catch (RuntimeException e) {
            releaseConnection(connection);
            throw e;
        }
    }

    /**
     * ローカルトランザクションから指定されたセーブポイントと以降のセーブポイントを削除します。
     * <p>
     * このメソッドを呼び出す前に {@link #begin()} または
     * {@link #begin(TransactionIsolationLevel)}
     * を呼び出し、ローカルトランザクションを開始しておく必要があります。 同一スレッド内であれば、 異なるインスタンスの
     * {@link #begin()} または {@link #begin(TransactionIsolationLevel)} でも構いません。
     * 
     * @param savepointName
     *            セーブポイントの名前
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @throws LocalTransactionNotYetBegunException
     *             ローカルトランザクションがまだ開始されていない場合
     * @throws JdbcException
     *             JDBCに関する例外が発生した場合
     */
    public void releaseSavepoint(String savepointName) {
        LocalTransactionContext context = localTxContextHolder.get();
        if (context == null) {
            throw new LocalTransactionNotYetBegunException(Message.DOMA2061,
                    savepointName);
        }
        LocalTransactionalConnection connection = context.getConnection();
        if (savepointName == null) {
            releaseConnection(connection);
            throw new DomaNullPointerException("savepointName");
        }
        Savepoint savepoint = context.releaseAndGetSavepoint(savepointName);
        if (savepoint == null) {
            releaseConnection(connection);
            throw new SavepointNotFoundException(savepointName);
        }
        try {
            JdbcUtil.releaseSavepoint(connection, savepointName, savepoint);
            jdbcLogger.logLocalTransactionSavepointCreated(
                    getClass().getName(), "setSavepoint", context.getId(),
                    savepointName);
        } catch (RuntimeException e) {
            releaseConnection(connection);
            throw e;
        }
    }

    /**
     * 指定されたセーブポイントが設定されたあとに行われたすべての変更をロールバックします。
     * <p>
     * <p>
     * このメソッドを呼び出す前に {@link #begin()} または
     * {@link #begin(TransactionIsolationLevel)}
     * を呼び出し、ローカルトランザクションを開始しておく必要があります。 同一スレッド内であれば、 異なるインスタンスの
     * {@link #begin()} または {@link #begin(TransactionIsolationLevel)} でも構いません。
     * 
     * @param savepointName
     *            セーブポイントの名前
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @throws SavepointNotFoundException
     *             セーブポイントが見つからない場合
     * @throws LocalTransactionNotYetBegunException
     *             ローカルトランザクションがまだ開始されていない場合
     * @throws JdbcException
     *             JDBCに関する例外が発生した場合
     */
    public void rollback(String savepointName) {
        LocalTransactionContext context = localTxContextHolder.get();
        if (context == null) {
            throw new LocalTransactionNotYetBegunException(Message.DOMA2062,
                    savepointName);
        }
        LocalTransactionalConnection connection = context.getConnection();
        if (savepointName == null) {
            releaseConnection(connection);
            throw new DomaNullPointerException("savepointName");
        }
        Savepoint savepoint = context.getSavepoint(savepointName);
        if (savepoint == null) {
            releaseConnection(connection);
            throw new SavepointNotFoundException(savepointName);
        }
        try {
            JdbcUtil.rollback(connection, savepointName, savepoint);
            jdbcLogger.logLocalTransactionSavepointRolledback(getClass()
                    .getName(), "rollback", context.getId(), savepointName);
        } catch (RuntimeException e) {
            releaseConnection(connection);
            throw e;
        }
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
    private boolean isActiveInternal() {
        return localTxContextHolder.get() != null;
    }

    /**
     * コネクションを元の設定に戻します。
     * 
     * @param connection
     *            コネクション
     */
    private void restoreConnectionSettings(LocalTransactionContext context,
            LocalTransactionalConnection connection) {
        assertNotNull(context, connection);
        int isolationLevel = context.getTransactionIsolationLevel();
        if (isolationLevel != Connection.TRANSACTION_NONE) {
            JdbcUtil.setTransactionIsolation(connection, isolationLevel);
        }
        JdbcUtil.setAutoCommit(connection, context.getAutoCommit());
    }

    /**
     * コネクションを開放します。
     * 
     * @param connection
     *            コネクション
     */
    private void releaseConnection(LocalTransactionalConnection connection) {
        assertNotNull(connection);
        localTxContextHolder.set(null);
        JdbcUtil.close(connection.getWrappedConnection(), jdbcLogger);
    }

    /**
     * トランザクションを識別するための文字列表現を返します。
     */
    @Override
    public String toString() {
        LocalTransactionContext context = localTxContextHolder.get();
        String transactionId = context != null ? context.getId() : "null";
        return "{LocalTransaction transactionId=" + transactionId + "}";
    }

}
