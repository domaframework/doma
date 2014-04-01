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

import java.sql.SQLException;
import java.util.function.Supplier;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.message.Message;

/**
 * @author nakamura-to
 * @since 2.0.0
 */
public abstract class AbstractJdbcLogger<LEVEL> implements JdbcLogger {

    protected LEVEL defaultLevel;

    /**
     * ログレベルを指定してインスタンスを構築します。
     */
    protected AbstractJdbcLogger(LEVEL level) {
        if (level == null) {
            throw new DomaNullPointerException("level");
        }
        this.defaultLevel = level;
    }

    /**
     * ログ出力します。
     * 
     * @param level
     *            ログレベル
     * @param callerClassName
     *            呼び出し元のクラス名
     * @param callerMethodName
     *            呼び出し元のメソッド名
     * @param messageSupplier
     *            メッセージのサプライヤ
     */
    protected abstract void log(LEVEL level, String callerClassName,
            String callerMethodName, Supplier<String> messageSupplier);

    /**
     * 例外をログ出力します。
     * 
     * @param level
     *            ログレベル
     * @param callerClassName
     *            呼び出し元のクラス名
     * @param callerMethodName
     *            呼び出し元のメソッド名
     * @param throwable
     *            例外
     * @param messageSupplier
     *            メッセージのサプライヤ
     */
    protected abstract void log(LEVEL level, String callerClassName,
            String callerMethodName, Throwable throwable,
            Supplier<String> messageSupplier);

    @Override
    public void logDaoMethodEntering(String callerClassName,
            String callerMethodName, Object... args) {
        logDaoMethodEntering(callerClassName, callerMethodName, args,
                defaultLevel, () -> "ENTRY");
    }

    protected void logDaoMethodEntering(String callerClassName,
            String callerMethodName, Object args[], LEVEL level,
            Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, messageSupplier);
    }

    @Override
    public void logDaoMethodExiting(String callerClassName,
            String callerMethodName, Object result) {
        logDaoMethodExiting(callerClassName, callerMethodName, result,
                defaultLevel, () -> "RETURN " + result);
    }

    protected void logDaoMethodExiting(String callerClassName,
            String callerMethodName, Object result, LEVEL level,
            Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, messageSupplier);
    }

    @Override
    public void logDaoMethodThrowing(String callerClassName,
            String callerMethodName, RuntimeException e) {
        logDaoMethodThrowing(callerClassName, callerMethodName, e,
                defaultLevel, () -> "THROW " + e);
    }

    protected void logDaoMethodThrowing(String callerClassName,
            String callerMethodName, RuntimeException e, LEVEL level,
            Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, messageSupplier);
    }

    @Override
    public void logSqlExecutionSkipping(String callerClassName,
            String callerMethodName, SqlExecutionSkipCause cause) {
        logSqlExecutionSkipping(callerClassName, callerMethodName, cause,
                defaultLevel, cause::name);
    }

    protected void logSqlExecutionSkipping(String callerClassName,
            String callerMethodName, SqlExecutionSkipCause cause, LEVEL level,
            Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, messageSupplier);
    }

    @Override
    public void logSql(String callerClassName, String callerMethodName,
            Sql<?> sql) {
        logSql(callerClassName,
                callerMethodName,
                sql,
                defaultLevel,
                () -> Message.DOMA2076.getMessage(sql.getSqlFilePath(),
                        sql.getFormattedSql()));
    }

    protected void logSql(String callerClassName, String callerMethodName,
            Sql<?> sql, LEVEL level, Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, messageSupplier);
    }

    @Override
    public void logTransactionBegun(String callerClassName,
            String callerMethodName, String transactionId) {
        logTransactionBegun(callerClassName, callerMethodName, transactionId,
                defaultLevel, () -> Message.DOMA2063.getMessage(transactionId));
    }

    protected void logTransactionBegun(String callerClassName,
            String callerMethodName, String transactionId, LEVEL level,
            Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, messageSupplier);
    }

    @Override
    public void logTransactionEnded(String callerClassName,
            String callerMethodName, String transactionId) {
        logTransactionEnded(callerClassName, callerMethodName, transactionId,
                defaultLevel, () -> Message.DOMA2064.getMessage(transactionId));
    }

    protected void logTransactionEnded(String callerClassName,
            String callerMethodName, String transactionId, LEVEL level,
            Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, messageSupplier);
    }

    @Override
    public void logTransactionSavepointCreated(String callerClassName,
            String callerMethodName, String transactionId, String savepointName) {
        logTransactionSavepointCreated(callerClassName, callerMethodName,
                transactionId, savepointName, defaultLevel,
                () -> Message.DOMA2065.getMessage(transactionId, savepointName));
    }

    protected void logTransactionSavepointCreated(String callerClassName,
            String callerMethodName, String transactionId,
            String savepointName, LEVEL level, Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, messageSupplier);
    }

    @Override
    public void logTransactionCommitted(String callerClassName,
            String callerMethodName, String transactionId) {
        logTransactionCommitted(callerClassName, callerMethodName,
                transactionId, defaultLevel,
                () -> Message.DOMA2067.getMessage(transactionId));
    }

    protected void logTransactionCommitted(String callerClassName,
            String callerMethodName, String transactionId, LEVEL level,
            Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, messageSupplier);
    }

    @Override
    public void logTransactionRolledback(String callerClassName,
            String callerMethodName, String transactionId) {
        logTransactionRolledback(callerClassName, callerMethodName,
                transactionId, defaultLevel,
                () -> Message.DOMA2068.getMessage(transactionId));
    }

    protected void logTransactionRolledback(String callerClassName,
            String callerMethodName, String transactionId, LEVEL level,
            Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, messageSupplier);
    }

    @Override
    public void logTransactionSavepointRolledback(String callerClassName,
            String callerMethodName, String transactionId, String savepointName) {
        logTransactionSavepointRolledback(callerClassName, callerMethodName,
                transactionId, savepointName, defaultLevel,
                () -> Message.DOMA2069.getMessage(transactionId, savepointName));
    }

    protected void logTransactionSavepointRolledback(String callerClassName,
            String callerMethodName, String transactionId,
            String savepointName, LEVEL level, Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, messageSupplier);
    }

    @Override
    public void logTransactionRollbackFailure(String callerClassName,
            String callerMethodName, String transactionId, SQLException e) {
        logTransactionRollbackFailure(callerClassName, callerMethodName, e,
                defaultLevel, () -> Message.DOMA2070.getMessage(transactionId));
    }

    protected void logTransactionRollbackFailure(String callerClassName,
            String callerMethodName, SQLException e, LEVEL level,
            Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, e, messageSupplier);
    }

    @Override
    public void logAutoCommitEnablingFailure(String callerClassName,
            String callerMethodName, SQLException e) {
        logAutoCommitEnablingFailure(callerClassName, callerMethodName, e,
                defaultLevel, () -> Message.DOMA2071.getMessage());
    }

    protected void logAutoCommitEnablingFailure(String callerClassName,
            String callerMethodName, SQLException e, LEVEL level,
            Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, e, messageSupplier);
    }

    @Override
    public void logTransactionIsolationSettingFailuer(String callerClassName,
            String callerMethodName, int transactionIsolationLevel,
            SQLException e) {
        logTransactionIsolationSettingFailuer(callerClassName,
                callerMethodName, transactionIsolationLevel, e, defaultLevel,
                () -> Message.DOMA2072.getMessage(transactionIsolationLevel));
    }

    protected void logTransactionIsolationSettingFailuer(
            String callerClassName, String callerMethodName,
            int transactionIsolationLevel, SQLException e, LEVEL level,
            Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, e, messageSupplier);
    }

    @Override
    public void logConnectionClosingFailure(String callerClassName,
            String callerMethodName, SQLException e) {
        logConnectionClosingFailure(callerClassName, callerMethodName, e,
                defaultLevel, () -> Message.DOMA2073.getMessage());
    }

    protected void logConnectionClosingFailure(String callerClassName,
            String callerMethodName, SQLException e, LEVEL level,
            Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, e, messageSupplier);
    }

    @Override
    public void logStatementClosingFailure(String callerClassName,
            String callerMethodName, SQLException e) {
        logStatementClosingFailure(callerClassName, callerMethodName, e,
                defaultLevel, () -> Message.DOMA2074.getMessage());
    }

    protected void logStatementClosingFailure(String callerClassName,
            String callerMethodName, SQLException e, LEVEL level,
            Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, e, messageSupplier);
    }

    @Override
    public void logResultSetClosingFailure(String callerClassName,
            String callerMethodName, SQLException e) {
        logResultSetClosingFailure(callerClassName, callerMethodName, e,
                defaultLevel, () -> Message.DOMA2075.getMessage());
    }

    protected void logResultSetClosingFailure(String callerClassName,
            String callerMethodName, SQLException e, LEVEL level,
            Supplier<String> messageSupplier) {
        log(level, callerClassName, callerMethodName, e, messageSupplier);
    }

}
