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

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
import org.seasar.doma.internal.jdbc.sql.InParameter;
import org.seasar.doma.internal.jdbc.sql.ListParameter;
import org.seasar.doma.internal.jdbc.sql.MapListParameter;
import org.seasar.doma.internal.jdbc.sql.MapListResultParameter;
import org.seasar.doma.internal.jdbc.sql.OutParameter;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

/**
 * 
 * @author taedium
 * 
 */
public class CallableSqlParameterBinder implements
        ParameterBinder<CallableStatement, CallableSqlParameter> {

    protected final Query query;

    public CallableSqlParameterBinder(Query query) {
        assertNotNull(query);
        this.query = query;
    }

    @Override
    public void bind(CallableStatement callableStatement,
            List<? extends CallableSqlParameter> parameters)
            throws SQLException {
        assertNotNull(callableStatement, parameters);
        BindingVisitor visitor = new BindingVisitor(query, callableStatement);
        for (CallableSqlParameter parameter : parameters) {
            parameter.accept(visitor, null);
        }
    }

    protected static class BindingVisitor implements
            CallableSqlParameterVisitor<Void, Void, SQLException> {

        protected final Dialect dialect;

        protected final JdbcMappingVisitor jdbcMappingVisitor;

        protected final CallableStatement callableStatement;

        protected int index = 1;

        public BindingVisitor(Query query, CallableStatement callableStatement) {
            this.dialect = query.getConfig().getDialect();
            this.jdbcMappingVisitor = dialect.getJdbcMappingVisitor();
            this.callableStatement = callableStatement;
        }

        @Override
        public <V> Void visitBasicListParameter(
                BasicListParameter<V> parameter, Void p) throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public <V, D> Void visitDomainListParameter(
                DomainListParameter<V, D> parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public <E> Void visitEntityListParameter(
                EntityListParameter<E> parameter, Void p) throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public Void visitMapListParameter(MapListParameter parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public <V> Void visitBasicListResultParameter(
                BasicListResultParameter<V> parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public <V, D> Void visitDomainListResultParameter(
                DomainListResultParameter<V, D> parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public <E> Void visitEntityListResultParameter(
                EntityListResultParameter<E> parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public Void visitMapListResultParameter(
                MapListResultParameter parameter, Void p) throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        protected void handleListParameter(ListParameter<?, ?> parameter)
                throws SQLException {
            if (dialect.supportsResultSetReturningAsOutParameter()) {
                JdbcType<ResultSet> resultSetType = dialect.getResultSetType();
                resultSetType.registerOutParameter(callableStatement, index);
                index++;
            }
        }

        @Override
        public <V> Void visitBasicInOutParameter(
                BasicInOutParameter<V> parameter, Void p) throws SQLException {
            handleInParameter(parameter);
            handleOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <V, D> Void visitDomainInOutParameter(
                DomainInOutParameter<V, D> parameter, Void p)
                throws SQLException {
            handleInParameter(parameter);
            handleOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public Void visitBasicInParameter(BasicInParameter parameter, Void p)
                throws SQLException {
            handleInParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <V, D> Void visitDomainInParameter(
                DomainInParameter<V, D> parameter, Void p) throws SQLException {
            handleInParameter(parameter);
            index++;
            return null;
        }

        protected void handleInParameter(InParameter parameter)
                throws SQLException {
            parameter.getWrapper().accept(jdbcMappingVisitor,
                    new SetValueFunction(callableStatement, index));
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
                    new RegisterOutParameterFunction(callableStatement, index));
        }

        @Override
        public <V> Void visitBasicResultParameter(
                BasicResultParameter<V> parameter, Void p) throws SQLException {
            Wrapper<?> wrapper = parameter.getWrapper();
            wrapper.accept(jdbcMappingVisitor,
                    new RegisterOutParameterFunction(callableStatement, index));
            index++;
            return null;
        }

        @Override
        public <V, D> Void visitDomainResultParameter(
                DomainResultParameter<V, D> parameter, Void p)
                throws SQLException {
            Wrapper<?> wrapper = parameter.getWrapper();
            wrapper.accept(jdbcMappingVisitor,
                    new RegisterOutParameterFunction(callableStatement, index));
            index++;
            return null;
        }
    }

}
