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
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogFormattingFunction;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class CallableSqlBuilder
        implements
        CallableSqlParameterVisitor<Void, CallableSqlBuilder.Context, RuntimeException> {

    protected final Config config;

    protected final ResultParameter<?> resultParameter;

    protected final List<CallableSqlParameter> parameters;

    protected final String moduleName;

    protected final SqlLogFormattingFunction formattingFunction;

    protected boolean began;

    public CallableSqlBuilder(Config config, String moduleName,
            List<CallableSqlParameter> parameters) {
        this(config, moduleName, parameters, null);
    }

    public CallableSqlBuilder(Config config, String moduleName,
            List<CallableSqlParameter> parameters,
            ResultParameter<?> resultParameter) {
        assertNotNull(config, parameters, moduleName);
        this.config = config;
        this.resultParameter = resultParameter;
        this.parameters = parameters;
        this.moduleName = moduleName;
        this.formattingFunction = new ConvertToLogFormatFunction();
    }

    public CallableSql build() {
        Context context = new Context();
        context.append("{");
        if (resultParameter != null) {
            resultParameter.accept(this, context);
            context.append("= ");
        }
        context.append("call ");
        context.append(moduleName);
        context.append("(");
        for (CallableSqlParameter parameter : parameters) {
            parameter.accept(this, context);
        }
        context.cutBackIfNecessary();
        context.append(")}");
        LinkedList<CallableSqlParameter> allParameters = new LinkedList<CallableSqlParameter>(
                parameters);
        if (resultParameter != null) {
            allParameters.addFirst(resultParameter);
        }
        return new CallableSql(context.getSqlBuf(), context
                .getFormattedSqlBuf(), allParameters);
    }

    @Override
    public Void visitDomainResultParameter(ValueResultParameter<?> parameter,
            Context p) throws RuntimeException {
        handelResultParameter(parameter, p);
        return null;
    }

    @Override
    public Void visitDomainListResultParameter(
            ValueListResultParameter<?> parameter, Context p)
            throws RuntimeException {
        handelResultParameter(parameter, p);
        return null;
    }

    @Override
    public Void visitEntityListResultParameter(
            EntityListResultParameter<?> parameter, Context p)
            throws RuntimeException {
        handelResultParameter(parameter, p);
        return null;
    }

    protected void handelResultParameter(ResultParameter<?> parameter, Context p) {
        p.appendRawSql("? ");
        p.appendFormattedSql("? ");
    }

    @Override
    public Void visitDomainListParameter(ValueListParameter<?> parameter,
            Context p) throws RuntimeException {
        handelListParameter(parameter, p);
        return null;
    }

    @Override
    public Void visitEntityListParameter(EntityListParameter<?> parameter,
            Context p) throws RuntimeException {
        handelListParameter(parameter, p);
        return null;
    }

    protected void handelListParameter(ListParameter<?> parameter, Context p) {
        if (config.dialect().supportsResultSetReturningAsOutParameter()) {
            p.appendRawSql("?, ");
            p.appendFormattedSql("?, ");
            p.addParentheticParameter(parameter);
        }
    }

    @Override
    public Void visitInParameter(InParameter parameter, Context p)
            throws RuntimeException {
        Wrapper<?> domain = parameter.getWrapper();
        p.appendRawSql("?, ");
        p.appendFormattedSql(domain.accept(config.dialect()
                .getSqlLogFormattingVisitor(), formattingFunction));
        p.appendFormattedSql(", ");
        p.addParentheticParameter(parameter);
        return null;
    }

    @Override
    public Void visitInOutParameter(InOutParameter<?> parameter, Context p)
            throws RuntimeException {
        Wrapper<?> domain = parameter.getWrapper();
        p.appendRawSql("?, ");
        p.appendFormattedSql(domain.accept(config.dialect()
                .getSqlLogFormattingVisitor(), formattingFunction));
        p.appendFormattedSql(", ");
        p.addParentheticParameter(parameter);
        return null;
    }

    @Override
    public Void visitOutParameter(OutParameter<?> parameter, Context p)
            throws RuntimeException {
        p.appendRawSql("?, ");
        p.appendFormattedSql("?, ");
        p.addParentheticParameter(parameter);
        return null;
    }

    protected class Context {

        private final StringBuilder rawSqlBuf = new StringBuilder(200);

        private final StringBuilder formattedSqlBuf = new StringBuilder(200);

        private final List<CallableSqlParameter> parentheticParameters = new ArrayList<CallableSqlParameter>();

        protected void append(CharSequence sql) {
            appendRawSql(sql);
            appendFormattedSql(sql);
        }

        protected void cutBackIfNecessary() {
            if (!parentheticParameters.isEmpty()) {
                rawSqlBuf.setLength(rawSqlBuf.length() - 2);
                formattedSqlBuf.setLength(formattedSqlBuf.length() - 2);
            }
        }

        protected void appendRawSql(CharSequence sql) {
            rawSqlBuf.append(sql);
        }

        protected void appendFormattedSql(CharSequence sql) {
            formattedSqlBuf.append(sql);
        }

        protected CharSequence getSqlBuf() {
            return rawSqlBuf;
        }

        protected CharSequence getFormattedSqlBuf() {
            return formattedSqlBuf;
        }

        protected void addParentheticParameter(CallableSqlParameter parameter) {
            parentheticParameters.add(parameter);
        }

        @Override
        public String toString() {
            return rawSqlBuf.toString();
        }
    }
}
