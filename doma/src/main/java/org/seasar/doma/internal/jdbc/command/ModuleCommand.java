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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.query.ModuleQuery;
import org.seasar.doma.internal.jdbc.sql.CallableSql;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public abstract class ModuleCommand<R, Q extends ModuleQuery> implements
        Command<R, Q> {

    protected final Q query;

    protected CallableSql sql;

    public ModuleCommand(Q query) {
        assertNotNull(query);
        this.query = query;
        this.sql = query.getSql();
    }

    @Override
    public R execute() {
        Connection connection = Jdbcs.getConnection(query.getConfig()
                .dataSource());
        try {
            CallableStatement callableStatement = Jdbcs
                    .prepareCall(connection, sql.getRawSql());
            try {
                log();
                setupOptions(callableStatement);
                bindParameters(callableStatement);
                return executeInternal(callableStatement);
            } catch (SQLException e) {
                throw new JdbcException(MessageCode.DOMA2009, e, e);
            } finally {
                Jdbcs.close(callableStatement, query.getConfig().jdbcLogger());
            }
        } finally {
            Jdbcs.close(connection, query.getConfig().jdbcLogger());
        }
    }

    protected abstract R executeInternal(CallableStatement callableStatement)
            throws SQLException;

    protected void setupOptions(CallableStatement preparedStatement)
            throws SQLException {
        if (query.getQueryTimeout() > 0) {
            preparedStatement.setQueryTimeout(query.getQueryTimeout());
        }
    }

    protected void bindParameters(CallableStatement callableStatement)
            throws SQLException {
        CallableSqlParameterBinder binder = new CallableSqlParameterBinder(
                query);
        binder.bind(callableStatement, sql.getParameters());
    }

    protected void fetchParameters(CallableStatement callableStatement)
            throws SQLException {
        CallableSqlParameterFetcher fetcher = new CallableSqlParameterFetcher(
                query, callableStatement, sql.getParameters());
        fetcher.fetch();
    }

    protected void log() {
        query.getConfig().jdbcLogger().logSql(query.getClassName(), query
                .getMethodName(), sql);
    }

}
