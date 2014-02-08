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

import org.seasar.doma.message.Message;

/**
 * {@link Config}に不適切な設定がある場合にスローされる例外です。
 * 
 * @author taedium
 * 
 */
public class ConfigException extends JdbcException {

    private static final long serialVersionUID = 1L;

    /** {@link Config} の実装クラス名 */
    protected final String className;

    /** {@link Config} の実装クラスのメソッド名 */
    protected final String methodName;

    /**
     * インスタンスを構築します。
     * 
     * @param className
     *            {@link Config} の実装クラス名
     * @param methodName
     *            {@link Config} の実装クラスのメソッド名
     */
    public ConfigException(String className, String methodName) {
        super(Message.DOMA2035, className, methodName);
        this.className = className;
        this.methodName = methodName;
    }

    /**
     * {@link Config} の実装クラス名を返します。
     * 
     * @return {@link Config} の実装クラス名
     */
    public String getClassName() {
        return className;
    }

    /**
     * {@link Config} の実装クラスのメソッド名を返します。
     * 
     * @return {@link Config} の実装クラスのメソッド名
     */
    public String getMethodName() {
        return methodName;
    }

}
