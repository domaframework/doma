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
import java.util.Map;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.criteria.CriterionVisitor;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.ConstructorUtil;
import org.seasar.doma.internal.util.FieldUtil;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.wrapper.Accessor;
import org.seasar.doma.wrapper.EnumWrapper;
import org.seasar.doma.wrapper.Wrapper;

/**
 * 基本のプロパティ型です。
 * 
 * @author taedium
 * 
 */
public class BasicPropertyType<PE, E extends PE, V, D> implements
        EntityPropertyType<E, V> {

    /** エンティティのクラス */
    protected final Class<E> entityClass;

    /** プロパティのクラス */
    protected final Class<V> entityPropertyClass;

    /** ラッパーのクラス */
    protected final Class<?> wrapperClass;

    /** 親のエンティティのプロパティ型 */
    protected final EntityPropertyType<PE, V> parentEntityPropertyType;

    /** ドメインのメタタイプ */
    protected final DomainType<V, D> domainType;

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

    /** ラッパーのアクセサのファクトリ */
    protected final AccessorFactory<E, V> accessorFactory;

    /** ラッパーのMapアクセサのファクトリ */
    protected final MapAccessorFactory<V> mapAccessorFactory;

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
     * @param insertable
     *            挿入可能かどうか
     * @param updatable
     *            更新可能かどうか
     */
    public BasicPropertyType(Class<E> entityClass,
            Class<V> entityPropertyClass, Class<?> wrapperClass,
            EntityPropertyType<PE, V> parentEntityPropertyType,
            DomainType<V, D> domainType, String name, String columnName,
            boolean insertable, boolean updatable) {
        if (entityClass == null) {
            throw new DomaNullPointerException("entityClass");
        }
        if (entityPropertyClass == null) {
            throw new DomaNullPointerException("entityPropertyClass");
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
        this.entityPropertyClass = entityPropertyClass;
        this.wrapperClass = wrapperClass;
        this.parentEntityPropertyType = parentEntityPropertyType;
        this.domainType = domainType;
        this.name = name;
        this.columnName = columnName;
        this.insertable = insertable;
        this.updatable = updatable;
        this.field = parentEntityPropertyType == null ? getField() : null;
        this.wrapperFactory = createWrapperFactory();
        this.accessorFactory = createAccessorFactory(field);
        this.mapAccessorFactory = createMapAccessorFactory(field);
    }

    private WrapperFactory<V> createWrapperFactory() {
        if (entityPropertyClass.isEnum()) {
            return new EnumWrapperFactory<V>(entityPropertyClass);
        }
        return new SimpleWrapperFactory<V>(wrapperClass);
    }

    private AccessorFactory<E, V> createAccessorFactory(Field field) {
        if (parentEntityPropertyType != null) {
            return new ParentValueAccessorFactory<PE, E, V>(
                    parentEntityPropertyType);
        }
        if (domainType != null) {
            return new DomainAccessorFactory<E, V, D>(entityClass.getName(),
                    name, field, domainType);
        }
        return new ValueAccessorFactory<E, V>(entityClass.getName(), name,
                field);
    }

    private MapAccessorFactory<V> createMapAccessorFactory(Field field) {
        if (parentEntityPropertyType != null) {
            return new ParentValueMapAccessorFactory<PE, E, V>(
                    parentEntityPropertyType);
        }
        if (domainType != null) {
            return new DomainMapAccessorFactory<V, D>(entityClass.getName(),
                    name, field, domainType);
        }
        return new ValueMapAccessorFactory<V>(entityClass.getName(), name,
                field);
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
    public Object getCopy(E entity) {
        Wrapper<V> wrapper = getWrapper(entity);
        V value = wrapper.getCopy();
        if (domainType != null) {
            return domainType.newDomain(value);
        }
        return value;
    }

    /**
     * 値のラッパーを返します。
     * 
     * @param entity
     *            エンティティ
     * @return 値のラッパー
     */
    @Override
    public Wrapper<V> getWrapper(E entity) {
        Wrapper<V> wrapper = wrapperFactory.getWrapper();
        Accessor<V> accessor = accessorFactory.getAccessor(entity, wrapper);
        wrapper.setAccessor(accessor);
        return wrapper;
    }

    @Override
    public Wrapper<V> getWrapper(Map<String, Object> properties) {
        Wrapper<V> wrapper = wrapperFactory.getWrapper();
        Accessor<V> accessor = mapAccessorFactory.getAccessor(properties,
                wrapper);
        wrapper.setAccessor(accessor);
        return wrapper;
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

    @Override
    public Class<V> getType() {
        return entityPropertyClass;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CriterionVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (visitor instanceof EntityPropertyTypeVisitor<?, ?, ?>) {
            EntityPropertyTypeVisitor<R, P, TH> v = (EntityPropertyTypeVisitor<R, P, TH>) visitor;
            return v.visitEntityPropertyType(this, p);
        }
        return visitor.visitUnknownExpression(this, p);
    }

    /**
     * ラッパーのアクセサのファクトリです。
     * 
     * @author taedium
     * 
     * @param <E>
     *            エンティティの型
     * @param <V>
     *            値の型
     * 
     * @since 1.20.0
     */
    protected interface AccessorFactory<E, V> {
        Accessor<V> getAccessor(E entity, Wrapper<V> wrapper);
    }

    protected interface MapAccessorFactory<V> {
        Accessor<V> getAccessor(Map<String, Object> properties,
                Wrapper<V> wrapper);
    }

    /**
     * 値のアクセサのファクトリです。
     * 
     * @author taedium
     * 
     * @param <E>
     *            エンティティの型
     * @param <V>
     *            値の型
     * @since 1.20.0
     */
    protected static class ValueAccessorFactory<E, V> implements
            AccessorFactory<E, V> {

        protected final String entityClassName;

        protected final String entityPropertyName;

        protected final Field field;

        protected final boolean primitive;

        protected ValueAccessorFactory(String entityClassName,
                String entityPropertyName, Field field) {
            if (entityClassName == null) {
                throw new DomaNullPointerException("entityClassName");
            }
            if (entityPropertyName == null) {
                throw new DomaNullPointerException("entityPropertyName");
            }
            if (field == null) {
                throw new DomaNullPointerException("field");
            }
            this.entityClassName = entityClassName;
            this.entityPropertyName = entityPropertyName;
            this.field = field;
            this.primitive = field.getType().isPrimitive();
        }

        @Override
        public Accessor<V> getAccessor(final E entity, final Wrapper<V> wrapper) {
            if (entity == null) {
                throw new DomaNullPointerException("entity");
            }
            if (wrapper == null) {
                throw new DomaNullPointerException("wrapper");
            }
            return new Accessor<V>() {

                @SuppressWarnings("unchecked")
                @Override
                public V get() {
                    try {
                        return (V) FieldUtil.get(field, entity);
                    } catch (WrapException wrapException) {
                        throw new EntityPropertyAccessException(
                                wrapException.getCause(), entityClassName,
                                entityPropertyName);
                    }
                }

                @Override
                public void set(V value) {
                    V actualValue = (primitive && value == null) ? wrapper
                            .getDefault() : value;
                    try {
                        FieldUtil.set(field, entity, actualValue);
                    } catch (WrapException wrapException) {
                        throw new EntityPropertyAccessException(
                                wrapException.getCause(), entityClassName,
                                entityPropertyName);
                    }
                }
            };
        }
    }

    /**
     * 親の値のアクセサのファクトリです。
     * 
     * @author taedium
     * @param <PE>
     *            親エンティティの型
     * @param <E>
     *            エンティティの型
     * @param <V>
     *            値の型
     * @since 1.20.0
     */
    protected static class ParentValueAccessorFactory<PE, E extends PE, V>
            implements AccessorFactory<E, V> {

        protected final EntityPropertyType<PE, V> parentEntityPropertyType;

        protected ParentValueAccessorFactory(
                EntityPropertyType<PE, V> parentEntityPropertyType) {
            if (parentEntityPropertyType == null) {
                throw new DomaNullPointerException("parentEntityPropertyType");
            }
            this.parentEntityPropertyType = parentEntityPropertyType;
        }

        @Override
        public Accessor<V> getAccessor(final E entity, final Wrapper<V> wrapper) {
            if (entity == null) {
                throw new DomaNullPointerException("entity");
            }
            if (wrapper == null) {
                throw new DomaNullPointerException("wrapper");
            }
            return new Accessor<V>() {

                @Override
                public V get() {
                    return parentEntityPropertyType.getWrapper(entity).get();
                }

                @Override
                public void set(V value) {
                    parentEntityPropertyType.getWrapper(entity).set(value);
                }
            };
        }
    }

    /**
     * ドメインのアクセサのファクトリです。
     * 
     * @author taedium
     * 
     * @param <E>
     *            エンティティの型
     * @param <V>
     *            値の型
     * @param <D>
     *            ドメインの型
     * @since 1.20.0
     */
    protected static class DomainAccessorFactory<E, V, D> implements
            AccessorFactory<E, V> {

        protected final String entityClassName;

        protected final String entityPropertyName;

        protected final Field field;

        protected final boolean primitive;

        protected final DomainType<V, D> domainType;

        protected DomainAccessorFactory(String entityClassName,
                String entityPropertyName, Field field,
                DomainType<V, D> domainType) {
            if (entityClassName == null) {
                throw new DomaNullPointerException("entityClassName");
            }
            if (entityPropertyName == null) {
                throw new DomaNullPointerException("entityPropertyName");
            }
            if (field == null) {
                throw new DomaNullPointerException("field");
            }
            if (domainType == null) {
                throw new DomaNullPointerException("domainType");
            }
            this.entityClassName = entityClassName;
            this.entityPropertyName = entityPropertyName;
            this.field = field;
            this.primitive = field.getType().isPrimitive();
            this.domainType = domainType;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Accessor<V> getAccessor(final E entity, final Wrapper<V> wrapper) {
            if (entity == null) {
                throw new DomaNullPointerException("entity");
            }
            if (wrapper == null) {
                throw new DomaNullPointerException("wrapper");
            }
            return new Accessor<V>() {

                @Override
                public V get() {
                    D domain;
                    try {
                        domain = (D) FieldUtil.get(field, entity);
                    } catch (WrapException wrapException) {
                        throw new EntityPropertyAccessException(
                                wrapException.getCause(), entityClassName,
                                entityPropertyName);
                    }
                    return domainType.getWrapper(domain).get();
                }

                @Override
                public void set(V value) {
                    V actualValue = (primitive && value == null) ? wrapper
                            .getDefault() : value;
                    Object domain = domainType.newDomain(actualValue);
                    try {
                        FieldUtil.set(field, entity, domain);
                    } catch (WrapException wrapException) {
                        throw new EntityPropertyAccessException(
                                wrapException.getCause(), entityClassName,
                                entityPropertyName);
                    }
                }
            };
        }
    }

    /**
     * 値のMap用アクセサのファクトリです。
     * 
     * @author taedium
     * 
     * @param <V>
     *            値の型
     * @since 1.34.0
     */
    protected static class ValueMapAccessorFactory<V> implements
            MapAccessorFactory<V> {

        protected final String entityClassName;

        protected final String entityPropertyName;

        protected final Field field;

        protected final boolean primitive;

        protected ValueMapAccessorFactory(String entityClassName,
                String entityPropertyName, Field field) {
            if (entityClassName == null) {
                throw new DomaNullPointerException("entityClassName");
            }
            if (entityPropertyName == null) {
                throw new DomaNullPointerException("entityPropertyName");
            }
            if (field == null) {
                throw new DomaNullPointerException("field");
            }
            this.entityClassName = entityClassName;
            this.entityPropertyName = entityPropertyName;
            this.field = field;
            this.primitive = field.getType().isPrimitive();
        }

        @Override
        public Accessor<V> getAccessor(final Map<String, Object> properties,
                final Wrapper<V> wrapper) {
            if (properties == null) {
                throw new DomaNullPointerException("properties");
            }
            if (wrapper == null) {
                throw new DomaNullPointerException("wrapper");
            }
            return new Accessor<V>() {

                @SuppressWarnings("unchecked")
                @Override
                public V get() {
                    return (V) properties.get(entityPropertyName);
                }

                @Override
                public void set(V value) {
                    V actualValue = (primitive && value == null) ? wrapper
                            .getDefault() : value;
                    properties.put(entityPropertyName, actualValue);
                }
            };
        }
    }

    /**
     * 親の値のMap用アクセサのファクトリです。
     * 
     * @author taedium
     * @param <PE>
     *            親エンティティの型
     * @param <E>
     *            エンティティの型
     * @param <V>
     *            値の型
     * @since 1.34.0
     */
    protected static class ParentValueMapAccessorFactory<PE, E extends PE, V>
            implements MapAccessorFactory<V> {

        protected final EntityPropertyType<PE, V> parentEntityPropertyType;

        protected ParentValueMapAccessorFactory(
                EntityPropertyType<PE, V> parentEntityPropertyType) {
            if (parentEntityPropertyType == null) {
                throw new DomaNullPointerException("parentEntityPropertyType");
            }
            this.parentEntityPropertyType = parentEntityPropertyType;
        }

        @Override
        public Accessor<V> getAccessor(final Map<String, Object> properties,
                final Wrapper<V> wrapper) {
            if (properties == null) {
                throw new DomaNullPointerException("properties");
            }
            if (wrapper == null) {
                throw new DomaNullPointerException("wrapper");
            }
            return new Accessor<V>() {

                @Override
                public V get() {
                    return parentEntityPropertyType.getWrapper(properties)
                            .get();
                }

                @Override
                public void set(V value) {
                    parentEntityPropertyType.getWrapper(properties).set(value);
                }
            };
        }
    }

    /**
     * ドメインのMap用アクセサのファクトリです。
     * 
     * @author taedium
     * 
     * @param <V>
     *            値の型
     * @param <D>
     *            ドメインの型
     * @since 1.34.0
     */
    protected static class DomainMapAccessorFactory<V, D> implements
            MapAccessorFactory<V> {

        protected final String entityClassName;

        protected final String entityPropertyName;

        protected final Field field;

        protected final boolean primitive;

        protected final DomainType<V, D> domainType;

        protected DomainMapAccessorFactory(String entityClassName,
                String entityPropertyName, Field field,
                DomainType<V, D> domainType) {
            if (entityClassName == null) {
                throw new DomaNullPointerException("entityClassName");
            }
            if (entityPropertyName == null) {
                throw new DomaNullPointerException("entityPropertyName");
            }
            if (field == null) {
                throw new DomaNullPointerException("field");
            }
            if (domainType == null) {
                throw new DomaNullPointerException("domainType");
            }
            this.entityClassName = entityClassName;
            this.entityPropertyName = entityPropertyName;
            this.field = field;
            this.primitive = field.getType().isPrimitive();
            this.domainType = domainType;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Accessor<V> getAccessor(final Map<String, Object> properties,
                final Wrapper<V> wrapper) {
            if (properties == null) {
                throw new DomaNullPointerException("properties");
            }
            if (wrapper == null) {
                throw new DomaNullPointerException("wrapper");
            }
            return new Accessor<V>() {

                @Override
                public V get() {
                    D domain;
                    domain = (D) properties.get(entityPropertyName);
                    return domainType.getWrapper(domain).get();
                }

                @Override
                public void set(V value) {
                    V actualValue = (primitive && value == null) ? wrapper
                            .getDefault() : value;
                    Object domain = domainType.newDomain(actualValue);
                    properties.put(entityPropertyName, domain);
                }
            };
        }
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
     * @param <V>
     *            値の型
     * 
     * @since 1.20.0
     */
    protected static class SimpleWrapperFactory<V> implements WrapperFactory<V> {

        protected final Class<?> wrapperClass;

        protected final Constructor<? extends Wrapper<V>> constructor;

        @SuppressWarnings("unchecked")
        protected SimpleWrapperFactory(Class<?> wrapperClass) {
            if (wrapperClass == null) {
                throw new DomaNullPointerException("wrapperClass");
            }
            this.wrapperClass = wrapperClass;
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
     * @param <V>
     *            値の型
     */
    protected static class EnumWrapperFactory<V> implements WrapperFactory<V> {

        protected final Class<V> enumClass;

        @SuppressWarnings("rawtypes")
        protected final Constructor<EnumWrapper> constructor;

        protected EnumWrapperFactory(Class<V> enumClass) {
            if (enumClass == null) {
                throw new DomaNullPointerException("enumClass");
            }
            this.enumClass = enumClass;
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
                return ConstructorUtil.newInstance(constructor, enumClass);
            } catch (WrapException wrapException) {
                throw new WrapperInstantiationException(
                        wrapException.getCause(), EnumWrapper.class.getName());
            }
        }
    }
}
