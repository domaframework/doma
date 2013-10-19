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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.FieldUtil;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.wrapper.Wrapper;
import org.seasar.doma.wrapper.WrapperVisitor;

/**
 * /** デフォルトのプロパティ型です。
 * 
 * @author nakamura-to
 * 
 * @param <PARENT>
 * @param <ENTITY>
 * @param <BASIC>
 * @param <DOMAIN>
 */
public class DefaultPropertyType<PARENT, ENTITY extends PARENT, BASIC, DOMAIN>
        implements EntityPropertyType<ENTITY, BASIC> {

    /** エンティティのクラス */
    protected final Class<ENTITY> entityClass;

    /** プロパティのクラス */
    protected final Class<?> entityPropertyClass;

    /** 基本型のクラス */
    protected final Class<BASIC> basicClass;

    /** ラッパーのサプライヤ */
    protected final Supplier<Wrapper<BASIC>> wrapperSupplier;

    /** 親のエンティティのプロパティ型 */
    protected final EntityPropertyType<PARENT, BASIC> parentEntityPropertyType;

    /** ドメインのメタタイプ */
    protected final DomainType<BASIC, DOMAIN> domainType;

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
    protected final Supplier<Property<ENTITY, BASIC>> propertySupplier;

    /**
     * インスタンスを構築します。
     * 
     * @param entityClass
     *            エンティティのクラス
     * @param entityPropertyClass
     *            プロパティのクラス
     * @param basicClass
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
    public DefaultPropertyType(Class<ENTITY> entityClass,
            Class<?> entityPropertyClass, Class<BASIC> basicClass,
            Supplier<Wrapper<BASIC>> wrapperSupplier,
            EntityPropertyType<PARENT, BASIC> parentEntityPropertyType,
            DomainType<BASIC, DOMAIN> domainType, String name,
            String columnName, boolean insertable, boolean updatable) {
        if (entityClass == null) {
            throw new DomaNullPointerException("entityClass");
        }
        if (entityPropertyClass == null) {
            throw new DomaNullPointerException("entityPropertyClass");
        }
        if (basicClass == null) {
            throw new DomaNullPointerException("basicClass");
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
        this.entityPropertyClass = entityPropertyClass;
        this.basicClass = basicClass;
        this.wrapperSupplier = wrapperSupplier;
        this.parentEntityPropertyType = parentEntityPropertyType;
        this.domainType = domainType;
        this.name = name;
        this.columnName = columnName;
        this.insertable = insertable;
        this.updatable = updatable;
        this.field = parentEntityPropertyType == null ? getField() : null;
        this.propertySupplier = createPropertySupplier();
    }

    private Supplier<Property<ENTITY, BASIC>> createPropertySupplier() {
        if (parentEntityPropertyType != null) {
            return () -> new ParentProperty();
        }
        if (domainType != null) {
            if (entityPropertyClass == Optional.class) {
                return () -> new DefaultProperty<Optional<DOMAIN>>(
                        domainType.createOptionalScalar());
            } else {
                return () -> new DefaultProperty<DOMAIN>(
                        domainType.createScalar());
            }
        }
        if (entityPropertyClass == Optional.class) {
            return () -> new DefaultProperty<Optional<BASIC>>(
                    new org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar<>(
                            wrapperSupplier));
        } else {
            return () -> new DefaultProperty<BASIC>(
                    new org.seasar.doma.internal.jdbc.scalar.BasicScalar<>(
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
    public Property<ENTITY, BASIC> createProperty() {
        return propertySupplier.get();
    }

    @Override
    public void copy(ENTITY destEntity, ENTITY srcEntity) {
        Property<ENTITY, BASIC> dest = createProperty();
        dest.load(destEntity);
        Property<ENTITY, BASIC> src = createProperty();
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
    protected <PARAM> ENTITY modify(EntityType<ENTITY> entityType,
            ENTITY entity,
            WrapperVisitor<Void, PARAM, Void, RuntimeException> visitor,
            PARAM value) {
        if (entityType.isImmutable()) {
            List<EntityPropertyType<ENTITY, ?>> propertyTypes = entityType
                    .getEntityPropertyTypes();
            Map<String, Property<ENTITY, ?>> args = new HashMap<>(
                    propertyTypes.size());
            for (EntityPropertyType<ENTITY, ?> propertyType : propertyTypes) {
                Property<ENTITY, ?> property = propertyType.createProperty();
                property.load(entity);
                if (propertyType == this) {
                    property.getWrapper().accept(visitor, value, null);
                }
                args.put(propertyType.getName(), property);
            }
            return entityType.newEntity(args);
        } else {
            Property<ENTITY, ?> property = createProperty();
            property.load(entity);
            property.getWrapper().accept(visitor, value, null);
            property.save(entity);
            return entity;
        }
    }

    /**
     * @author nakamura-to
     */
    protected class ParentProperty implements Property<ENTITY, BASIC> {

        protected final Property<PARENT, BASIC> delegate;

        protected ParentProperty() {
            this.delegate = parentEntityPropertyType.createProperty();
        }

        @Override
        public Object get() {
            return delegate.get();
        }

        @Override
        public Property<ENTITY, BASIC> load(ENTITY entity) {
            delegate.load(entity);
            return this;
        }

        @Override
        public Property<ENTITY, BASIC> save(ENTITY entity) {
            delegate.save(entity);
            return this;
        }

        @Override
        public Wrapper<BASIC> getWrapper() {
            return delegate.getWrapper();
        }

        @Override
        public Optional<Class<?>> getDomainClass() {
            return delegate.getDomainClass();
        }

    }

    /**
     * @author nakamura-to
     */
    protected class DefaultProperty<CONTAINER> implements
            Property<ENTITY, BASIC> {

        protected final Scalar<BASIC, CONTAINER> scalar;

        protected DefaultProperty(Scalar<BASIC, CONTAINER> scalar) {
            this.scalar = scalar;
        }

        @Override
        public Object get() {
            return scalar.get();
        }

        @Override
        public Property<ENTITY, BASIC> load(ENTITY entity) {
            try {
                Object value = FieldUtil.get(field, entity);
                scalar.set(scalar.cast(value));
            } catch (WrapException wrapException) {
                throw new EntityPropertyAccessException(
                        wrapException.getCause(), entityClass.getName(), name);
            }
            return this;
        }

        @Override
        public Property<ENTITY, BASIC> save(ENTITY entity) {
            try {
                FieldUtil.set(field, entity, scalar.get());
            } catch (WrapException wrapException) {
                throw new EntityPropertyAccessException(
                        wrapException.getCause(), entityClass.getName(), name);
            }
            return this;
        }

        @Override
        public Wrapper<BASIC> getWrapper() {
            return scalar.getWrapper();
        }

        @Override
        public Optional<Class<?>> getDomainClass() {
            return scalar.getDomainClass();
        }

    }

}
