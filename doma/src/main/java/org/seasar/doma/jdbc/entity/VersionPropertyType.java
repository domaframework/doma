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

import java.util.HashMap;
import java.util.Map;

import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.BigDecimalWrapperVisitor;
import org.seasar.doma.wrapper.BigIntegerWrapper;
import org.seasar.doma.wrapper.BigIntegerWrapperVisitor;
import org.seasar.doma.wrapper.ByteWrapper;
import org.seasar.doma.wrapper.ByteWrapperVisitor;
import org.seasar.doma.wrapper.DoubleWrapper;
import org.seasar.doma.wrapper.DoubleWrapperVisitor;
import org.seasar.doma.wrapper.FloatWrapper;
import org.seasar.doma.wrapper.FloatWrapperVisitor;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.IntegerWrapperVisitor;
import org.seasar.doma.wrapper.LongWrapper;
import org.seasar.doma.wrapper.LongWrapperVisitor;
import org.seasar.doma.wrapper.NumberWrapper;
import org.seasar.doma.wrapper.ShortWrapper;
import org.seasar.doma.wrapper.ShortWrapperVisitor;
import org.seasar.doma.wrapper.Wrapper;
import org.seasar.doma.wrapper.WrapperVisitor;

/**
 * バージョンのプロパティ型です。
 * 
 * @author taedium
 * 
 */
public class VersionPropertyType<PE, E extends PE, P, V extends Number> extends
        BasicPropertyType<PE, E, P, V> {

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
            Class<?> entityPropertyClass, Class<V> valueClass,
            Class<? extends NumberWrapper<V>> wrapperClass,
            EntityPropertyType<PE, P, V> parentEntityPropertyType,
            DomainType<V, P> domainType, String name, String columnName) {
        super(entityClass, entityPropertyClass, valueClass, wrapperClass,
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
     * @param entityType
     *            エンティティのタイプ
     * @param entity
     *            エンティティ
     * @param value
     *            バージョンの値
     * @return エンティティ
     */
    public E setIfNecessary(EntityType<E> entityType, E entity, Number value) {
        return modifyValue(entityType, entity, new ValueSetter(), value);
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
    public E increment(EntityType<E> entityType, E entity) {
        return modifyValue(entityType, entity, new Incrementer(), null);
    }

    /**
     * エンティティに値を設定して返します。
     * 
     * @param entityType
     *            エンティティタイプ
     * @param entity
     *            エンティティ
     * @param visitor
     *            ビジター
     * @param value
     *            値
     * @return エンティティ
     */
    protected <PARAM> E modifyValue(EntityType<E> entityType, E entity,
            WrapperVisitor<Void, PARAM, RuntimeException> visitor, PARAM value) {
        if (entityType.isImmutable()) {
            Map<String, Accessor<E, ?, ?>> accessors = new HashMap<>();
            for (EntityPropertyType<E, ?, ?> p : entityType
                    .getEntityPropertyTypes()) {
                Accessor<E, ?, ?> accessor = p.getAccessor();
                accessor.load(entity);
                if (p == this) {
                    accessor.getWrapper().accept(visitor, value);
                }
                accessors.put(p.getName(), accessor);
            }
            return entityType.newEntity(accessors);
        } else {
            Accessor<E, ?, ?> accessor = getAccessor();
            accessor.load(entity);
            accessor.getWrapper().accept(visitor, value);
            accessor.save(entity);
            return entity;
        }
    }

    protected static class ValueSetter implements
            BigDecimalWrapperVisitor<Void, Number, RuntimeException>,
            BigIntegerWrapperVisitor<Void, Number, RuntimeException>,
            ByteWrapperVisitor<Void, Number, RuntimeException>,
            DoubleWrapperVisitor<Void, Number, RuntimeException>,
            FloatWrapperVisitor<Void, Number, RuntimeException>,
            IntegerWrapperVisitor<Void, Number, RuntimeException>,
            LongWrapperVisitor<Void, Number, RuntimeException>,
            ShortWrapperVisitor<Void, Number, RuntimeException> {

        @Override
        public Void visitBigIntegerWrapper(BigIntegerWrapper wrapper, Number p)
                throws RuntimeException {
            setIfNecessary(wrapper, p);
            return null;
        }

        @Override
        public Void visitBigDecimalWrapper(BigDecimalWrapper wrapper, Number p)
                throws RuntimeException {
            setIfNecessary(wrapper, p);
            return null;
        }

        @Override
        public Void visitByteWrapper(ByteWrapper wrapper, Number p)
                throws RuntimeException {
            setIfNecessary(wrapper, p);
            return null;
        }

        @Override
        public Void visitDoubleWrapper(DoubleWrapper wrapper, Number p)
                throws RuntimeException {
            setIfNecessary(wrapper, p);
            return null;
        }

        @Override
        public Void visitFloatWrapper(FloatWrapper wrapper, Number p)
                throws RuntimeException {
            setIfNecessary(wrapper, p);
            return null;
        }

        @Override
        public Void visitIntegerWrapper(IntegerWrapper wrapper, Number p)
                throws RuntimeException {
            setIfNecessary(wrapper, p);
            return null;
        }

        @Override
        public Void visitLongWrapper(LongWrapper wrapper, Number p)
                throws RuntimeException {
            setIfNecessary(wrapper, p);
            return null;
        }

        @Override
        public Void visitShortWrapper(ShortWrapper wrapper, Number p)
                throws RuntimeException {
            setIfNecessary(wrapper, p);
            return null;
        }

        protected void setIfNecessary(NumberWrapper<? extends Number> wrapper,
                Number value) {
            Number currentValue = wrapper.get();
            if (currentValue == null || currentValue.intValue() < 0) {
                wrapper.set(value);
            }
        }

        @Override
        public Void visitUnknownWrapper(Wrapper<?> wrapper, Number p)
                throws RuntimeException {
            return null;
        }
    }

    protected static class Incrementer implements
            BigDecimalWrapperVisitor<Void, Void, RuntimeException>,
            BigIntegerWrapperVisitor<Void, Void, RuntimeException>,
            ByteWrapperVisitor<Void, Void, RuntimeException>,
            DoubleWrapperVisitor<Void, Void, RuntimeException>,
            FloatWrapperVisitor<Void, Void, RuntimeException>,
            IntegerWrapperVisitor<Void, Void, RuntimeException>,
            LongWrapperVisitor<Void, Void, RuntimeException>,
            ShortWrapperVisitor<Void, Void, RuntimeException> {

        @Override
        public Void visitBigIntegerWrapper(BigIntegerWrapper wrapper, Void p)
                throws RuntimeException {
            increment(wrapper);
            return null;
        }

        @Override
        public Void visitBigDecimalWrapper(BigDecimalWrapper wrapper, Void p)
                throws RuntimeException {
            increment(wrapper);
            return null;
        }

        @Override
        public Void visitByteWrapper(ByteWrapper wrapper, Void p)
                throws RuntimeException {
            increment(wrapper);
            return null;
        }

        @Override
        public Void visitDoubleWrapper(DoubleWrapper wrapper, Void p)
                throws RuntimeException {
            increment(wrapper);
            return null;
        }

        @Override
        public Void visitFloatWrapper(FloatWrapper wrapper, Void p)
                throws RuntimeException {
            increment(wrapper);
            return null;
        }

        @Override
        public Void visitIntegerWrapper(IntegerWrapper wrapper, Void p)
                throws RuntimeException {
            increment(wrapper);
            return null;
        }

        @Override
        public Void visitLongWrapper(LongWrapper wrapper, Void p)
                throws RuntimeException {
            increment(wrapper);
            return null;
        }

        @Override
        public Void visitShortWrapper(ShortWrapper wrapper, Void p)
                throws RuntimeException {
            increment(wrapper);
            return null;
        }

        protected void increment(NumberWrapper<? extends Number> wrapper) {
            wrapper.increment();
        }

        @Override
        public Void visitUnknownWrapper(Wrapper<?> wrapper, Void p)
                throws RuntimeException {
            return null;
        }
    }
}
