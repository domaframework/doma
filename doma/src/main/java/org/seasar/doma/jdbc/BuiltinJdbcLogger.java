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
package org.seasar.doma.jdbc;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.seasar.doma.DomaIllegalArgumentException;


/**
 * @author taedium
 * 
 */
public class BuiltinJdbcLogger implements JdbcLogger {

    protected final Level level;

    protected final Logger logger;

    public BuiltinJdbcLogger() {
        this(Level.INFO);
    }

    public BuiltinJdbcLogger(Level level) {
        if (level == null) {
            throw new DomaIllegalArgumentException("level", level);
        }
        this.level = level;
        this.logger = Logger.getLogger(BuiltinJdbcLogger.class.getName());
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
