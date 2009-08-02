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
package org.seasar.doma.bean;

import org.seasar.doma.message.DomaMessageCode;

/**
 * プロパティの値の設定に失敗した場合にスローされる例外です。
 * 
 * @author taedium
 * 
 */
public class PropertyWriteAccessException extends BeanException {

    private static final long serialVersionUID = 1L;

    /** クラス名 */
    protected final String className;

    /** プロパティ名 */
    protected final String propertyName;

    /**
     * インスタンスを構築します。
     * 
     * @param className
     *            クラス名
     * @param propertyName
     *            プロパティ名
     * @param cause
     *            原因
     */
    public PropertyWriteAccessException(String className, String propertyName,
            Throwable cause) {
        super(DomaMessageCode.DOMA6001, cause, className, propertyName, cause);
        this.className = className;
        this.propertyName = propertyName;
    }

    public String getClassName() {
        return className;
    }

    public String getPropertyName() {
        return propertyName;
    }

}
