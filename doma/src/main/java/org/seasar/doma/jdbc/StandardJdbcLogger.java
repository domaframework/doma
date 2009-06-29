package org.seasar.doma.jdbc;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.seasar.doma.DomaIllegalArgumentException;


/**
 * @author taedium
 * 
 */
public class StandardJdbcLogger implements JdbcLogger {

    protected final Level level;

    protected final Logger logger;

    public StandardJdbcLogger() {
        this(Level.INFO);
    }

    public StandardJdbcLogger(Level level) {
        if (level == null) {
            throw new DomaIllegalArgumentException("level", level);
        }
        this.level = level;
        this.logger = Logger.getLogger(StandardJdbcLogger.class.getName());
    }

    @Override
    public void logMethodEntering(String callerClassName,
            String callerMethodName, Object... parameters) {
        if (callerClassName == null) {
            throw new DomaIllegalArgumentException("callerClassName",
                    callerClassName);
        }
        if (callerMethodName == null) {
            throw new DomaIllegalArgumentException("callerMethodName",
                    callerMethodName);
        }
        if (parameters == null) {
            throw new DomaIllegalArgumentException("parameters", parameters);
        }
        StringBuilder buf = new StringBuilder(100);
        buf.append("entering");
        logger.logp(level, callerClassName, callerMethodName, "ENTRY");

    }

    @Override
    public void logMethodExiting(String callerClassName,
            String callerMethodName, Object result) {
        if (callerClassName == null) {
            throw new DomaIllegalArgumentException("callerClassName",
                    callerClassName);
        }
        if (callerMethodName == null) {
            throw new DomaIllegalArgumentException("callerMethodName",
                    callerMethodName);
        }
        logger
                .logp(level, callerClassName, callerMethodName, "RETURN {0}", result);
    }

    @Override
    public void logSqlFile(String callerClassName, String callerMethodName,
            SqlFile sqlFile) {
        if (callerClassName == null) {
            throw new DomaIllegalArgumentException("callerClassName",
                    callerClassName);
        }
        if (callerMethodName == null) {
            throw new DomaIllegalArgumentException("callerMethodName",
                    callerMethodName);
        }
        if (sqlFile == null) {
            throw new DomaIllegalArgumentException("sqlFile", sqlFile);
        }
        logger.logp(level, callerClassName, callerMethodName, sqlFile
                .getRealPath());
    }

    @Override
    public void logSql(String callerClassName, String callerMethodName,
            Sql<?> sql) {
        if (callerClassName == null) {
            throw new DomaIllegalArgumentException("callerClassName",
                    callerClassName);
        }
        if (callerMethodName == null) {
            throw new DomaIllegalArgumentException("callerMethodName",
                    callerMethodName);
        }
        if (sql == null) {
            throw new DomaIllegalArgumentException("sql", sql);
        }
        logger.logp(level, callerClassName, callerMethodName, sql
                .getFormattedSql());
    }

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

}
