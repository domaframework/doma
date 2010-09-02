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
import java.sql.SQLException;
import java.sql.Savepoint;

import javax.sql.DataSource;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.message.Message;

/**
 * ローカルトランザクションです。
 * <p>
 * このクラスはスレッドセーフです。
 * <p>
 * {@link #begin()} もしくは {@link #begin(TransactionIsolationLevel)}
 * でトランザクションを開始した後、トランザクションは必ず {@link #commit()} もしくは {@link #rollback()}
 * で終了してください。 {@code rollback()}は、{@code begin()} が成功する前や {@code commit()}
 * が成功した後に呼び出された場合、実質的には何も行いません。したがって、次のように記述できます。
 * 
 * <pre>
 * LocalTransaction tx = AppConfig.getLocalTransaction();
 * try {
 *     // 開始
 *     tx.begin();
 * 
 *     Employee employee = dao.selectById(1);
 *     employee.setName(&quot;hoge&quot;);
 *     employee.setJobType(JobType.PRESIDENT);
 *     dao.update(employee);
 * 
 *     // コミット
 *     tx.commit();
 * } finally {
 *     // ロールバック
 *     tx.rollback();
 * }
 * </pre>
 * 
 * トランザクションを終了した後、 同じインスタンスに対して {@code begin()} もしくは
 * {@code begin(TransactionIsolationLevel)} を呼び出した場合、新しいトランザクションを開始できます。
 * 
 * <pre>
 * LocalTransaction tx = AppConfig.getLocalTransaction();
 * try {
 *     // トランザクションAの開始
 *     tx.begin();
 *     ...
 *     tx.commit();
 * } finally {
 *     tx.rollback();
 * }
 * try {
 *     // トランザクションBの開始
 *     tx.begin();
 *     ...
 *     tx.commit();
 * } finally {
 *     tx.rollback();
 * }
 * </pre>
 * 
 * トランザクション開始後、このクラスのいずれかのメソッドが例外をスローした場合、 トランザクションは直ちにロールバックされます。
 * <p>
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

    /** クラス名 */
    private final String className;

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
        this.className = getClass().getName();
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
     *             トランザクションの開始に失敗した場合
     */
    public void begin() {
        beginInternal(defaultTransactionIsolationLevel, "begin");
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
     *             トランザクションの開始に失敗した場合
     */
    public void begin(TransactionIsolationLevel transactionIsolationLevel) {
        if (transactionIsolationLevel == null) {
            throw new DomaNullPointerException("transactionIsolationLevel");
        }
        beginInternal(transactionIsolationLevel, "begin");
    }

    /**
     * 内部的にローカルトランザクションを開始します。
     * 
     * @param transactionIsolationLevel
     *            トランザクション分離レベル
     * @param callerMethodName
     *            呼び出し元のメソッド名
     */
    private void beginInternal(
            TransactionIsolationLevel transactionIsolationLevel,
            String callerMethodName) {
        assertNotNull(callerMethodName);
        LocalTransactionContext context = localTxContextHolder.get();
        if (context != null) {
            rollbackInternal(callerMethodName);
            throw new LocalTransactionAlreadyBegunException(context.getId());
        }
        context = createLocalTransactionContext();
        LocalTransactionalConnection connection = context.getConnection();
        try {
            int level = connection.getTransactionIsolation();
            context.setTransactionIsolationLevel(level);
        } catch (SQLException e) {
            release(context, callerMethodName);
            throw new JdbcException(Message.DOMA2056, e, e);
        }
        if (transactionIsolationLevel != null) {
            int level = transactionIsolationLevel.getLevel();
            try {
                connection.setTransactionIsolation(level);
            } catch (SQLException e) {
                release(context, callerMethodName);
                throw new JdbcException(Message.DOMA2055, e, level, e);
            }
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            release(context, callerMethodName);
            throw new JdbcException(Message.DOMA2041, e, e);
        }
        jdbcLogger.logLocalTransactionBegun(className, callerMethodName,
                context.getId());
    }

    /**
     * ローカルトランザクションコンテキストを返します。
     * 
     * @return ローカルトランザクションコンテキスト
     */
    private LocalTransactionContext createLocalTransactionContext() {
        Connection originalConnection = JdbcUtil.getConnection(dataSource);
        LocalTransactionalConnection connection = new LocalTransactionalConnection(
                originalConnection);
        LocalTransactionContext context = new LocalTransactionContext(
                connection);
        localTxContextHolder.set(context);
        return context;
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
     *             コミットに失敗した場合
     */
    public void commit() {
        LocalTransactionContext context = localTxContextHolder.get();
        if (context == null) {
            throw new LocalTransactionNotYetBegunException(Message.DOMA2046);
        }
        LocalTransactionalConnection connection = context.getConnection();
        try {
            connection.commit();
            jdbcLogger.logLocalTransactionCommitted(className, "commit",
                    context.getId());
        } catch (SQLException e) {
            rollbackInternal("commit");
            throw new JdbcException(Message.DOMA2043, e, e);
        } finally {
            end("commit");
        }
    }

    /**
     * ローカルトランザクションをロールバックします。
     * <p>
     * ローカルトランザクションが開始されていない場合、何もおこないません。
     * <p>
     * このメソッドは、例外をスローしません。
     */
    public void rollback() {
        rollbackInternal("rollback");
    }

    /**
     * 内部的にロールバックします。
     * <p>
     * このメソッドは、実行時例外をスローしません。
     * 
     * @param callerMethodName
     *            呼び出し元のメソッド名
     */
    private void rollbackInternal(String callerMethodName) {
        assertNotNull(callerMethodName);
        LocalTransactionContext context = localTxContextHolder.get();
        if (context == null) {
            return;
        }
        LocalTransactionalConnection connection = context.getConnection();
        try {
            connection.rollback();
            jdbcLogger.logLocalTransactionRolledback(className,
                    callerMethodName, context.getId());
        } catch (SQLException ignored) {
            jdbcLogger.logLocalTransactionRollbackFailure(className,
                    callerMethodName, context.getId(), ignored);
        } finally {
            end(callerMethodName);
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
     *             セーブポイントの作成に失敗した場合
     */
    public void setSavepoint(String savepointName) {
        if (savepointName == null) {
            rollbackInternal("setSavepoint");
            throw new DomaNullPointerException("savepointName");
        }
        LocalTransactionContext context = localTxContextHolder.get();
        if (context == null) {
            throw new LocalTransactionNotYetBegunException(Message.DOMA2053,
                    savepointName);
        }
        Savepoint savepoint = context.getSavepoint(savepointName);
        if (savepoint != null) {
            rollbackInternal("setSavepoint");
            throw new SavepointAleadyExistsException(savepointName);
        }
        LocalTransactionalConnection connection = context.getConnection();
        try {
            savepoint = connection.setSavepoint(savepointName);
        } catch (SQLException e) {
            rollbackInternal("setSavepoint");
            throw new JdbcException(Message.DOMA2051, e, savepointName, e);
        }
        context.addSavepoint(savepointName, savepoint);
        jdbcLogger.logLocalTransactionSavepointCreated(className,
                "setSavepoint", context.getId(), savepointName);
    }

    /**
     * このローカルトランザクションでセーブポイントを保持しているかどうかを返します。
     * 
     * @param savepointName
     *            セーブポイントの名前
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @throws LocalTransactionNotYetBegunException
     *             ローカルトランザクションがまだ開始されていない場合
     * @return セーブポイントを保持している場合 {@code ture}
     * @since 1.2.0
     */
    public boolean hasSavepoint(String savepointName) {
        if (savepointName == null) {
            rollbackInternal("hasSavepoint");
            throw new DomaNullPointerException("savepointName");
        }
        LocalTransactionContext context = localTxContextHolder.get();
        if (context == null) {
            throw new LocalTransactionNotYetBegunException(Message.DOMA2057,
                    savepointName);
        }
        return context.getSavepoint(savepointName) != null;
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
     *             セーブポイントの削除に失敗した場合
     */
    public void releaseSavepoint(String savepointName) {
        if (savepointName == null) {
            rollbackInternal("releaseSavepoint");
            throw new DomaNullPointerException("savepointName");
        }
        LocalTransactionContext context = localTxContextHolder.get();
        if (context == null) {
            throw new LocalTransactionNotYetBegunException(Message.DOMA2061,
                    savepointName);
        }
        Savepoint savepoint = context.releaseAndGetSavepoint(savepointName);
        if (savepoint == null) {
            rollbackInternal("releaseSavepoint");
            throw new SavepointNotFoundException(savepointName);
        }
        LocalTransactionalConnection connection = context.getConnection();
        try {
            connection.releaseSavepoint(savepoint);
        } catch (SQLException e) {
            rollbackInternal("releaseSavepoint");
            throw new JdbcException(Message.DOMA2060, e, savepointName, e);
        }
        jdbcLogger.logLocalTransactionSavepointCreated(className,
                "setSavepoint", context.getId(), savepointName);
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
     *             セーブポイントへのロールバックに失敗した場合
     */
    public void rollback(String savepointName) {
        if (savepointName == null) {
            rollbackInternal("rollback");
            throw new DomaNullPointerException("savepointName");
        }
        LocalTransactionContext context = localTxContextHolder.get();
        if (context == null) {
            throw new LocalTransactionNotYetBegunException(Message.DOMA2062,
                    savepointName);
        }
        Savepoint savepoint = context.getSavepoint(savepointName);
        if (savepoint == null) {
            rollbackInternal("rollback");
            throw new SavepointNotFoundException(savepointName);
        }
        LocalTransactionalConnection connection = context.getConnection();
        try {
            connection.rollback(savepoint);
        } catch (SQLException e) {
            rollbackInternal("rollback");
            throw new JdbcException(Message.DOMA2052, e, savepointName, e);
        }
        jdbcLogger.logLocalTransactionSavepointRolledback(className,
                "rollback", context.getId(), savepointName);
    }

    /**
     * ローカルトランザクションがアクティブな場合 {@code true} を返します。
     * 
     * @return ローカルトランザクションがアクティブな場合 {@code true}
     */
    public boolean isActive() {
        return localTxContextHolder.get() != null;
    }

    /**
     * ローカルトランザクションを終了します。
     * <p>
     * このメソッドは、実行時例外をスローしません。
     * 
     * @param callerMethodName
     *            呼び出し元のメソッド名
     */
    private void end(String callerMethodName) {
        assertNotNull(callerMethodName);
        LocalTransactionContext context = localTxContextHolder.get();
        if (context == null) {
            return;
        }
        release(context, callerMethodName);
        jdbcLogger.logLocalTransactionEnded(className, callerMethodName,
                context.getId());
    }

    /**
     * トランザクションコンテキストを開放します。
     * <p>
     * このメソッドは、実行時例外をスローしません。
     * 
     * @param context
     *            トランザクションコンテキスト
     * @param callerMethodName
     *            呼び出し元のメソッド名
     */
    private void release(LocalTransactionContext context,
            String callerMethodName) {
        assertNotNull(context, callerMethodName);
        localTxContextHolder.set(null);
        LocalTransactionalConnection connection = context.getConnection();
        int isolationLevel = context.getTransactionIsolationLevel();
        if (isolationLevel != Connection.TRANSACTION_NONE) {
            try {
                connection.setTransactionIsolation(isolationLevel);
            } catch (SQLException ignored) {
                jdbcLogger.logTransactionIsolationSettingFailuer(className,
                        callerMethodName, isolationLevel, ignored);
            }
        }
        try {
            connection.setAutoCommit(true);
        } catch (SQLException ignored) {
            jdbcLogger.logAutoCommitEnablingFailure(className,
                    callerMethodName, ignored);
        }
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
