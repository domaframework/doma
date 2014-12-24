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
 * コメントのコンテキストです。
 * 
 * @author nakamura-to
 * @since 2.1.0
 */
public class CommentContext {

    /** SQLの呼び出し元のクラス名 */
    protected final String className;

    /** SQLの呼び出し元のメソッド名 */
    protected final String methodName;

    /** 設定 */
    protected final Config config;

    /** Daoのメソッド */
    protected final Optional<Method> method;

    /**
     * インスタンスを構築します。
     * 
     * @param className
     *            SQLの呼び出し元のクラス名
     * @param methodName
     *            SQLの呼び出し元のメソッド名
     * @param config
     *            設定
     * @param method
     *            Daoのメソッド
     */
    public CommentContext(String className, String methodName, Config config,
            Method method) {
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
     * SQLの呼び出し元のクラス名を返します。
     * 
     * @return SQLの呼び出し元のクラス名
     */
    public String getClassName() {
        return className;
    }

    /**
     * SQLの呼び出し元のメソッド名を返します。
     * 
     * @return SQLの呼び出し元のメソッド名
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 設定を返します。
     * 
     * @return 設定
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Daoのメソッドを返します。
     * <p>
     * {@link SelectBuilder} などを用いるなどDaoを利用していない場合に {@link Optional#empty()}
     * を返します。
     * 
     * @return Daoのメソッド
     */
    public Optional<Method> getMethod() {
        return method;
    }

}
