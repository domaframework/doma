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

import org.seasar.doma.internal.jdbc.sql.BasicInOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicInParameter;
import org.seasar.doma.internal.jdbc.sql.BasicListParameter;
import org.seasar.doma.internal.jdbc.sql.BasicOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicResultListParameter;
import org.seasar.doma.internal.jdbc.sql.BasicSingleResultParameter;
import org.seasar.doma.internal.jdbc.sql.CallableSqlParameter;
import org.seasar.doma.internal.jdbc.sql.CallableSqlParameterVisitor;
import org.seasar.doma.internal.jdbc.sql.DomainInOutParameter;
import org.seasar.doma.internal.jdbc.sql.DomainInParameter;
import org.seasar.doma.internal.jdbc.sql.DomainListParameter;
import org.seasar.doma.internal.jdbc.sql.DomainOutParameter;
import org.seasar.doma.internal.jdbc.sql.DomainResultListParameter;
import org.seasar.doma.internal.jdbc.sql.DomainSingleResultParameter;
import org.seasar.doma.internal.jdbc.sql.EntityListParameter;
import org.seasar.doma.internal.jdbc.sql.EntityResultListParameter;
import org.seasar.doma.internal.jdbc.sql.InParameter;
import org.seasar.doma.internal.jdbc.sql.ListParameter;
import org.seasar.doma.internal.jdbc.sql.MapListParameter;
import org.seasar.doma.internal.jdbc.sql.MapResultListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalBasicInOutParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalBasicInParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalBasicListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalBasicOutParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalBasicResultListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalBasicSingleResultParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDomainInOutParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDomainInParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDomainListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDomainOutParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDomainResultListParameter;
import org.seasar.doma.internal.jdbc.sql.OptionalDomainSingleResultParameter;
import org.seasar.doma.internal.jdbc.sql.OutParameter;
import org.seasar.doma.internal.jdbc.sql.SingleResultParameter;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.command.RegisterOutParameterFunction;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.query.Query;
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
        public <BASIC> Void visitBasicInParameter(
                BasicInParameter<BASIC> parameter, Void p) throws SQLException {
            handleInParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC> Void visitBasicOutParameter(
                BasicOutParameter<BASIC> parameter, Void p) throws SQLException {
            handleOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC> Void visitBasicInOutParameter(
                BasicInOutParameter<BASIC> parameter, Void p)
                throws SQLException {
            handleInOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC> Void visitBasicListParameter(
                BasicListParameter<BASIC> parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public <BASIC> Void visitBasicSingleResultParameter(
                BasicSingleResultParameter<BASIC> parameter, Void p)
                throws SQLException {
            handleSingleResultParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC> Void visitBasicResultListParameter(
                BasicResultListParameter<BASIC> parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public <BASIC, DOMAIN> Void visitDomainInParameter(
                DomainInParameter<BASIC, DOMAIN> parameter, Void p)
                throws SQLException {
            handleInParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC, DOMAIN> Void visitDomainOutParameter(
                DomainOutParameter<BASIC, DOMAIN> parameter, Void p)
                throws SQLException {
            handleOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC, DOMAIN> Void visitDomainInOutParameter(
                DomainInOutParameter<BASIC, DOMAIN> parameter, Void p)
                throws SQLException {
            handleInOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC, DOMAIN> Void visitDomainListParameter(
                DomainListParameter<BASIC, DOMAIN> parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public <BASIC, DOMAIN> Void visitDomainSingleResultParameter(
                DomainSingleResultParameter<BASIC, DOMAIN> parameter, Void p)
                throws SQLException {
            handleSingleResultParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC, DOMAIN> Void visitDomainResultListParameter(
                DomainResultListParameter<BASIC, DOMAIN> parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public <ENTITY> Void visitEntityListParameter(
                EntityListParameter<ENTITY> parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public <ENTITY> Void visitEntityResultListParameter(
                EntityResultListParameter<ENTITY> parameter, Void p)
                throws SQLException {
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
        public Void visitMapResultListParameter(
                MapResultListParameter parameter, Void p) throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public <BASIC> Void visitOptionalBasicInParameter(
                OptionalBasicInParameter<BASIC> parameter, Void p)
                throws SQLException {
            handleInParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC> Void visitOptionalBasicOutParameter(
                OptionalBasicOutParameter<BASIC> parameter, Void p)
                throws SQLException {
            handleOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC> Void visitOptionalBasicInOutParameter(
                OptionalBasicInOutParameter<BASIC> parameter, Void p)
                throws SQLException {
            handleInOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC> Void visitOptionalBasicListParameter(
                OptionalBasicListParameter<BASIC> parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public <BASIC> Void visitOptionalBasicSingleResultParameter(
                OptionalBasicSingleResultParameter<BASIC> parameter, Void p)
                throws SQLException {
            handleSingleResultParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC> Void visitOptionalBasicResultListParameter(
                OptionalBasicResultListParameter<BASIC> parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public <BASIC, DOMAIN> Void visitOptionalDomainInParameter(
                OptionalDomainInParameter<BASIC, DOMAIN> parameter, Void p)
                throws SQLException {
            handleInParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC, DOMAIN> Void visitOptionalDomainInOutParameter(
                OptionalDomainInOutParameter<BASIC, DOMAIN> parameter, Void p)
                throws SQLException {
            handleInOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC, DOMAIN> Void visitOptionalDomainOutParameter(
                OptionalDomainOutParameter<BASIC, DOMAIN> parameter, Void p)
                throws SQLException {
            handleOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC, DOMAIN> Void visitOptionalDomainListParameter(
                OptionalDomainListParameter<BASIC, DOMAIN> parameter, Void p)
                throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        @Override
        public <BASIC, DOMAIN> Void visitOptionalDomainSingleResultParameter(
                OptionalDomainSingleResultParameter<BASIC, DOMAIN> parameter,
                Void p) throws SQLException {
            handleSingleResultParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC, DOMAIN> Void visitOptionalDomainResultListParameter(
                OptionalDomainResultListParameter<BASIC, DOMAIN> parameter,
                Void p) throws SQLException {
            handleListParameter(parameter);
            return null;
        }

        protected <BASIC> void handleInParameter(InParameter<BASIC> parameter)
                throws SQLException {
            Wrapper<BASIC> wrapper = parameter.getWrapper();
            wrapper.accept(jdbcMappingVisitor, new SetValueFunction(
                    callableStatement, index));
        }

        protected <BASIC> void handleOutParameter(OutParameter<BASIC> parameter)
                throws SQLException {
            Wrapper<BASIC> wrapper = parameter.getWrapper();
            wrapper.accept(jdbcMappingVisitor,
                    new RegisterOutParameterFunction(callableStatement, index));
        }

        protected <BASIC, INOUT extends InParameter<BASIC> & OutParameter<BASIC>> void handleInOutParameter(
                INOUT parameter) throws SQLException {
            handleInParameter(parameter);
            handleOutParameter(parameter);
        }

        protected <ELEMENT> void handleListParameter(
                ListParameter<ELEMENT> parameter) throws SQLException {
            if (dialect.supportsResultSetReturningAsOutParameter()) {
                JdbcType<ResultSet> resultSetType = dialect.getResultSetType();
                resultSetType.registerOutParameter(callableStatement, index);
                index++;
            }
        }

        public <BASIC, RESULT> void handleSingleResultParameter(
                SingleResultParameter<BASIC, RESULT> parameter)
                throws SQLException {
            Wrapper<BASIC> wrapper = parameter.getWrapper();
            wrapper.accept(jdbcMappingVisitor,
                    new RegisterOutParameterFunction(callableStatement, index));
        }

    }
}
