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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBCに関する処理を記録するロガーです。
 * <p>
 * このインタフェースの実装はスレッドセーフでなければいけません。
 * <p>
 * このインタフェースのメソッドは例外をスローしてはいけません。
 * 
 * @author taedium
 * 
 */
public interface JdbcLogger {

    /**
     * Daoメソッドの実行開始を記録します。
     * 
     * @param callerClassName
     *            Daoのクラス名
     * @param callerMethodName
     *            Daoのメソッド名
     * @param args
     *            メソッドの引数
     */
    void logDaoMethodEntering(String callerClassName, String callerMethodName,
            Object... args);

    /**
     * Daoメソッドの実行終了を記録します。
     * 
     * @param callerClassName
     *            Daoのクラス名
     * @param callerMethodName
     *            Daoのメソッド名
     * @param result
     *            メソッドの実行結果、実行結果が存在しない場合{@code null}
     */
    void logDaoMethodExiting(String callerClassName, String callerMethodName,
            Object result);

    /**
     * SQLの実行がスキップされたことを記録します。
     * 
     * @param callerClassName
     *            呼び出し元のクラス名
     * @param callerMethodName
     *            呼び出し元のメソッド名
     * @param cause
     *            原因
     */
    void logSqlExecutionSkipping(String callerClassName,
            String callerMethodName, SqlExecutionSkipCause cause);

    /**
     * 実行するSQLを記録します。
     * 
     * @param callerClassName
     *            呼び出し元のクラス名
     * @param callerMethodName
     *            呼び出し元のメソッド名
     * @param sql
     *            SQL
     */
    void logSql(String callerClassName, String callerMethodName, Sql<?> sql);

    /**
     * トランザクションの開始を記録します。
     * 
     * @param callerClassName
     *            呼び出し元のクラス名
     * @param callerMethodName
     *            呼び出し元のメソッド名
     * @param transactionId
     *            トランザクションの識別子
     * @since 1.1.0
     */
    void logLocalTransactionBegun(String callerClassName,
            String callerMethodName, String transactionId);

    /**
     * トランザクションの終了を記録します。
     * 
     * @param callerClassName
     *            呼び出し元のクラス名
     * @param callerMethodName
     *            呼び出し元のメソッド名
     * @param transactionId
     *            トランザクションの識別子
     * @since 1.2.0
     */
    void logLocalTransactionEnded(String callerClassName,
            String callerMethodName, String transactionId);

    /**
     * トランザクションのコミットを記録します。
     * 
     * @param callerClassName
     *            呼び出し元のクラス名
     * @param callerMethodName
     *            呼び出し元のメソッド名
     * @param transactionId
     *            トランザクションの識別子
     * @since 1.1.0
     */
    void logLocalTransactionCommitted(String callerClassName,
            String callerMethodName, String transactionId);

    /**
     * トランザクションのセーブポイントの作成を記録します。
     * 
     * @param callerClassName
     *            呼び出し元のクラス名
     * @param callerMethodName
     *            呼び出し元のメソッド名
     * @param transactionId
     *            トランザクションの識別子
     * @param savepointName
     *            セーブポイントの名前
     * @since 1.1.0
     */
    void logLocalTransactionSavepointCreated(String callerClassName,
            String callerMethodName, String transactionId, String savepointName);

    /**
     * トランザクションのセーブポイントの削除を記録します。
     * 
     * @param callerClassName
     *            呼び出し元のクラス名
     * @param callerMethodName
     *            呼び出し元のメソッド名
     * @param transactionId
     *            トランザクションの識別子
     * @param savepointName
     *            セーブポイントの名前
     * @since 1.1.0
     */
    void logLocalTransactionSavepointReleased(String callerClassName,
            String callerMethodName, String transactionId, String savepointName);

    /**
     * トランザクションのロールバックを記録します。
     * 
     * @param callerClassName
     *            呼び出し元のクラス名
     * @param callerMethodName
     *            呼び出し元のメソッド名
     * @param transactionId
     *            トランザクションの識別子
     * @since 1.1.0
     */
    void logLocalTransactionRolledback(String callerClassName,
            String callerMethodName, String transactionId);

    /**
     * トランザクションのセーブポイントのロールバックを記録します。
     * 
     * @param callerClassName
     *            呼び出し元のクラス名
     * @param callerMethodName
     *            呼び出し元のメソッド名
     * @param transactionId
     *            トランザクションの識別子
     * @param savepointName
     *            セーブポイントの名前
     * @since 1.1.0
     */
    void logLocalTransactionSavepointRolledback(String callerClassName,
            String callerMethodName, String transactionId, String savepointName);

    /**
     * トランザクションのロールバックの失敗を記録します。
     * 
     * @param callerClassName
     *            呼び出し元のクラス名
     * @param callerMethodName
     *            呼び出し元のメソッド名
     * @param transactionId
     *            トランザクションの識別子
     * @param e
     *            {@link Connection#rollback()} 時に発生した {@link SQLException}
     * @since 1.2.0
     */
    void logLocalTransactionRollbackFailure(String callerClassName,
            String callerMethodName, String transactionId, SQLException e);

    /**
     * {@link Connection#setAutoCommit(boolean)} の引数に {@code true} を渡した時に発生した
     * {@link SQLException} を記録します。
     * 
     * @param callerClassName
     *            呼び出し元のクラス名
     * @param callerMethodName
     *            呼び出し元のメソッド名
     * @param e
     *            {@link Connection#setAutoCommit(boolean)} 時に発生した
     *            {@link SQLException}
     * @since 1.2.0
     */
    void logAutoCommitEnablingFailure(String callerClassName,
            String callerMethodName, SQLException e);

    /**
     * {@link Connection#setTransactionIsolation(int)} 時に発生した
     * {@link SQLException} を記録します。
     * 
     * @param callerClassName
     *            呼び出し元のクラス名
     * @param callerMethodName
     *            呼び出し元のメソッド名
     * @param transactionIsolationLevel
     *            {@link Connection#setTransactionIsolation(int)}
     *            で渡されるトランザクション分離レベル
     * @param e
     *            {@link Connection#setTransactionIsolation(int)} 時に発生した
     *            {@link SQLException}
     * @since 1.2.0
     */
    void logTransactionIsolationSettingFailuer(String callerClassName,
            String callerMethodName, int transactionIsolationLevel,
            SQLException e);

    /**
     * {@link Connection#close()} 時に発生した {@link SQLException} を記録します。
     * 
     * @param callerClassName
     * @param callerMethodName
     * @param e
     *            {@link Connection#close()} 時に発生した {@link SQLException}
     */
    void logConnectionClosingFailure(String callerClassName,
            String callerMethodName, SQLException e);

    /**
     * {@link Statement#close()} 時に発生した {@link SQLException} を記録します。
     * 
     * @param callerClassName
     * @param callerMethodName
     * @param e
     *            {@link Statement#close()} 時に発生した {@link SQLException}
     */
    void logStatementClosingFailure(String callerClassName,
            String callerMethodName, SQLException e);

    /**
     * {@link ResultSet#close()} 時に発生した {@link SQLException} を記録します。
     * 
     * @param callerClassName
     * @param callerMethodName
     * @param e
     *            {@link ResultSet#close()} 時に発生した {@link SQLException}
     */
    void logResultSetClosingFailure(String callerClassName,
            String callerMethodName, SQLException e);

}
