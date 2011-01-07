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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.message.Message;

/**
 * 出力先に {@link java.util.logging.Logger} を使用したJDBCロガーです。
 * <p>
 * 
 * @author taedium
 * 
 */
public class UtilLoggingJdbcLogger implements JdbcLogger {

    /** ログレベルです。 */
    protected final Level level;

    /** このインスタンスで使用するロガーです。 */
    protected final Logger logger;

    /**
     * インスタンスを構築します。
     * <p>
     * ログレベルは {@link Level#INFO} になります。
     */
    public UtilLoggingJdbcLogger() {
        this(Level.INFO);
    }

    /**
     * ログレベルを指定してインスタンスを構築します。
     * 
     * @param level
     *            ログレベル
     */
    public UtilLoggingJdbcLogger(Level level) {
        if (level == null) {
            throw new DomaNullPointerException("level");
        }
        this.level = level;
        this.logger = Logger.getLogger(UtilLoggingJdbcLogger.class.getName());
    }

    @Override
    public void logDaoMethodEntering(String callerClassName,
            String callerMethodName, Object... args) {
        logger.logp(level, callerClassName, callerMethodName, "ENTRY");

    }

    @Override
    public void logDaoMethodExiting(String callerClassName,
            String callerMethodName, Object result) {
        logger.logp(level, callerClassName, callerMethodName, "RETURN {0}",
                result);
    }

    @Override
    public void logDaoMethodThrowing(String callerClassName,
            String callerMethodName, RuntimeException e) {
        logger.logp(level, callerClassName, callerMethodName, "THROW {0}",
                e.toString());
    }

    @Override
    public void logSqlExecutionSkipping(String callerClassName,
            String callerMethodName, SqlExecutionSkipCause cause) {
        logger.logp(level, callerClassName, callerMethodName, cause.name());
    }

    @Override
    public void logSql(String callerClassName, String callerMethodName,
            Sql<?> sql) {
        logger.logp(
                level,
                callerClassName,
                callerMethodName,
                Message.DOMA2076.getMessage(sql.getSqlFilePath(),
                        sql.getFormattedSql()));
    }

    @Override
    public void logLocalTransactionBegun(String callerClassName,
            String callerMethodName, String transactionId) {
        logger.logp(level, callerClassName, callerMethodName,
                Message.DOMA2063.getMessage(transactionId));
    }

    @Override
    public void logLocalTransactionEnded(String callerClassName,
            String callerMethodName, String transactionId) {
        logger.logp(level, callerClassName, callerMethodName,
                Message.DOMA2064.getMessage(transactionId));
    }

    @Override
    public void logLocalTransactionSavepointCreated(String callerClassName,
            String callerMethodName, String transactionId, String savepointName) {
        logger.logp(level, callerClassName, callerMethodName,
                Message.DOMA2065.getMessage(transactionId, savepointName));
    }

    @Override
    public void logLocalTransactionSavepointReleased(String callerClassName,
            String callerMethodName, String transactionId, String savepointName) {
        logger.logp(level, callerClassName, callerMethodName,
                Message.DOMA2066.getMessage(transactionId, savepointName));
    }

    @Override
    public void logLocalTransactionCommitted(String callerClassName,
            String callerMethodName, String transactionId) {
        logger.logp(level, callerClassName, callerMethodName,
                Message.DOMA2067.getMessage(transactionId));
    }

    @Override
    public void logLocalTransactionRolledback(String callerClassName,
            String callerMethodName, String transactionId) {
        logger.logp(level, callerClassName, callerMethodName,
                Message.DOMA2068.getMessage(transactionId));
    }

    @Override
    public void logLocalTransactionSavepointRolledback(String callerClassName,
            String callerMethodName, String transactionId, String savepointName) {
        logger.logp(level, callerClassName, callerMethodName,
                Message.DOMA2069.getMessage(transactionId, savepointName));

    }

    @Override
    public void logLocalTransactionRollbackFailure(String callerClassName,
            String callerMethodName, String transactionId, SQLException e) {
        logger.logp(level, callerClassName, callerMethodName,
                Message.DOMA2070.getMessage(transactionId));
    }

    @Override
    public void logAutoCommitEnablingFailure(String callerClassName,
            String callerMethodName, SQLException e) {
        logger.logp(level, callerClassName, callerMethodName,
                Message.DOMA2071.getMessage(), e);
    }

    @Override
    public void logTransactionIsolationSettingFailuer(String callerClassName,
            String callerMethodName, int transactionIsolationLevel,
            SQLException e) {
        logger.logp(level, callerClassName, callerMethodName,
                Message.DOMA2072.getMessage(transactionIsolationLevel), e);
    }

    @Override
    public void logConnectionClosingFailure(String callerClassName,
            String callerMethodName, SQLException e) {
        logger.logp(level, callerClassName, callerMethodName,
                Message.DOMA2073.getMessage(), e);
    }

    @Override
    public void logStatementClosingFailure(String callerClassName,
            String callerMethodName, SQLException e) {
        logger.logp(level, callerClassName, callerMethodName,
                Message.DOMA2074.getMessage(), e);
    }

    @Override
    public void logResultSetClosingFailure(String callerClassName,
            String callerMethodName, SQLException e) {
        logger.logp(level, callerClassName, callerMethodName,
                Message.DOMA2075.getMessage(), e);
    }

}
