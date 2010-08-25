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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import org.seasar.doma.internal.jdbc.query.ScriptQuery;
import org.seasar.doma.internal.jdbc.query.SelectQuery;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.ScriptException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlParameter;

/**
 * @author taedium
 * 
 */
public class ScriptCommand implements Command<Void, SelectQuery> {

    protected final ScriptQuery query;

    protected ScriptException savedScriptException;

    public ScriptCommand(ScriptQuery query) {
        assertNotNull(query);
        this.query = query;
    }

    @Override
    public Void execute() {
        Connection connection = JdbcUtil.getConnection(query.getConfig()
                .getDataSource());
        try {
            ScriptReader reader = new ScriptReader(query);
            try {
                for (String sqlText = reader.readSql(); sqlText != null; sqlText = reader
                        .readSql()) {
                    ScriptSql sql = new ScriptSql(sqlText,
                            query.getScriptFilePath());
                    Statement statement = JdbcUtil.createStatement(connection);
                    try {
                        log(sql);
                        setupOptions(statement);
                        statement.execute(sqlText);
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
                        JdbcUtil.close(statement, query.getConfig()
                                .getJdbcLogger());
                    }
                }
            } finally {
                reader.close();
            }
        } finally {
            JdbcUtil.close(connection, query.getConfig().getJdbcLogger());
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

    protected static class ScriptSql implements Sql<SqlParameter> {

        protected final String rawSql;

        protected final String sqlFilePath;

        public ScriptSql(String rawSql, String sqlFilePath) {
            assertNotNull(rawSql, sqlFilePath);
            this.rawSql = rawSql;
            this.sqlFilePath = sqlFilePath;
        }

        @Override
        public SqlKind getKind() {
            return SqlKind.SCRIPT;
        }

        @Override
        public String getRawSql() {
            return rawSql;
        }

        @Override
        public String getFormattedSql() {
            return rawSql;
        }

        @Override
        public String getSqlFilePath() {
            return sqlFilePath;
        }

        @Override
        public List<SqlParameter> getParameters() {
            return Collections.emptyList();
        }

    }

}
