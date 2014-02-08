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
package org.seasar.doma.jdbc.query;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.seasar.doma.internal.jdbc.sql.CallableSql;
import org.seasar.doma.internal.jdbc.util.DatabaseObjectUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlParameter;

/**
 * @author taedium
 * 
 */
public abstract class AutoModuleQuery implements ModuleQuery {

    protected Config config;

    protected String callerClassName;

    protected String callerMethodName;

    protected Method method;

    protected CallableSql sql;

    protected int queryTimeout;

    protected String catalogName;

    protected String schemaName;

    protected String moduleName;

    protected String qualifiedName;

    protected boolean isQuoteRequired;

    protected final List<SqlParameter> parameters = new ArrayList<>();

    protected void prepareQualifiedName() {
        Function<String, String> mapper = isQuoteRequired ? config.getDialect()::applyQuote
                : Function.identity();
        qualifiedName = DatabaseObjectUtil.getQualifiedName(mapper,
                catalogName, schemaName, moduleName);
    }

    protected void prepareOptions() {
        if (queryTimeout <= 0) {
            queryTimeout = config.getQueryTimeout();
        }
    }

    @Override
    public void complete() {
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setCallerClassName(String callerClassName) {
        this.callerClassName = callerClassName;
    }

    public void setCallerMethodName(String callerMethodName) {
        this.callerMethodName = callerMethodName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    protected void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setQuoteRequired(boolean isQuoteRequired) {
        this.isQuoteRequired = isQuoteRequired;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public void addParameter(SqlParameter parameter) {
        parameters.add(parameter);
    }

    @Override
    public String getQualifiedName() {
        return qualifiedName;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public String getClassName() {
        return callerClassName;
    }

    @Override
    public String getMethodName() {
        return callerMethodName;
    }

    @Override
    public int getQueryTimeout() {
        return queryTimeout;
    }

    @Override
    public CallableSql getSql() {
        return sql;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

}
