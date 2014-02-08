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

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * エンティティプロパティがエンティティクラスに定義されていない場合にスローされる例外です。
 * 
 * @author taedium
 * @since 1.11.0
 */
public class EntityPropertyNotDefinedException extends JdbcException {

    private static final long serialVersionUID = 1L;

    private final String entityClassName;

    private final String entityPropertyName;

    /**
     * インスタンスを構築します。
     * 
     * @param entityClassName
     *            エンティティクラスの名前
     * @param entityPropertyName
     *            エンティティプロパティの名前
     */
    public EntityPropertyNotDefinedException(String entityClassName,
            String entityPropertyName) {
        super(Message.DOMA2207, entityClassName, entityPropertyName);
        this.entityClassName = entityClassName;
        this.entityPropertyName = entityPropertyName;
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
     * エンティティプロパティの名前を返します。
     * 
     * @return エンティティプロパティの名前
     */
    public String getEntityPropertyName() {
        return entityPropertyName;
    }
}
