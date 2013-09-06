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

import java.util.Map;

import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.wrapper.NumberWrapper;

/**
 * バージョンのプロパティ型です。
 * 
 * @author taedium
 * 
 */
public class VersionPropertyType<PE, E extends PE, V extends Number, D> extends
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
    public VersionPropertyType(Class<E> entityClass,
            Class<V> entityPropertyClass,
            Class<? extends NumberWrapper<V>> wrapperClass,
            EntityPropertyType<PE, V> parentEntityPropertyType,
            DomainType<V, D> domainType, String name, String columnName) {
        super(entityClass, entityPropertyClass, wrapperClass,
                parentEntityPropertyType, domainType, name, columnName, true,
                true);
    }

    @Override
    public boolean isVersion() {
        return true;
    }

    /**
     * 必要であればバージョンの値を設定します。
     * 
     * @param entity
     *            エンティティ
     * @param value
     *            バージョンの値
     */
    public void setIfNecessary(E entity, Number value) {
        NumberWrapper<V> wrapper = (NumberWrapper<V>) getWrapper(entity);
        V currentValue = wrapper.get();
        if (currentValue == null || currentValue.intValue() < 0) {
            wrapper.set(value);
        }
    }

    /**
     * 必要であればバージョンの値を設定し、新しいエンティティを返します。
     * 
     * @param entity
     *            エンティティ
     * @param value
     *            バージョンの値
     * @param entityType
     *            エンティティタイプ
     * @since 1.34.0
     */
    public E setIfNecessaryAndMakeNewEntity(E entity, Number value,
            EntityType<E> entityType) {
        NumberWrapper<V> getter = (NumberWrapper<V>) getWrapper(entity);
        V currentValue = getter.get();
        if (currentValue == null || currentValue.intValue() < 0) {
            Map<String, Object> properties = entityType.getCopy(entity);
            NumberWrapper<V> setter = (NumberWrapper<V>) getWrapper(properties);
            setter.set(value);
            return entityType.newEntity(properties);
        }
        return null;
    }

    /**
     * バージョン番号をインクリメントします。
     * 
     * @param entity
     *            エンティティ
     */
    public void increment(E entity) {
        NumberWrapper<V> wrapper = (NumberWrapper<V>) getWrapper(entity);
        wrapper.increment();
    }

    /**
     * バージョン番号をインクリメントして新しいエンティティを返します。
     * 
     * @param entity
     *            エンティティ
     * @param entityType
     *            エンティティタイプ
     * @return 新しいエンティティ
     * @since 1.34.0
     */
    public E incrementAndNewEntity(E entity, EntityType<E> entityType) {
        NumberWrapper<V> getter = (NumberWrapper<V>) getWrapper(entity);
        V value = getter.getIncrementedValue();
        Map<String, Object> properties = entityType.getCopy(entity);
        NumberWrapper<V> setter = (NumberWrapper<V>) getWrapper(properties);
        setter.set(value);
        return entityType.newEntity(properties);
    }

}
