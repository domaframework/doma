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

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;

/**
 * @author taedium
 * 
 */
public abstract class AbstractCreateQuery<R> implements CreateQuery<R> {

    protected Config config;

    protected String callerClassName;

    protected String callerMethodName;

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setCallerClassName(String callerClassName) {
        this.callerClassName = callerClassName;
    }

    public void setCallerMethodName(String callerMethodName) {
        this.callerMethodName = callerMethodName;
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
        return -1;
    }

    @Override
    public Sql<?> getSql() {
        return null;
    }

    @Override
    public void complete() {
    }

}
