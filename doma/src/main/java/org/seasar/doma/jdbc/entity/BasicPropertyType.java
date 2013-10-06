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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.function.Supplier;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.ConstructorUtil;
import org.seasar.doma.internal.util.FieldUtil;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.wrapper.EnumWrapper;
import org.seasar.doma.wrapper.Wrapper;

/**
 * 基本のプロパティ型です。
 * 
 * @author taedium
 * 
 */
public class BasicPropertyType<PE, E extends PE, P, V> implements
        EntityPropertyType<E, P, V> {

    /** エンティティのクラス */
    protected final Class<E> entityClass;

    /** プロパティのクラス */
    protected final Class<P> entityPropertyClass;

    /** 値のクラス */
    protected final Class<V> valueClass;

    /** ラッパーのクラス */
    protected final Class<?> wrapperClass;

    /** 親のエンティティのプロパティ型 */
    protected final EntityPropertyType<PE, P, V> parentEntityPropertyType;

    /** ドメインのメタタイプ */
    protected final DomainType<V, P> domainType;

    /** プロパティの名前 */
    protected final String name;

    /** カラム名 */
    protected final String columnName;

    /** 挿入可能かどうか */
    protected final boolean insertable;

    /** 更新可能かどうか */
    protected final boolean updatable;

    /** プロパティのフィールド */
    protected final Field field;

    /** ラッパーのファクトリ */
    protected final WrapperFactory<V> wrapperFactory;

    /** アクセサのサプライヤ */
    protected final Supplier<Accessor<E, P, V>> accessorSupplier;

    /**
     * インスタンスを構築します。
     * 
     * @param entityClass
     *            エンティティのクラス
     * @param entityPropertyClass
     *            プロパティのクラス
     * @param valueClass
     *            値のクラス
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
     * @param insertable
     *            挿入可能かどうか
     * @param updatable
     *            更新可能かどうか
     */
    @SuppressWarnings("unchecked")
    public BasicPropertyType(Class<E> entityClass,
            Class<?> entityPropertyClass, Class<V> valueClass,
            Class<?> wrapperClass,
            EntityPropertyType<PE, P, V> parentEntityPropertyType,
            DomainType<V, P> domainType, String name, String columnName,
            boolean insertable, boolean updatable) {
        if (entityClass == null) {
            throw new DomaNullPointerException("entityClass");
        }
        if (entityPropertyClass == null) {
            throw new DomaNullPointerException("entityPropertyClass");
        }
        if (valueClass == null) {
            throw new DomaNullPointerException("valueClass");
        }
        if (wrapperClass == null) {
            throw new DomaNullPointerException("wrapperClass");
        }
        if (name == null) {
            throw new DomaNullPointerException("name");
        }
        if (columnName == null) {
            throw new DomaNullPointerException("columnName");
        }
        this.entityClass = entityClass;
        this.entityPropertyClass = (Class<P>) entityPropertyClass;
        this.valueClass = valueClass;
        this.wrapperClass = wrapperClass;
        this.parentEntityPropertyType = parentEntityPropertyType;
        this.domainType = domainType;
        this.name = name;
        this.columnName = columnName;
        this.insertable = insertable;
        this.updatable = updatable;
        this.field = parentEntityPropertyType == null ? getField() : null;
        this.wrapperFactory = createWrapperFactory();
        this.accessorSupplier = createPropertyAccessorSupplier();
    }

    private WrapperFactory<V> createWrapperFactory() {
        if (valueClass.isEnum()) {
            return new EnumWrapperFactory();
        }
        return new SimpleWrapperFactory();
    }

    private Supplier<Accessor<E, P, V>> createPropertyAccessorSupplier() {
        if (parentEntityPropertyType != null) {
            return () -> new ParentPropertyAccessor();
        }
        if (domainType != null) {
            return () -> new DomainPropertyAccessor();
        }
        return () -> new ValuePropertyAccessor();
    }

    private Field getField() {
        Field field;
        try {
            field = ClassUtil.getDeclaredField(entityClass, name);
        } catch (WrapException wrapException) {
            throw new EntityPropertyNotFoundException(wrapException.getCause(),
                    entityClass.getName(), name);
        }
        if (!FieldUtil.isPublic(field)) {
            try {
                FieldUtil.setAccessible(field, true);
            } catch (WrapException wrapException) {
                throw new EntityPropertyAccessException(
                        wrapException.getCause(), entityClass.getName(), name);
            }
        }
        return field;
    }

    @Override
    public Accessor<E, P, V> getAccessor() {
        return accessorSupplier.get();
    }

    @Override
    public void copy(E destEntity, E srcEntity) {
        Accessor<E, P, V> dest = getAccessor();
        dest.load(destEntity);
        Accessor<E, P, V> src = getAccessor();
        src.load(srcEntity);
        dest.getWrapper().set(src.getWrapper().getCopy());
        dest.save(destEntity);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public boolean isId() {
        return false;
    }

    @Override
    public boolean isVersion() {
        return false;
    }

    @Override
    public boolean isInsertable() {
        return insertable;
    }

    @Override
    public boolean isUpdatable() {
        return updatable;
    }

    /**
     * ラッパーのファクトリです。
     * 
     * @author taedium
     * 
     * @param <V>
     *            値の型
     * 
     * @since 1.20.0
     */
    protected interface WrapperFactory<V> {
        Wrapper<V> getWrapper();
    }

    /**
     * 単純なラッパーのファクトリです。
     * 
     * @author taedium
     * 
     * @since 1.20.0
     */
    protected class SimpleWrapperFactory implements WrapperFactory<V> {

        protected final Constructor<? extends Wrapper<V>> constructor;

        @SuppressWarnings("unchecked")
        protected SimpleWrapperFactory() {
            try {
                constructor = (Constructor<? extends Wrapper<V>>) ClassUtil
                        .getConstructor(wrapperClass);
            } catch (WrapException wrapException) {
                throw new WrapperConstructorNotFoundException(
                        wrapException.getCause(), wrapperClass.getName());
            }
        }

        @Override
        public Wrapper<V> getWrapper() {
            try {
                return ConstructorUtil.newInstance(constructor);
            } catch (WrapException wrapException) {
                throw new WrapperInstantiationException(
                        wrapException.getCause(), wrapperClass.getName());
            }
        }
    }

    /**
     * 列挙型のラッパーのファクトリです。
     * 
     * @author taedium
     * 
     */
    protected class EnumWrapperFactory implements WrapperFactory<V> {

        @SuppressWarnings("rawtypes")
        protected final Constructor<EnumWrapper> constructor;

        protected EnumWrapperFactory() {
            try {
                this.constructor = ClassUtil.getConstructor(EnumWrapper.class,
                        Class.class);
            } catch (WrapException wrapException) {
                throw new WrapperConstructorNotFoundException(
                        wrapException.getCause(), EnumWrapper.class.getName());
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public Wrapper<V> getWrapper() {
            try {
                return ConstructorUtil.newInstance(constructor, valueClass);
            } catch (WrapException wrapException) {
                throw new WrapperInstantiationException(
                        wrapException.getCause(), EnumWrapper.class.getName());
            }
        }
    }

    /**
     * 親プロパティのアクセサです。
     * 
     * @author nakamura-to
     */
    protected class ParentPropertyAccessor implements Accessor<E, P, V> {

        protected final Accessor<PE, P, V> delegate;

        protected ParentPropertyAccessor() {
            this.delegate = parentEntityPropertyType.getAccessor();
        }

        @Override
        public P get() {
            return delegate.get();
        }

        @Override
        public ParentPropertyAccessor set(P value) {
            delegate.set(value);
            return this;
        }

        @Override
        public ParentPropertyAccessor load(E entity) {
            delegate.load(entity);
            return this;
        }

        @Override
        public ParentPropertyAccessor save(E entity) {
            delegate.save(entity);
            return this;
        }

        @Override
        public Wrapper<V> getWrapper() {
            return delegate.getWrapper();
        }

    }

    /**
     * ドメインプロパティのアクセサです。
     * 
     * @author nakamura-to
     */
    protected class DomainPropertyAccessor implements Accessor<E, P, V> {

        protected final Wrapper<V> wrapper;

        protected DomainPropertyAccessor() {
            this.wrapper = wrapperFactory.getWrapper();
        }

        @Override
        public P get() {
            V value = wrapper.get();
            V actualValue = (field.getType().isPrimitive() && value == null) ? wrapper
                    .getDefault() : value;
            return domainType.newDomain(actualValue);
        }

        @Override
        public DomainPropertyAccessor set(P domain) {
            wrapper.set(domainType.getWrapper(domain).get());
            return this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public DomainPropertyAccessor load(E entity) {
            try {
                P domain = (P) FieldUtil.get(field, entity);
                wrapper.set(domainType.getWrapper(domain).get());
            } catch (WrapException wrapException) {
                throw new EntityPropertyAccessException(
                        wrapException.getCause(), entityClass.getName(), name);
            }
            return this;
        }

        @Override
        public DomainPropertyAccessor save(E entity) {
            V value = wrapper.get();
            V actualValue = (field.getType().isPrimitive() && value == null) ? wrapper
                    .getDefault() : value;
            P domain = domainType.newDomain(actualValue);
            try {
                FieldUtil.set(field, entity, domain);
            } catch (WrapException wrapException) {
                throw new EntityPropertyAccessException(
                        wrapException.getCause(), entityClass.getName(), name);
            }
            return this;
        }

        @Override
        public Wrapper<V> getWrapper() {
            return wrapper;
        }
    }

    /**
     * 値プロパティのアクセサです。
     * 
     * @author nakamura-to
     */
    protected class ValuePropertyAccessor implements Accessor<E, P, V> {

        protected final Wrapper<V> wrapper;

        protected ValuePropertyAccessor() {
            this.wrapper = wrapperFactory.getWrapper();
        }

        @Override
        public P get() {
            return entityPropertyClass.cast(wrapper.get());
        }

        @Override
        public ValuePropertyAccessor set(P value) {
            wrapper.set(valueClass.cast(value));
            return this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public ValuePropertyAccessor load(E entity) {
            try {
                V value = (V) FieldUtil.get(field, entity);
                wrapper.set(value);
            } catch (WrapException wrapException) {
                throw new EntityPropertyAccessException(
                        wrapException.getCause(), entityClass.getName(), name);
            }
            return this;
        }

        @Override
        public ValuePropertyAccessor save(E entity) {
            V value = wrapper.get();
            V actualValue = (field.getType().isPrimitive() && value == null) ? wrapper
                    .getDefault() : value;
            try {
                FieldUtil.set(field, entity, actualValue);
            } catch (WrapException wrapException) {
                throw new EntityPropertyAccessException(
                        wrapException.getCause(), entityClass.getName(), name);
            }
            return this;
        }

        @Override
        public Wrapper<V> getWrapper() {
            return wrapper;
        }

    }
}
