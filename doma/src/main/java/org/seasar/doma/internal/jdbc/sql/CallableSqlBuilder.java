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
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
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

    protected final SqlKind kind;

    protected final ResultParameter<?> resultParameter;

    protected final List<CallableSqlParameter> parameters;

    protected final String moduleName;

    protected final SqlLogFormattingFunction formattingFunction;

    protected boolean began;

    public CallableSqlBuilder(Config config, SqlKind kind, String moduleName,
            List<CallableSqlParameter> parameters) {
        this(config, kind, moduleName, parameters, null);
    }

    public CallableSqlBuilder(Config config, SqlKind kind, String moduleName,
            List<CallableSqlParameter> parameters,
            ResultParameter<?> resultParameter) {
        assertNotNull(config, kind, parameters, moduleName);
        this.config = config;
        this.kind = kind;
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
        return new CallableSql(kind, context.getSqlBuf(),
                context.getFormattedSqlBuf(), allParameters);
    }

    @Override
    public <BASIC> Void visitBasicInParameter(
            BasicInParameter<BASIC> parameter, Context p)
            throws RuntimeException {
        handleInParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC> Void visitBasicOutParameter(
            BasicOutParameter<BASIC> parameter, Context p)
            throws RuntimeException {
        handleOutParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC> Void visitBasicInOutParameter(
            BasicInOutParameter<BASIC> parameter, Context p)
            throws RuntimeException {
        handleInParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC> Void visitBasicListParameter(
            BasicListParameter<BASIC> parameter, Context p)
            throws RuntimeException {
        handelListParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC> Void visitBasicSingleResultParameter(
            BasicSingleResultParameter<BASIC> parameter, Context p)
            throws RuntimeException {
        handelResultParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC> Void visitBasicResultListParameter(
            BasicResultListParameter<BASIC> parameter, Context p)
            throws RuntimeException {
        handelResultParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC, DOMAIN> Void visitDomainInParameter(
            DomainInParameter<BASIC, DOMAIN> parameter, Context p)
            throws RuntimeException {
        handleInParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC, DOMAIN> Void visitDomainInOutParameter(
            DomainInOutParameter<BASIC, DOMAIN> parameter, Context p)
            throws RuntimeException {
        handleInParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC, DOMAIN> Void visitDomainOutParameter(
            DomainOutParameter<BASIC, DOMAIN> parameter, Context p)
            throws RuntimeException {
        handleOutParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC, DOMAIN> Void visitDomainListParameter(
            DomainListParameter<BASIC, DOMAIN> parameter, Context p)
            throws RuntimeException {
        handelListParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC, DOMAIN> Void visitDomainSingleResultParameter(
            DomainSingleResultParameter<BASIC, DOMAIN> parameter, Context p)
            throws RuntimeException {
        handelResultParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC, DOMAIN> Void visitDomainResultListParameter(
            DomainResultListParameter<BASIC, DOMAIN> parameter, Context p)
            throws RuntimeException {
        handelResultParameter(parameter, p);
        return null;
    }

    @Override
    public <ENTITY> Void visitEntityListParameter(
            EntityListParameter<ENTITY> parameter, Context p)
            throws RuntimeException {
        handelListParameter(parameter, p);
        return null;
    }

    @Override
    public <ENTITY> Void visitEntityResultListParameter(
            EntityResultListParameter<ENTITY> parameter, Context p)
            throws RuntimeException {
        handelResultParameter(parameter, p);
        return null;
    }

    @Override
    public Void visitMapListParameter(MapListParameter parameter, Context p)
            throws RuntimeException {
        handelListParameter(parameter, p);
        return null;
    }

    @Override
    public Void visitMapResultListParameter(MapResultListParameter parameter,
            Context p) throws RuntimeException {
        handelResultParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC> Void visitOptionalBasicInParameter(
            OptionalBasicInParameter<BASIC> parameter, Context p)
            throws RuntimeException {
        handleInParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC> Void visitOptionalBasicOutParameter(
            OptionalBasicOutParameter<BASIC> parameter, Context p)
            throws RuntimeException {
        handleOutParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC> Void visitOptionalBasicInOutParameter(
            OptionalBasicInOutParameter<BASIC> parameter, Context p)
            throws RuntimeException {
        handleInParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC> Void visitOptionalBasicListParameter(
            OptionalBasicListParameter<BASIC> parameter, Context p)
            throws RuntimeException {
        handelListParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC> Void visitOptionalBasicSingleResultParameter(
            OptionalBasicSingleResultParameter<BASIC> parameter, Context p)
            throws RuntimeException {
        handelResultParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC> Void visitOptionalBasicResultListParameter(
            OptionalBasicResultListParameter<BASIC> parameter, Context p)
            throws RuntimeException {
        handelResultParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC, DOMAIN> Void visitOptionalDomainInParameter(
            OptionalDomainInParameter<BASIC, DOMAIN> parameter, Context p)
            throws RuntimeException {
        handleInParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC, DOMAIN> Void visitOptionalDomainOutParameter(
            OptionalDomainOutParameter<BASIC, DOMAIN> parameter, Context p)
            throws RuntimeException {
        handleOutParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC, DOMAIN> Void visitOptionalDomainInOutParameter(
            OptionalDomainInOutParameter<BASIC, DOMAIN> parameter, Context p)
            throws RuntimeException {
        handleInParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC, DOMAIN> Void visitOptionalDomainListParameter(
            OptionalDomainListParameter<BASIC, DOMAIN> parameter, Context p)
            throws RuntimeException {
        handelListParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC, DOMAIN> Void visitOptionalDomainSingleResultParameter(
            OptionalDomainSingleResultParameter<BASIC, DOMAIN> parameter,
            Context p) throws RuntimeException {
        handelResultParameter(parameter, p);
        return null;
    }

    @Override
    public <BASIC, DOMAIN> Void visitOptionalDomainResultListParameter(
            OptionalDomainResultListParameter<BASIC, DOMAIN> parameter,
            Context p) throws RuntimeException {
        handelResultParameter(parameter, p);
        return null;
    }

    protected <BASIC> void handleInParameter(InParameter<BASIC> parameter,
            Context p) {
        Wrapper<BASIC> wrapper = parameter.getWrapper();
        p.appendRawSql("?, ");
        p.appendFormattedSql(wrapper.accept(config.getDialect()
                .getSqlLogFormattingVisitor(), formattingFunction));
        p.appendFormattedSql(", ");
        p.addParameter(parameter);
    }

    protected <BASIC> void handleOutParameter(OutParameter<BASIC> parameter,
            Context p) {
        p.appendRawSql("?, ");
        p.appendFormattedSql("?, ");
        p.addParameter(parameter);
    }

    protected <ELEMENT> void handelListParameter(
            ListParameter<ELEMENT> parameter, Context p) {
        if (config.getDialect().supportsResultSetReturningAsOutParameter()) {
            p.appendRawSql("?, ");
            p.appendFormattedSql("?, ");
            p.addParameter(parameter);
        }
    }

    protected <RESULT> void handelResultParameter(
            ResultParameter<RESULT> parameter, Context p) {
        p.appendRawSql("? ");
        p.appendFormattedSql("? ");
    }

    protected class Context {

        private final StringBuilder rawSqlBuf = new StringBuilder(200);

        private final StringBuilder formattedSqlBuf = new StringBuilder(200);

        private final List<CallableSqlParameter> contextParameters = new ArrayList<CallableSqlParameter>();

        protected void append(CharSequence sql) {
            appendRawSql(sql);
            appendFormattedSql(sql);
        }

        protected void cutBackIfNecessary() {
            if (!contextParameters.isEmpty()) {
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

        protected void addParameter(CallableSqlParameter parameter) {
            contextParameters.add(parameter);
        }

        @Override
        public String toString() {
            return rawSqlBuf.toString();
        }
    }
}
