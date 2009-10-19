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

import org.seasar.doma.internal.domain.DomainType;
import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.jdbc.sql.BasicInOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicInParameter;
import org.seasar.doma.internal.jdbc.sql.BasicListParameter;
import org.seasar.doma.internal.jdbc.sql.BasicListResultParameter;
import org.seasar.doma.internal.jdbc.sql.BasicOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicResultParameter;
import org.seasar.doma.internal.jdbc.sql.CallableSqlParameter;
import org.seasar.doma.internal.jdbc.sql.CallableSqlParameterVisitor;
import org.seasar.doma.internal.jdbc.sql.DomainInOutParameter;
import org.seasar.doma.internal.jdbc.sql.DomainInParameter;
import org.seasar.doma.internal.jdbc.sql.DomainListParameter;
import org.seasar.doma.internal.jdbc.sql.DomainListResultParameter;
import org.seasar.doma.internal.jdbc.sql.DomainOutParameter;
import org.seasar.doma.internal.jdbc.sql.DomainResultParameter;
import org.seasar.doma.internal.jdbc.sql.EntityListParameter;
import org.seasar.doma.internal.jdbc.sql.EntityListResultParameter;
import org.seasar.doma.internal.jdbc.sql.OutParameter;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class CallableSqlParameterFetcher implements
        ResultFetcher<CallableStatement, List<? extends CallableSqlParameter>> {

    protected final Query query;

    public CallableSqlParameterFetcher(Query query) throws SQLException {
        assertNotNull(query);
        this.query = query;
    }

    public void fetch(CallableStatement callableStatement,
            List<? extends CallableSqlParameter> parameters)
            throws SQLException {
        assertNotNull(callableStatement, parameters);
        FetchingVisitor fetchngVisitor = new FetchingVisitor(query,
                callableStatement);
        for (CallableSqlParameter parameter : parameters) {
            parameter.accept(fetchngVisitor, null);
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
            this.dialect = query.getConfig().getDialect();
            this.jdbcMappingVisitor = dialect.getJdbcMappingVisitor();
            this.callableStatement = callableStatement;
        }

        @Override
        public Void visitBasicInOutParameter(BasicInOutParameter<?> parameter,
                Void p) throws SQLException {
            handleOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public Void visitDomainInOutParameter(
                DomainInOutParameter<?, ?> parameter, Void p)
                throws SQLException {
            handleOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public Void visitBasicInParameter(BasicInParameter parameter, Void p)
                throws SQLException {
            index++;
            return null;
        }

        @Override
        public Void visitDomainInParameter(DomainInParameter<?, ?> parameter,
                Void p) throws SQLException {
            index++;
            return null;
        }

        @Override
        public Void visitBasicOutParameter(BasicOutParameter<?> parameter,
                Void p) throws SQLException {
            handleOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public Void visitDomainOutParameter(DomainOutParameter<?, ?> parameter,
                Void p) throws SQLException {
            handleOutParameter(parameter);
            index++;
            return null;
        }

        protected void handleOutParameter(OutParameter parameter)
                throws SQLException {
            parameter.getWrapper().accept(jdbcMappingVisitor,
                    new GetOutParameterFunction(callableStatement, index));
            parameter.updateReference();
        }

        @Override
        public Void visitBasicResultParameter(
                BasicResultParameter<?> parameter, Void p) throws SQLException {
            parameter.getWrapper().accept(jdbcMappingVisitor,
                    new GetOutParameterFunction(callableStatement, index));
            index++;
            return null;
        }

        @Override
        public Void visitDomainResultParameter(
                DomainResultParameter<?, ?> parameter, Void p)
                throws SQLException {
            Wrapper<?> wrapper = parameter.getDomainType().getWrapper();
            wrapper.accept(jdbcMappingVisitor, new GetOutParameterFunction(
                    callableStatement, index));
            index++;
            return null;
        }

        @Override
        public Void visitBasicListParameter(BasicListParameter<?> parameter,
                Void p) throws SQLException {
            handleListParameter(new BasicFetcherCallbck(parameter));
            return null;
        }

        @Override
        public Void visitDomainListParameter(
                DomainListParameter<?, ?> parameter, Void p)
                throws SQLException {
            handleListParameter(new DomainFetcherCallbck(parameter));
            return null;
        }

        @Override
        public Void visitEntityListParameter(EntityListParameter<?> parameter,
                Void p) throws SQLException {
            handleListParameter(new EntityFetcherCallbck(parameter));
            return null;
        }

        @Override
        public Void visitBasicListResultParameter(
                BasicListResultParameter<?> parameter, Void p)
                throws SQLException {
            handleListParameter(new BasicFetcherCallbck(parameter));
            return null;
        }

        @Override
        public Void visitDomainListResultParameter(
                DomainListResultParameter<?, ?> parameter, Void p)
                throws SQLException {
            handleListParameter(new DomainFetcherCallbck(parameter));
            return null;
        }

        @Override
        public Void visitEntityListResultParameter(
                EntityListResultParameter<?> parameter, Void p)
                throws SQLException {
            handleListParameter(new EntityFetcherCallbck(parameter));
            return null;
        }

        protected void handleListParameter(FetcherCallback callback)
                throws SQLException {
            if (dialect.supportsResultSetReturningAsOutParameter()) {
                JdbcType<ResultSet> resultSetType = dialect.getResultSetType();
                ResultSet resultSet = resultSetType.getValue(callableStatement,
                        index);
                try {
                    while (resultSet.next()) {
                        callback.fetch(resultSet);
                    }
                } finally {
                    JdbcUtil.close(resultSet, query.getConfig().getJdbcLogger());
                }
                index++;
            } else {
                ResultSet resultSet = callableStatement.getResultSet();
                if (resultSet != null) {
                    try {
                        while (resultSet.next()) {
                            callback.fetch(resultSet);
                        }
                    } finally {
                        callableStatement
                                .getMoreResults(Statement.CLOSE_CURRENT_RESULT);
                    }
                }
            }
        }

        protected interface FetcherCallback {

            void fetch(ResultSet resultSet) throws SQLException;
        }

        protected class EntityFetcherCallbck implements FetcherCallback {

            protected EntityFetcher fetcher;

            protected EntityListParameter<?> parameter;

            public EntityFetcherCallbck(EntityListParameter<?> parameter)
                    throws SQLException {
                this.fetcher = new EntityFetcher(query);
                this.parameter = parameter;
            }

            @Override
            public void fetch(ResultSet resultSet) throws SQLException {
                EntityType<?> entityType = parameter.getElementHolder();
                fetcher.fetch(resultSet, entityType);
                parameter.add();
            }
        }

        protected class DomainFetcherCallbck implements FetcherCallback {

            protected BasicFetcher fetcher;

            protected DomainListParameter<?, ?> parameter;

            public DomainFetcherCallbck(DomainListParameter<?, ?> parameter)
                    throws SQLException {
                this.fetcher = new BasicFetcher(query);
                this.parameter = parameter;
            }

            @Override
            public void fetch(ResultSet resultSet) throws SQLException {
                DomainType<?, ?> domainType = parameter.getElementHolder();
                fetcher.fetch(resultSet, domainType.getWrapper());
                parameter.add();
            }
        }

        protected class BasicFetcherCallbck implements FetcherCallback {

            protected BasicFetcher fetcher;

            protected BasicListParameter<?> parameter;

            public BasicFetcherCallbck(BasicListParameter<?> parameter)
                    throws SQLException {
                this.fetcher = new BasicFetcher(query);
                this.parameter = parameter;
            }

            @Override
            public void fetch(ResultSet resultSet) throws SQLException {
                Wrapper<?> wrapper = parameter.getElementHolder();
                fetcher.fetch(resultSet, wrapper);
                parameter.add();
            }
        }

    }

}
