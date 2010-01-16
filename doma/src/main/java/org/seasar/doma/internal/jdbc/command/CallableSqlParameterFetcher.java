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

import org.seasar.doma.internal.domain.DomainWrapper;
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
        public <V> Void visitBasicInOutParameter(
                BasicInOutParameter<V> parameter, Void p) throws SQLException {
            handleOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <V, D> Void visitDomainInOutParameter(
                DomainInOutParameter<V, D> parameter, Void p)
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
        public <V, D> Void visitDomainInParameter(
                DomainInParameter<V, D> parameter, Void p) throws SQLException {
            index++;
            return null;
        }

        @Override
        public <V> Void visitBasicOutParameter(BasicOutParameter<V> parameter,
                Void p) throws SQLException {
            handleOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <V, D> Void visitDomainOutParameter(
                DomainOutParameter<V, D> parameter, Void p) throws SQLException {
            handleOutParameter(parameter);
            index++;
            return null;
        }

        protected void handleOutParameter(OutParameter<?> parameter)
                throws SQLException {
            parameter.getWrapper().accept(jdbcMappingVisitor,
                    new GetOutParameterFunction(callableStatement, index));
            parameter.update();
        }

        @Override
        public <V> Void visitBasicResultParameter(
                BasicResultParameter<V> parameter, Void p) throws SQLException {
            parameter.getWrapper().accept(jdbcMappingVisitor,
                    new GetOutParameterFunction(callableStatement, index));
            index++;
            return null;
        }

        @Override
        public <V, D> Void visitDomainResultParameter(
                DomainResultParameter<V, D> parameter, Void p)
                throws SQLException {
            Wrapper<?> wrapper = parameter.getWrapper();
            wrapper.accept(jdbcMappingVisitor, new GetOutParameterFunction(
                    callableStatement, index));
            index++;
            return null;
        }

        @Override
        public <V> Void visitBasicListParameter(
                BasicListParameter<V> parameter, Void p) throws SQLException {
            handleListParameter(new BasicFetcherCallbck<V>(parameter));
            return null;
        }

        @Override
        public <V, D> Void visitDomainListParameter(
                DomainListParameter<V, D> parameter, Void p)
                throws SQLException {
            handleListParameter(new DomainFetcherCallbck<V, D>(parameter));
            return null;
        }

        @Override
        public <E> Void visitEntityListParameter(
                EntityListParameter<E> parameter, Void p) throws SQLException {
            handleListParameter(new EntityFetcherCallbck<E>(parameter));
            return null;
        }

        @Override
        public <V> Void visitBasicListResultParameter(
                BasicListResultParameter<V> parameter, Void p)
                throws SQLException {
            handleListParameter(new BasicFetcherCallbck<V>(parameter));
            return null;
        }

        @Override
        public <V, D> Void visitDomainListResultParameter(
                DomainListResultParameter<V, D> parameter, Void p)
                throws SQLException {
            handleListParameter(new DomainFetcherCallbck<V, D>(parameter));
            return null;
        }

        @Override
        public <E> Void visitEntityListResultParameter(
                EntityListResultParameter<E> parameter, Void p)
                throws SQLException {
            handleListParameter(new EntityFetcherCallbck<E>(parameter));
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
                    JdbcUtil
                            .close(resultSet, query.getConfig().getJdbcLogger());
                }
                index++;
            } else {
                ResultSet resultSet = callableStatement.getResultSet();
                if (resultSet == null && callableStatement.getMoreResults()) {
                    resultSet = callableStatement.getResultSet();
                }
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

        protected class EntityFetcherCallbck<E> implements FetcherCallback {

            protected EntityFetcher<E> fetcher;

            protected EntityListParameter<E> parameter;

            protected EntityType<E> entityType;

            public EntityFetcherCallbck(EntityListParameter<E> parameter)
                    throws SQLException {
                this.entityType = parameter.getEntityType();
                this.fetcher = new EntityFetcher<E>(query, entityType);
                this.parameter = parameter;
            }

            @Override
            public void fetch(ResultSet resultSet) throws SQLException {
                E entity = entityType.newEntity();
                fetcher.fetch(resultSet, entity);
                parameter.add(entity);
            }
        }

        protected class DomainFetcherCallbck<V, D> implements FetcherCallback {

            protected BasicFetcher fetcher;

            protected DomainListParameter<V, D> parameter;

            public DomainFetcherCallbck(DomainListParameter<V, D> parameter)
                    throws SQLException {
                this.fetcher = new BasicFetcher(query);
                this.parameter = parameter;
            }

            @Override
            public void fetch(ResultSet resultSet) throws SQLException {
                DomainWrapper<V, D> wrapper = parameter.getWrapper();
                fetcher.fetch(resultSet, wrapper);
                parameter.add(wrapper.getDomain());
            }
        }

        protected class BasicFetcherCallbck<V> implements FetcherCallback {

            protected BasicFetcher fetcher;

            protected BasicListParameter<V> parameter;

            public BasicFetcherCallbck(BasicListParameter<V> parameter)
                    throws SQLException {
                this.fetcher = new BasicFetcher(query);
                this.parameter = parameter;
            }

            @Override
            public void fetch(ResultSet resultSet) throws SQLException {
                Wrapper<V> wrapper = parameter.getWrapper();
                fetcher.fetch(resultSet, wrapper);
                parameter.add(wrapper.get());
            }
        }

    }

}
