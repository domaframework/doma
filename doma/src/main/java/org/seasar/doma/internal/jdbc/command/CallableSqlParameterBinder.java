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
import java.util.List;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.domain.DomainVisitor;
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
import org.seasar.doma.jdbc.Dialect;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcType;


/**
 * 
 * @author taedium
 * 
 */
public class CallableSqlParameterBinder {

    protected final Query query;

    public CallableSqlParameterBinder(Query query) {
        assertNotNull(query);
        this.query = query;
    }

    public void bind(CallableStatement callableStatement,
            List<? extends CallableSqlParameter> parameters)
            throws SQLException {
        assertNotNull(callableStatement, parameters);
        BindingVisitor visitor = new BindingVisitor(query, callableStatement);
        for (CallableSqlParameter p : parameters) {
            p.accept(visitor, null);
        }
    }

    protected static class BindingVisitor implements
            CallableSqlParameterVisitor<Void, Void, SQLException> {

        protected final Dialect dialect;

        protected final DomainVisitor<Void, JdbcMappingFunction, SQLException> jdbcMappingVisitor;

        protected final CallableStatement callableStatement;

        protected int index = 1;

        public BindingVisitor(Query query, CallableStatement callableStatement) {
            this.dialect = query.getConfig().dialect();
            this.jdbcMappingVisitor = query.getConfig().jdbcMappingVisitor();
            this.callableStatement = callableStatement;
        }

        @Override
        public Void visitDomainListParameter(DomainListParameter parameter,
                Void p) throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public Void visitEntityListParameter(
                EntityListParameter<?, ?> parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public Void visitDomainListResultParameter(
                DomainListResultParameter<?> parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public Void visitEntityListResultParameter(
                EntityListResultParameter<?, ?> parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        protected void handleListParameter(ListParameter<?> parameter)
                throws SQLException {
            if (dialect.supportsResultSetReturningAsOutParameter()) {
                JdbcType<ResultSet> resultSetType = dialect.getResultSetType();
                resultSetType.registerOutParameter(callableStatement, index);
                index++;
            }
        }

        @Override
        public Void visitInOutParameter(InOutParameter parameter, Void p)
                throws SQLException {
            Domain<?, ?> domain = parameter.getDomain();
            domain.accept(jdbcMappingVisitor, new SetValueFunction(
                    callableStatement, index));
            domain.accept(jdbcMappingVisitor, new RegisterOutParameterFunction(
                    callableStatement, index));
            index++;
            return null;
        }

        @Override
        public Void visitInParameter(InParameter parameter, Void p)
                throws SQLException {
            Domain<?, ?> domain = parameter.getDomain();
            domain.accept(jdbcMappingVisitor, new SetValueFunction(
                    callableStatement, index));
            index++;
            return null;
        }

        @Override
        public Void visitOutParameter(OutParameter parameter, Void p)
                throws SQLException {
            Domain<?, ?> domain = parameter.getDomain();
            domain.accept(jdbcMappingVisitor, new RegisterOutParameterFunction(
                    callableStatement, index));
            index++;
            return null;
        }

        @Override
        public Void visitDomainResultParameter(
                DomainResultParameter<?> parameter, Void p) throws SQLException {
            Domain<?, ?> domain = parameter.getDomain();
            domain.accept(jdbcMappingVisitor, new RegisterOutParameterFunction(
                    callableStatement, index));
            index++;
            return null;
        }

    }

}
