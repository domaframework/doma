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

import java.util.function.Supplier;

import org.seasar.doma.jdbc.holder.HolderType;
import org.seasar.doma.wrapper.NumberWrapper;
import org.seasar.doma.wrapper.NumberWrapperVisitor;
import org.seasar.doma.wrapper.Wrapper;

/**
 * バージョンのプロパティ型です。
 * 
 * @author nakamura-to
 * 
 * @param <ENTITY>
 *            エンティティの型
 * @param <BASIC>
 *            プロパティの基本型
 * @param <HOLDER>
 *            プロパティのドメイン型
 */
public class VersionPropertyType<ENTITY, BASIC extends Number, HOLDER>
        extends DefaultPropertyType<ENTITY, BASIC, HOLDER> {

    /**
     * インスタンスを構築します。
     * 
     * @param entityClass
     *            エンティティのクラス
     * @param entityPropertyClass
     *            プロパティのクラス
     * @param wrapperSupplier
     *            ラッパーのサプライヤ
     * @param holderType
     *            ドメインのメタタイプ、ドメインでない場合 {@code null}
     * @param name
     *            プロパティの名前
     * @param columnName
     *            カラム名
     * @param namingType
     *            ネーミング規約
     * @param quoteRequired
     *            カラム名に引用符が必要とされるかどうか
     */
    public VersionPropertyType(Class<ENTITY> entityClass,
            Class<?> entityPropertyClass,
            Supplier<Wrapper<BASIC>> wrapperSupplier,
            HolderType<BASIC, HOLDER> holderType, String name,
            String columnName, NamingType namingType, boolean quoteRequired) {
        super(entityClass, entityPropertyClass, wrapperSupplier,
                holderType, name, columnName,
                namingType, true, true, quoteRequired);
    }

    @Override
    public boolean isVersion() {
        return true;
    }

    /**
     * 必要であればバージョンの値を設定します。
     * 
     * @param entityType
     *            エンティティのタイプ
     * @param entity
     *            エンティティ
     * @param value
     *            バージョンの値
     * @return エンティティ
     */
    public ENTITY setIfNecessary(EntityType<ENTITY> entityType, ENTITY entity,
            Number value) {
        return modifyIfNecessary(entityType, entity, new ValueSetter(), value);
    }

    /**
     * バージョン番号をインクリメントします。
     * 
     * @param entityType
     *            エンティティのタイプ
     * @param entity
     *            エンティティ
     * @return エンティティ
     */
    public ENTITY increment(EntityType<ENTITY> entityType, ENTITY entity) {
        return modifyIfNecessary(entityType, entity, new Incrementer(), null);
    }

    protected static class ValueSetter implements
            NumberWrapperVisitor<Boolean, Number, Void, RuntimeException> {

        @Override
        public <V extends Number> Boolean visitNumberWrapper(
                NumberWrapper<V> wrapper, Number value, Void q) {
            Number currentValue = wrapper.get();
            if (currentValue == null || currentValue.intValue() < 0) {
                wrapper.set(value);
                return true;
            }
            return false;
        }
    }

    protected static class Incrementer implements
            NumberWrapperVisitor<Boolean, Void, Void, RuntimeException> {

        @Override
        public <V extends Number> Boolean visitNumberWrapper(
                NumberWrapper<V> wrapper, Void p, Void q) {
            wrapper.increment();
            return true;
        }
    }

}
