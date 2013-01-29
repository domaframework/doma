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

import org.seasar.doma.jdbc.domain.DomainType;

/**
 * アプリケーションにより割り当てられる識別子のプロパティ型です。
 * 
 * @author taedium
 * 
 */
public class AssignedIdPropertyType<PE, E extends PE, V, D> extends
        BasicPropertyType<PE, E, V, D> {

    /**
     * インスタンスを構築します。
     * 
     * @param entityClass
     *            エンティティのクラス
     * @param entityPropertyClass
     *            プロパティのクラス
     * @param wrapperClass
     *            ラッパーのクラス
     * @param parentEntityPropertyType
     *            親のエンティティのプロパティ型、親のエンティティを持たない場合 {@code null}
     * @param domainType
     *            ドメインのメタタイプ、ドメインでない場合 {@code null}
     * @param name
     *            プロパティの名前
     * @param columnName
     *            カラム名
     */
    public AssignedIdPropertyType(Class<E> entityClass,
            Class<V> entityPropertyClass, Class<?> wrapperClass,
            EntityPropertyType<PE, V> parentEntityPropertyType,
            DomainType<V, D> domainType, String name, String columnName) {
        super(entityClass, entityPropertyClass, wrapperClass,
                parentEntityPropertyType, domainType, name, columnName, true,
                true);
    }

    @Override
    public boolean isId() {
        return true;
    }

}
