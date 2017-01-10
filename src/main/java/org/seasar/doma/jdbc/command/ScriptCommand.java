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
package org.seasar.doma.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.function.Function;

import org.seasar.doma.internal.jdbc.command.ScriptReader;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.AbstractSql;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.ScriptException;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlParameter;
import org.seasar.doma.jdbc.query.ScriptQuery;

/**
 * @author taedium
 * 
 */
public class ScriptCommand implements Command<Void> {

    protected final ScriptQuery query;

    protected ScriptException savedScriptException;

    public ScriptCommand(ScriptQuery query) {
        assertNotNull(query);
        this.query = query;
    }

    @Override
    public Void execute() {
        Config config = query.getConfig();
        Connection connection = JdbcUtil.getConnection(config.getDataSource());
        try {
            ScriptReader reader = new ScriptReader(query);
            try {
                for (String script = reader.readSql(); script != null; script = reader
                        .readSql()) {
                    ScriptSql sql = new ScriptSql(script,
                            query.getScriptFilePath(), query.getSqlLogType(),
                            query::comment);
                    Statement statement = JdbcUtil.createStatement(connection);
                    try {
                        log(sql);
                        setupOptions(statement);
                        statement.execute(script);
                    } catch (Exception e) {
                        if (query.getHaltOnError()) {
                            throw new ScriptException(e, sql,
                                    reader.getLineNumber());
                        }
                        if (savedScriptException == null) {
                            savedScriptException = new ScriptException(e, sql,
                                    reader.getLineNumber());
                        }
                    } finally {
                        JdbcUtil.close(statement, config.getJdbcLogger());
                    }
                }
            } finally {
                reader.close();
            }
        } finally {
            JdbcUtil.close(connection, config.getJdbcLogger());
        }
        throwSavedScriptExceptionIfExists();
        return null;
    }

    protected void log(ScriptSql sql) {
        JdbcLogger logger = query.getConfig().getJdbcLogger();
        logger.logSql(query.getClassName(), query.getMethodName(), sql);
    }

    protected void setupOptions(Statement statement) throws SQLException {
        if (query.getQueryTimeout() > 0) {
            statement.setQueryTimeout(query.getQueryTimeout());
        }
    }

    protected void throwSavedScriptExceptionIfExists() {
        if (savedScriptException != null) {
            throw savedScriptException;
        }
    }

    protected static class ScriptSql extends AbstractSql<SqlParameter> {

        public ScriptSql(String rawSql, String sqlFilePath,
                SqlLogType sqlLogType, Function<String, String> converter) {
            super(SqlKind.SCRIPT, rawSql, rawSql, sqlFilePath, Collections
                    .emptyList(), sqlLogType, converter);
        }

    }

}
