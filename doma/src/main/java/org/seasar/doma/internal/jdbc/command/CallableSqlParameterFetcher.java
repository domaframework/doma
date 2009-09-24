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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.jdbc.sql.CallableSqlParameter;
import org.seasar.doma.internal.jdbc.sql.CallableSqlParameterVisitor;
import org.seasar.doma.internal.jdbc.sql.EntityListParameter;
import org.seasar.doma.internal.jdbc.sql.EntityListResultParameter;
import org.seasar.doma.internal.jdbc.sql.InOutParameter;
import org.seasar.doma.internal.jdbc.sql.InParameter;
import org.seasar.doma.internal.jdbc.sql.ListParameter;
import org.seasar.doma.internal.jdbc.sql.OutParameter;
import org.seasar.doma.internal.jdbc.sql.ValueListParameter;
import org.seasar.doma.internal.jdbc.sql.ValueListResultParameter;
import org.seasar.doma.internal.jdbc.sql.ValueResultParameter;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class CallableSqlParameterFetcher {

    protected final Query query;

    protected final CallableStatement callableStatement;

    protected final List<? extends CallableSqlParameter> parameters;

    public CallableSqlParameterFetcher(Query query,
            CallableStatement callableStatement,
            List<? extends CallableSqlParameter> parameters)
            throws SQLException {
        assertNotNull(query, callableStatement, parameters);
        this.query = query;
        this.callableStatement = callableStatement;
        this.parameters = parameters;
    }

    public void fetch() throws SQLException {
        FetchingVisitor fetchngVisitor = new FetchingVisitor(query,
                callableStatement);
        for (CallableSqlParameter p : parameters) {
            p.accept(fetchngVisitor, null);
        }
    }

    protected static class FetchingVisitor implements
            CallableSqlParameterVisitor<Void, Void, SQLException> {

        protected final Query query;

        protected final Dialect dialect;

        protected final JdbcMappingVisitor jdbcMappingVisitor;

        protected final CallableStatement callableStatement;

        protected int index = 1;

        public FetchingVisitor(Query query, CallableStatement callableStatement) {
            this.query = query;
            this.dialect = query.getConfig().dialect();
            this.jdbcMappingVisitor = dialect.getJdbcMappingVisitor();
            this.callableStatement = callableStatement;
        }

        @Override
        public Void visitInOutParameter(InOutParameter<?> parameter, Void p)
                throws SQLException {
            Wrapper<?> wrapper = parameter.getWrapper();
            wrapper.accept(jdbcMappingVisitor, new GetOutParameterFunction(
                    callableStatement, index));
            parameter.updateReference();
            index++;
            return null;
        }

        @Override
        public Void visitInParameter(InParameter parameter, Void p)
                throws SQLException {
            index++;
            return null;
        }

        @Override
        public Void visitOutParameter(OutParameter<?> parameter, Void p)
                throws SQLException {
            Wrapper<?> wrapper = parameter.getWrapper();
            wrapper.accept(jdbcMappingVisitor, new GetOutParameterFunction(
                    callableStatement, index));
            parameter.updateReference();
            index++;
            return null;
        }

        @Override
        public Void visitValueResultParameter(
                ValueResultParameter<?> parameter, Void p) throws SQLException {
            Wrapper<?> domain = parameter.getWrapper();
            domain.accept(jdbcMappingVisitor, new GetOutParameterFunction(
                    callableStatement, index));
            index++;
            return null;
        }

        @Override
        public Void visitValueListParameter(ValueListParameter<?> parameter,
                Void p) throws SQLException {
            handleValueListParameter(parameter);
            return null;
        }

        @Override
        public Void visitEntityListParameter(EntityListParameter<?> parameter,
                Void p) throws SQLException {
            handleEntityListParameter(parameter);
            return null;
        }

        @Override
        public Void visitValueListResultParameter(
                ValueListResultParameter<?> parameter, Void p)
                throws SQLException {
            handleValueListParameter(parameter);
            return null;
        }

        @Override
        public Void visitEntityListResultParameter(
                EntityListResultParameter<?> parameter, Void p)
                throws SQLException {
            handleEntityListParameter(parameter);
            return null;
        }

        protected <H extends EntityType<?>> void handleEntityListParameter(
                ListParameter<H> parameter) throws SQLException {
            EntityFetcher fetcher = new EntityFetcher(query);
            if (dialect.supportsResultSetReturningAsOutParameter()) {
                JdbcType<ResultSet> resultSetType = dialect.getResultSetType();
                ResultSet resultSet = resultSetType.getValue(callableStatement,
                        index);
                try {
                    while (resultSet.next()) {
                        fetchEntities(fetcher, resultSet, parameter);
                    }
                } finally {
                    JdbcUtil.close(resultSet, query.getConfig().jdbcLogger());
                }
                index++;
            } else {
                ResultSet resultSet = callableStatement.getResultSet();
                if (resultSet != null) {
                    try {
                        while (resultSet.next()) {
                            fetchEntities(fetcher, resultSet, parameter);
                        }
                    } finally {
                        callableStatement
                                .getMoreResults(Statement.CLOSE_CURRENT_RESULT);
                    }
                }
            }
        }

        protected <H extends EntityType<?>> void fetchEntities(
                EntityFetcher fetcher, ResultSet resultSet,
                ListParameter<H> parameter) throws SQLException {
            while (resultSet.next()) {
                H holder = parameter.getElementHolder();
                fetcher.fetch(resultSet, holder);
                parameter.putElementHolder(holder);
            }
        }

        protected <H extends Wrapper<?>> void handleValueListParameter(
                ListParameter<H> parameter) throws SQLException {
            ValueFetcher fetcher = new ValueFetcher(query);
            if (dialect.supportsResultSetReturningAsOutParameter()) {
                JdbcType<ResultSet> resultSetType = dialect.getResultSetType();
                ResultSet resultSet = resultSetType.getValue(callableStatement,
                        index);
                try {
                    fetchValues(fetcher, resultSet, parameter);
                } finally {
                    JdbcUtil.close(resultSet, query.getConfig().jdbcLogger());
                }
                index++;
            } else {
                ResultSet resultSet = callableStatement.getResultSet();
                if (resultSet != null) {
                    try {
                        fetchValues(fetcher, resultSet, parameter);
                    } finally {
                        callableStatement
                                .getMoreResults(Statement.CLOSE_CURRENT_RESULT);
                    }
                }
            }
        }

        protected <H extends Wrapper<?>> void fetchValues(ValueFetcher fetcher,
                ResultSet resultSet, ListParameter<H> parameter)
                throws SQLException {
            while (resultSet.next()) {
                H holder = parameter.getElementHolder();
                fetcher.fetch(resultSet, holder);
                parameter.putElementHolder(holder);
            }
        }
    }

}
