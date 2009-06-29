package org.seasar.doma.jdbc;

import java.sql.SQLException;

/**
 * @author taedium
 * 
 */
public interface JdbcLogger {

    void logMethodEntering(String callerClassName, String callerMethodName,
            Object... parameters);

    void logMethodExiting(String callerClassName, String callerMethodName,
            Object result);

    void logSqlFile(String callerClassName, String callerMethodName,
            SqlFile sqlFile);

    void logSql(String callerClassName, String callerMethodName, Sql<?> sql);

    void logConnectionClosingFailure(String callerClassName,
            String callerMethodName, SQLException e);

    void logStatementClosingFailure(String callerClassName,
            String callerMethodName, SQLException e);

    void logResultSetClosingFailure(String callerClassName,
            String callerMethodName, SQLException e);

}
