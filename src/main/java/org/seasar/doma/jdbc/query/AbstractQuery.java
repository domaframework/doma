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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;

import org.seasar.doma.jdbc.CommentContext;
import org.seasar.doma.jdbc.Config;

public abstract class AbstractQuery implements Query {

    protected String callerClassName;

    protected String callerMethodName;

    protected Config config;

    protected Method method;

    protected int queryTimeout;

    private CommentContext commentContext;

    protected AbstractQuery() {
    }

    @Override
    public String getClassName() {
        return callerClassName;
    }

    public void setCallerClassName(String callerClassName) {
        this.callerClassName = callerClassName;
    }

    @Override
    public String getMethodName() {
        return callerMethodName;
    }

    public void setCallerMethodName(String callerMethodName) {
        this.callerMethodName = callerMethodName;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    @Override
    public void prepare() {
        assertNotNull(callerClassName, callerMethodName, config);
        commentContext = new CommentContext(callerClassName, callerMethodName, config, method);
    }

    @Override
    public String comment(String sql) {
        assertNotNull(sql, config, commentContext);
        return config.getCommenter().comment(sql, commentContext);
    }
}
