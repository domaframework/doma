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
package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import java.util.Optional;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.builder.SelectBuilder;

/**
 * A context for a comment.
 */
public class CommentContext {

    protected final String className;

    protected final String methodName;

    protected final Config config;

    protected final Optional<Method> method;

    /**
     * Creates an instance.
     * 
     * @param className
     *            the class name that executes the SQL
     * @param methodName
     *            the method name that executes the SQL
     * @param config
     *            the configuration
     * @param method
     *            the DAO method
     */
    public CommentContext(String className, String methodName, Config config, Method method) {
        if (className == null) {
            throw new DomaNullPointerException("className");
        }
        if (methodName == null) {
            throw new DomaNullPointerException("methodName");
        }
        if (config == null) {
            throw new DomaNullPointerException("config");
        }
        this.className = className;
        this.methodName = methodName;
        this.config = config;
        this.method = Optional.ofNullable(method);
    }

    /**
     * Returns the class name that executes the SQL.
     * 
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Returns the method name that executes the SQL.
     * 
     * @return the method name
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Returns the configuration.
     * 
     * @return the configuration
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Returns the DAO method or {@link Optional#empty()} if the SQL is built by
     * the builder classes such as {@link SelectBuilder}.
     * 
     * @return the DAO method or {@link Optional#empty()} if it does not exit
     */
    public Optional<Method> getMethod() {
        return method;
    }

}
