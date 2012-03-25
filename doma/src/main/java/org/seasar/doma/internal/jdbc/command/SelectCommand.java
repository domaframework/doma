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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.query.SelectQuery;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.SqlExecutionException;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * @author taedium
 * 
 */
public class SelectCommand<R> implements Command<R, SelectQuery> {

    protected final SelectQuery query;

    protected final PreparedSql sql;

    protected final ResultSetHandler<R> resultSetHandler;

    public SelectCommand(SelectQuery query, ResultSetHandler<R> resultSetHandler) {
        assertNotNull(query, resultSetHandler);
        this.query = query;
        this.sql = query.getSql();
        this.resultSetHandler = resultSetHandler;
    }

    @Override
    public R execute() {
        Connection connection = JdbcUtil.getConnection(query.getConfig()
                .getDataSource());
        try {
            PreparedStatement preparedStatement = JdbcUtil.prepareStatement(
                    connection, sql);
            try {
                log();
                setupOptions(preparedStatement);
                bindParameters(preparedStatement);
                return executeQuery(preparedStatement);
            } catch (SQLException e) {
                Dialect dialect = query.getConfig().getDialect();
                throw new SqlExecutionException(query.getConfig()
                        .getExceptionSqlLogType(), sql, e,
                        dialect.getRootCause(e));
            } finally {
                JdbcUtil.close(preparedStatement, query.getConfig()
                        .getJdbcLogger());
            }
        } finally {
            JdbcUtil.close(connection, query.getConfig().getJdbcLogger());
        }
    }

    protected void log() {
        JdbcLogger logger = query.getConfig().getJdbcLogger();
        logger.logSql(query.getClassName(), query.getMethodName(), sql);
    }

    protected void setupOptions(PreparedStatement preparedStatement)
            throws SQLException {
        if (query.getFetchSize() > 0) {
            preparedStatement.setFetchSize(query.getFetchSize());
        }
        if (query.getMaxRows() > 0) {
            preparedStatement.setMaxRows(query.getMaxRows());
        }
        if (query.getQueryTimeout() > 0) {
            preparedStatement.setQueryTimeout(query.getQueryTimeout());
        }
    }

    protected void bindParameters(PreparedStatement preparedStatement)
            throws SQLException {
        PreparedSqlParameterBinder binder = new PreparedSqlParameterBinder(
                query);
        binder.bind(preparedStatement, sql.getParameters());
    }

    protected R executeQuery(PreparedStatement preparedStatement)
            throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        try {
            return resultSetHandler.handle(resultSet, query);
        } finally {
            JdbcUtil.close(resultSet, query.getConfig().getJdbcLogger());
        }
    }

}
