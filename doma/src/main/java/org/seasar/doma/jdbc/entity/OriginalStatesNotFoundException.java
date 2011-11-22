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
package org.seasar.doma.jdbc.entity;

import org.seasar.doma.OriginalStates;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * {@link OriginalStates}が注釈されたフィールドがエンティティクラスで見つからない場合にスローされる例外です。
 * 
 * @author taedium
 * @since 1.20.0
 */
public class OriginalStatesNotFoundException extends JdbcException {

    private static final long serialVersionUID = 1L;

    private final String entityClassName;

    private final String fieldName;

    /**
     * インスタンスを構築します。
     * 
     * @param cause
     *            原因
     * @param entityClassName
     *            エンティティクラスの名前
     * @param fieldName
     *            フィールドの名前
     */
    public OriginalStatesNotFoundException(Throwable cause,
            String entityClassName, String fieldName) {
        super(Message.DOMA2213, cause, entityClassName, fieldName, cause);
        this.entityClassName = entityClassName;
        this.fieldName = fieldName;
    }

    /**
     * エンティティクラスの名前を返します。
     * 
     * @return エンティティクラスの名前
     */
    public String getEntityClassName() {
        return entityClassName;
    }

    /**
     * フィールドの名前を返します。
     * 
     * @return フィールドの名前
     */
    public String getFieldName() {
        return fieldName;
    }
}
