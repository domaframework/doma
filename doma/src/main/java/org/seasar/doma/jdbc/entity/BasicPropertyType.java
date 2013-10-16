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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.FieldUtil;
import org.seasar.doma.internal.wrapper.Holder;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.wrapper.Wrapper;
import org.seasar.doma.wrapper.WrapperVisitor;

/**
 * 基本のプロパティ型です。
 * 
 * @author taedium
 * 
 */
public class BasicPropertyType<PE, E extends PE, P, V, D> implements
        EntityPropertyType<E, P, V> {

    /** エンティティのクラス */
    protected final Class<E> entityClass;

    /** プロパティのクラス */
    protected final Class<P> entityPropertyClass;

    /** 値のクラス */
    protected final Class<V> valueClass;

    /** ラッパーのサプライヤ */
    protected final Supplier<Wrapper<V>> wrapperSupplier;

    /** 親のエンティティのプロパティ型 */
    protected final EntityPropertyType<PE, P, V> parentEntityPropertyType;

    /** ドメインのメタタイプ */
    protected final DomainType<V, D> domainType;

    protected final boolean isOptional;

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

    /** アクセサのサプライヤ */
    protected final Supplier<PropertyState<E, V>> accessorSupplier;

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
            Supplier<Wrapper<V>> wrapperSupplier,
            EntityPropertyType<PE, P, V> parentEntityPropertyType,
            DomainType<V, D> domainType, String name, String columnName,
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
        if (wrapperSupplier == null) {
            throw new DomaNullPointerException("wrapperSupplier");
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
        this.wrapperSupplier = wrapperSupplier;
        this.parentEntityPropertyType = parentEntityPropertyType;
        this.domainType = domainType;
        this.isOptional = entityPropertyClass == Optional.class;
        this.name = name;
        this.columnName = columnName;
        this.insertable = insertable;
        this.updatable = updatable;
        this.field = parentEntityPropertyType == null ? getField() : null;
        this.accessorSupplier = createPropertyAccessorSupplier();
    }

    private Supplier<PropertyState<E, V>> createPropertyAccessorSupplier() {
        if (parentEntityPropertyType != null) {
            return () -> new ParentPropertyState();
        }
        if (domainType != null) {
            if (isOptional) {
                return () -> new BasicPropertyState<Optional<D>>(
                        () -> domainType.createOptionalHolder());
            } else {
                return () -> new BasicPropertyState<D>(
                        () -> domainType.createHolder());
            }
        }
        if (isOptional) {
            return () -> new BasicPropertyState<Optional<V>>(
                    () -> new org.seasar.doma.internal.wrapper.OptionalBasicHolder<>(
                            wrapperSupplier));
        } else {
            return () -> new BasicPropertyState<V>(
                    () -> new org.seasar.doma.internal.wrapper.BasicHolder<>(
                            wrapperSupplier, field.getClass().isPrimitive()));
        }
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
    public PropertyState<E, V> createState() {
        return accessorSupplier.get();
    }

    @Override
    public void copy(E destEntity, E srcEntity) {
        PropertyState<E, V> dest = createState();
        dest.load(destEntity);
        PropertyState<E, V> src = createState();
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
    protected <PARAM> E modify(EntityType<E> entityType, E entity,
            WrapperVisitor<Void, PARAM, Void, RuntimeException> visitor,
            PARAM value) {
        if (entityType.isImmutable()) {
            Map<String, PropertyState<E, ?>> states = new HashMap<>();
            for (EntityPropertyType<E, ?, ?> p : entityType
                    .getEntityPropertyTypes()) {
                PropertyState<E, ?> state = p.createState();
                state.load(entity);
                if (p == this) {
                    state.getWrapper().accept(visitor, value, null);
                }
                states.put(p.getName(), state);
            }
            return entityType.newEntity(states);
        } else {
            PropertyState<E, ?> state = createState();
            state.load(entity);
            state.getWrapper().accept(visitor, value, null);
            state.save(entity);
            return entity;
        }
    }

    /**
     * @author nakamura-to
     */
    protected class ParentPropertyState implements PropertyState<E, V> {

        protected final PropertyState<PE, V> delegate;

        protected ParentPropertyState() {
            this.delegate = parentEntityPropertyType.createState();
        }

        @Override
        public Object get() {
            return delegate.get();
        }

        @Override
        public PropertyState<E, V> load(E entity) {
            delegate.load(entity);
            return this;
        }

        @Override
        public PropertyState<E, V> save(E entity) {
            delegate.save(entity);
            return this;
        }

        @Override
        public Wrapper<V> getWrapper() {
            return delegate.getWrapper();
        }
    }

    /**
     * @author nakamura-to
     */
    protected class BasicPropertyState<C> implements PropertyState<E, V> {

        protected final Holder<V, C> holder;

        protected BasicPropertyState(Supplier<Holder<V, C>> holderSupplier) {
            this.holder = holderSupplier.get();
        }

        @Override
        public Object get() {
            return holder.get();
        }

        @Override
        public PropertyState<E, V> load(E entity) {
            try {
                Object value = FieldUtil.get(field, entity);
                holder.set(holder.cast(value));
            } catch (WrapException wrapException) {
                throw new EntityPropertyAccessException(
                        wrapException.getCause(), entityClass.getName(), name);
            }
            return this;
        }

        @Override
        public PropertyState<E, V> save(E entity) {
            try {
                FieldUtil.set(field, entity, holder.get());
            } catch (WrapException wrapException) {
                throw new EntityPropertyAccessException(
                        wrapException.getCause(), entityClass.getName(), name);
            }
            return this;
        }

        @Override
        public Wrapper<V> getWrapper() {
            return holder.getWrapper();
        }
    }

}
