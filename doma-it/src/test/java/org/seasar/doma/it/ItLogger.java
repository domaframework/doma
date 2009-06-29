package org.seasar.doma.it;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import doma.jdbc.JdbcLogger;
import doma.jdbc.Sql;
import doma.jdbc.SqlFile;

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
    public void logMethodEntering(String callerClassName,
            String callerMethodName, Object... parameters) {
        Log log = LogFactory.getLog(callerClassName);
        log.info("entering " + callerClassName + "#" + callerMethodName);
    }

    @Override
    public void logMethodExiting(String callerClassName,
            String callerMethodName, Object result) {
        Log log = LogFactory.getLog(callerClassName);
        log.info("exiting " + callerClassName + "#" + callerMethodName);
    }

    @Override
    public void logSql(String callerClassName, String callerMethodName,
            Sql<?> sql) {
        Log log = LogFactory.getLog(callerMethodName);
        log.info(sql.getFormattedSql());
    }

    @Override
    public void logSqlFile(String callerClassName, String callerMethodName,
            SqlFile sqlFile) {
        Log log = LogFactory.getLog(callerMethodName);
        log.info(sqlFile.getRealPath());
    }

}
