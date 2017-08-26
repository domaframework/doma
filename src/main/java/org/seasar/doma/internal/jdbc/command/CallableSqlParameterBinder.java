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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.JdbcMappable;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.ListParameter;
import org.seasar.doma.jdbc.OutParameter;
import org.seasar.doma.jdbc.ResultListParameter;
import org.seasar.doma.jdbc.SingleResultParameter;
import org.seasar.doma.jdbc.SqlParameter;
import org.seasar.doma.jdbc.SqlParameterVisitor;
import org.seasar.doma.jdbc.command.JdbcOutParameterRegistrar;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

/**
 * 
 * @author taedium
 * 
 */
public class CallableSqlParameterBinder
        extends AbstractParameterBinder<CallableStatement, SqlParameter> {

    protected final Query query;

    public CallableSqlParameterBinder(Query query) {
        assertNotNull(query);
        this.query = query;
    }

    @Override
    public void bind(CallableStatement callableStatement, List<? extends SqlParameter> parameters)
            throws SQLException {
        assertNotNull(callableStatement, parameters);
        BindingVisitor visitor = new BindingVisitor(query, callableStatement);
        for (SqlParameter parameter : parameters) {
            parameter.accept(visitor, null);
        }
    }

    protected class BindingVisitor implements SqlParameterVisitor<Void, Void, SQLException> {

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
        public <BASIC> Void visitInParameter(InParameter<BASIC> parameter, Void p)
                throws SQLException {
            bindInParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC> Void visitOutParameter(OutParameter<BASIC> parameter, Void p)
                throws SQLException {
            registerOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <BASIC, INOUT extends InParameter<BASIC> & OutParameter<BASIC>> Void visitInOutParameter(
                INOUT parameter, Void p) throws SQLException {
            bindInParameter(parameter);
            registerOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <ELEMENT> Void visitListParameter(ListParameter<ELEMENT> parameter, Void p)
                throws SQLException {
            registerListParameter(parameter);
            return null;
        }

        @Override
        public <BASIC, RESULT> Void visitSingleResultParameter(
                SingleResultParameter<BASIC, RESULT> parameter, Void p) throws SQLException {
            registerOutParameter(parameter);
            index++;
            return null;
        }

        @Override
        public <ELEMENT> Void visitResultListParameter(ResultListParameter<ELEMENT> parameter,
                Void p) throws SQLException {
            registerListParameter(parameter);
            return null;
        }

        protected <BASIC> void bindInParameter(InParameter<BASIC> parameter) throws SQLException {
            CallableSqlParameterBinder.this.bindInParameter(callableStatement, parameter, index,
                    jdbcMappingVisitor);
        }

        protected <BASIC> void registerOutParameter(JdbcMappable<BASIC> parameter)
                throws SQLException {
            Wrapper<BASIC> wrapper = parameter.getWrapper();
            wrapper.accept(jdbcMappingVisitor,
                    new JdbcOutParameterRegistrar(callableStatement, index), parameter);
        }

        protected <ELEMENT> void registerListParameter(ListParameter<ELEMENT> parameter)
                throws SQLException {
            if (dialect.supportsResultSetReturningAsOutParameter()) {
                JdbcType<ResultSet> resultSetType = dialect.getResultSetType();
                resultSetType.registerOutParameter(callableStatement, index);
                index++;
            }
        }

    }
}
