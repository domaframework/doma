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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.domain.DomainVisitor;
import org.seasar.doma.internal.jdbc.Entity;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.jdbc.sql.CallableSqlParameter;
import org.seasar.doma.internal.jdbc.sql.CallableSqlParameterVisitor;
import org.seasar.doma.internal.jdbc.sql.DomainListParameter;
import org.seasar.doma.internal.jdbc.sql.DomainListResultParameter;
import org.seasar.doma.internal.jdbc.sql.DomainResultParameter;
import org.seasar.doma.internal.jdbc.sql.EntityListParameter;
import org.seasar.doma.internal.jdbc.sql.EntityListResultParameter;
import org.seasar.doma.internal.jdbc.sql.InOutParameter;
import org.seasar.doma.internal.jdbc.sql.InParameter;
import org.seasar.doma.internal.jdbc.sql.ListParameter;
import org.seasar.doma.internal.jdbc.sql.OutParameter;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Dialect;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcType;

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

        protected final Config config;

        protected final Dialect dialect;

        protected final DomainVisitor<Void, JdbcMappingFunction, SQLException> jdbcMappingVisitor;

        protected final CallableStatement callableStatement;

        protected int index = 1;

        public FetchingVisitor(Query query, CallableStatement callableStatement) {
            this.query = query;
            this.config = query.getConfig();
            this.dialect = config.dialect();
            this.jdbcMappingVisitor = config.jdbcMappingVisitor();
            this.callableStatement = callableStatement;
        }

        @Override
        public Void visitInOutParameter(InOutParameter parameter, Void p)
                throws SQLException {
            Domain<?, ?> domain = parameter.getDomain();
            domain.accept(jdbcMappingVisitor, new GetOutParameterFunction(
                    config, callableStatement, index));
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
        public Void visitOutParameter(OutParameter parameter, Void p)
                throws SQLException {
            Domain<?, ?> domain = parameter.getDomain();
            domain.accept(jdbcMappingVisitor, new GetOutParameterFunction(
                    config, callableStatement, index));
            index++;
            return null;
        }

        @Override
        public Void visitDomainResultParameter(
                DomainResultParameter<?> parameter, Void p) throws SQLException {
            Domain<?, ?> domain = parameter.getDomain();
            domain.accept(jdbcMappingVisitor, new GetOutParameterFunction(
                    config, callableStatement, index));
            index++;
            return null;
        }

        @Override
        public Void visitDomainListParameter(DomainListParameter parameter,
                Void p) throws SQLException {
            handleDomainListParameter(parameter);
            return null;
        }

        @Override
        public Void visitEntityListParameter(
                EntityListParameter<?, ?> parameter, Void p)
                throws SQLException {
            handleEntityListParameter(parameter);
            return null;
        }

        @Override
        public Void visitDomainListResultParameter(
                DomainListResultParameter<?> parameter, Void p)
                throws SQLException {
            handleDomainListParameter(parameter);
            return null;
        }

        @Override
        public Void visitEntityListResultParameter(
                EntityListResultParameter<?, ?> parameter, Void p)
                throws SQLException {
            handleEntityListParameter(parameter);
            return null;
        }

        protected void handleEntityListParameter(
                ListParameter<? extends Entity<?>> parameter)
                throws SQLException {
            EntityFetcher fetcher = new EntityFetcher(query);
            if (dialect.supportsResultSetReturningAsOutParameter()) {
                JdbcType<ResultSet> resultSetType = dialect.getResultSetType();
                ResultSet resultSet = resultSetType
                        .getValue(callableStatement, index);
                try {
                    while (resultSet.next()) {
                        fetcher.fetch(resultSet, parameter.add());
                    }
                } finally {
                    Jdbcs.close(resultSet, query.getConfig().jdbcLogger());
                }
                index++;
            } else {
                ResultSet resultSet = callableStatement.getResultSet();
                try {
                    if (resultSet != null) {
                        while (resultSet.next()) {
                            fetcher.fetch(resultSet, parameter.add());
                        }
                    }
                } finally {
                    callableStatement
                            .getMoreResults(Statement.CLOSE_CURRENT_RESULT);
                }
            }
        }

        protected void handleDomainListParameter(
                ListParameter<? extends Domain<?, ?>> parameter)
                throws SQLException {
            DomainFetcher fetcher = new DomainFetcher(query);
            if (dialect.supportsResultSetReturningAsOutParameter()) {
                JdbcType<ResultSet> resultSetType = dialect.getResultSetType();
                ResultSet resultSet = resultSetType
                        .getValue(callableStatement, index);
                try {
                    while (resultSet.next()) {
                        fetcher.fetch(resultSet, parameter.add());
                    }
                } finally {
                    Jdbcs.close(resultSet, query.getConfig().jdbcLogger());
                }
                index++;
            } else {
                ResultSet resultSet = callableStatement.getResultSet();
                try {
                    if (resultSet != null) {
                        while (resultSet.next()) {
                            fetcher.fetch(resultSet, parameter.add());
                        }
                    }
                } finally {
                    callableStatement
                            .getMoreResults(Statement.CLOSE_CURRENT_RESULT);
                }
            }
        }
    }

}
