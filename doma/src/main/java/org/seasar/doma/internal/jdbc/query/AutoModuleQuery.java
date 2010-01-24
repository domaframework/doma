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
package org.seasar.doma.internal.jdbc.query;

import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.internal.jdbc.sql.CallableSql;
import org.seasar.doma.internal.jdbc.sql.CallableSqlParameter;
import org.seasar.doma.jdbc.Config;

/**
 * @author taedium
 * 
 */
public abstract class AutoModuleQuery implements ModuleQuery {

    protected Config config;

    protected String callerClassName;

    protected String callerMethodName;

    protected CallableSql sql;

    protected int queryTimeout;

    protected final List<CallableSqlParameter> parameters = new ArrayList<CallableSqlParameter>();

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

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public void addParameter(CallableSqlParameter parameter) {
        parameters.add(parameter);
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

}
