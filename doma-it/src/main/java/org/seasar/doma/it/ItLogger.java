/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.doma.it;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;

public class ItLogger implements JdbcLogger {

    @Override
    public void logConnectionClosingFailure(String callerClassName,
            String callerMethodName, SQLException e) {
    }

    @Override
    public void logStatementClosingFailure(String callerClassName,
            String callerMethodName, SQLException e) {
    }

    @Override
    public void logResultSetClosingFailure(String callerClassName,
            String callerMethodName, SQLException e) {
    }

    @Override
    public void logAutoCommitEnablingFailure(String callerClassName,
            String callerMethodName, SQLException e) {
    }

    @Override
    public void logTransactionIsolationSettingFailuer(String callerClassName,
            String callerMethodName, int transactionIsolationLevel,
            SQLException e) {
    }

    @Override
    public void logLocalTransactionRollbackFailure(String callerClassName,
            String callerMethodName, String transactionId, SQLException e) {
    }

    @Override
    public void logDaoMethodEntering(String callerClassName,
            String callerMethodName, Object... parameters) {
        Log log = LogFactory.getLog(callerClassName);
        log.info("START " + callerClassName + "#" + callerMethodName);
    }

    @Override
    public void logDaoMethodExiting(String callerClassName,
            String callerMethodName, Object result) {
        Log log = LogFactory.getLog(callerClassName);
        log.info("END   " + callerClassName + "#" + callerMethodName);
    }

    @Override
    public void logDaoMethodThrowing(String callerClassName,
            String callerMethodName, RuntimeException e) {
        Log log = LogFactory.getLog(callerClassName);
        log.info("END   " + callerClassName + "#" + callerMethodName
                + " RuntimeException: " + e);
    }

    @Override
    public void logSqlExecutionSkipping(String callerClassName,
            String callerMethodName, SqlExecutionSkipCause cause) {
        Log log = LogFactory.getLog(callerClassName);
        log.info("SKIPPED(" + cause.name() + ") " + callerClassName + "#"
                + callerMethodName);
    }

    @Override
    public void logSql(String callerClassName, String callerMethodName,
            Sql<?> sql) {
        Log log = LogFactory.getLog(callerClassName);
        String message = String.format("sqlFilePath=[%s]%n%s", sql
                .getSqlFilePath(), sql.getFormattedSql());
        log.info(message);
    }

    @Override
    public void logLocalTransactionBegun(String callerClassName,
            String callerMethodName, String transactionId) {
        Log log = LogFactory.getLog(callerClassName);
        log.info("Local transaction begun. transactionId=" + transactionId);
    }

    @Override
    public void logLocalTransactionCommitted(String callerClassName,
            String callerMethodName, String transactionId) {
        Log log = LogFactory.getLog(callerClassName);
        log.info("Local transaction committed. transactionId=" + transactionId);
    }

    @Override
    public void logLocalTransactionRolledback(String callerClassName,
            String callerMethodName, String transactionId) {
        Log log = LogFactory.getLog(callerClassName);
        log.info("Local transaction rolled back. transactionId="
                + transactionId);
    }

    @Override
    public void logLocalTransactionSavepointCreated(String callerClassName,
            String callerMethodName, String transactionId, String savepointName) {
        Log log = LogFactory.getLog(callerClassName);
        String message = String
                .format(
                        "Local transaction savepoint created. transactionId=%s savepointName=%s",
                        transactionId, transactionId);
        log.info(message);
    }

    @Override
    public void logLocalTransactionSavepointReleased(String callerClassName,
            String callerMethodName, String transactionId, String savepointName) {
        Log log = LogFactory.getLog(callerClassName);
        String message = String
                .format(
                        "Local transaction savepoint released. transactionId=%s savepointName=%s",
                        transactionId, transactionId);
        log.info(message);
    }

    @Override
    public void logLocalTransactionSavepointRolledback(String callerClassName,
            String callerMethodName, String transactionId, String savepointName) {
        Log log = LogFactory.getLog(callerClassName);
        String message = String
                .format(
                        "Local transaction savepoint rolled back. transactionId=%s savepointName=%s",
                        transactionId, transactionId);
        log.info(message);
    }

    @Override
    public void logLocalTransactionEnded(String callerClassName,
            String callerMethodName, String transactionId) {
        Log log = LogFactory.getLog(callerClassName);
        log.info("Local transaction ended. transactionId=" + transactionId);
    }

}
