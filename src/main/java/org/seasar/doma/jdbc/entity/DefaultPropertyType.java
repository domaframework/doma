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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.entity.PropertyField;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar;
import org.seasar.doma.internal.jdbc.scalar.OptionalDoubleScalar;
import org.seasar.doma.internal.jdbc.scalar.OptionalIntScalar;
import org.seasar.doma.internal.jdbc.scalar.OptionalLongScalar;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.holder.HolderType;
import org.seasar.doma.wrapper.Wrapper;
import org.seasar.doma.wrapper.WrapperVisitor;

/**
 * /** デフォルトのプロパティ型です。
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
public class DefaultPropertyType<ENTITY, BASIC, HOLDER>
        implements EntityPropertyType<ENTITY, BASIC> {

    /** エンティティのクラス */
    protected final Class<ENTITY> entityClass;

    /** プロパティのクラス */
    protected final Class<?> entityPropertyClass;

    /** ラッパーのサプライヤ */
    protected final Supplier<Wrapper<BASIC>> wrapperSupplier;

    /** ドメインのメタタイプ */
    protected final HolderType<BASIC, HOLDER> holderType;

    /** プロパティの名前 */
    protected final String name;

    /** プロパティの単純名 */
    protected final String simpleName;

    /** カラム名 */
    protected final String columnName;

    /** ネーミング規約 */
    protected final NamingType namingType;

    /** 挿入可能かどうか */
    protected final boolean insertable;

    /** 更新可能かどうか */
    protected final boolean updatable;

    /** 引用符が必要とされるかどうか */
    protected final boolean quoteRequired;

    /** プロパティのフィールド */
    protected final PropertyField<ENTITY> field;

    /** アクセサのサプライヤ */
    protected final Supplier<Property<ENTITY, BASIC>> propertySupplier;

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
     * @param insertable
     *            挿入可能かどうか
     * @param updatable
     *            更新可能かどうか
     * @param quoteRequired
     *            カラム名に引用符が必要とされるかどうか
     */
    public DefaultPropertyType(Class<ENTITY> entityClass,
            Class<?> entityPropertyClass,
            Supplier<Wrapper<BASIC>> wrapperSupplier,
            HolderType<BASIC, HOLDER> holderType, String name,
            String columnName, NamingType namingType, boolean insertable,
            boolean updatable, boolean quoteRequired) {
        if (entityClass == null) {
            throw new DomaNullPointerException("entityClass");
        }
        if (entityPropertyClass == null) {
            throw new DomaNullPointerException("entityPropertyClass");
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
        this.wrapperSupplier = wrapperSupplier;
        this.holderType = holderType;
        this.name = name;
        int pos = name.lastIndexOf('.');
        this.simpleName = pos > -1 ? name.substring(pos + 1) : name;
        this.columnName = columnName;
        this.namingType = namingType;
        this.insertable = insertable;
        this.updatable = updatable;
        this.quoteRequired = quoteRequired;
        this.field = new PropertyField<>(name, entityClass);
        this.propertySupplier = createPropertySupplier();
    }

    @SuppressWarnings("unchecked")
    private Supplier<Property<ENTITY, BASIC>> createPropertySupplier() {
        if (holderType != null) {
            if (entityPropertyClass == Optional.class) {
                return () -> new DefaultProperty<Optional<HOLDER>>(
                        holderType.createOptionalScalar());
            } else {
                return () -> new DefaultProperty<HOLDER>(
                        holderType.createScalar());
            }
        }
        if (entityPropertyClass == Optional.class) {
            return () -> new DefaultProperty<Optional<BASIC>>(
                    new OptionalBasicScalar<>(wrapperSupplier));
        } else if (entityPropertyClass == OptionalInt.class) {
            return () -> new DefaultProperty<OptionalInt>(
                    (Scalar<BASIC, OptionalInt>) new OptionalIntScalar());
        } else if (entityPropertyClass == OptionalLong.class) {
            return () -> new DefaultProperty<OptionalLong>(
                    (Scalar<BASIC, OptionalLong>) new OptionalLongScalar());
        } else if (entityPropertyClass == OptionalDouble.class) {
            return () -> new DefaultProperty<OptionalDouble>(
                    (Scalar<BASIC, OptionalDouble>) new OptionalDoubleScalar());
        } else {
            return () -> new DefaultProperty<BASIC>(new BasicScalar<>(
                    wrapperSupplier, field.isPrimitive()));
        }
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
    public String getColumnName(
            BiFunction<NamingType, String, String> namingFunction) {
        return getColumnName(namingFunction, Function.identity());
    }

    public String getColumnName(
            BiFunction<NamingType, String, String> namingFunction,
            Function<String, String> quoteFunction) {
        String columnName = this.columnName;
        if (columnName.isEmpty()) {
            columnName = namingFunction.apply(namingType, simpleName);
        }
        return quoteRequired ? quoteFunction.apply(columnName) : columnName;
    }

    @Override
    public boolean isQuoteRequired() {
        return quoteRequired;
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
     * 必要ならばエンティティに値を設定して返します。
     * 
     * @param <VALUE>
     *            値の型
     * @param entityType
     *            エンティティタイプ
     * @param entity
     *            エンティティ
     * @param visitor
     *            ビジター
     * @param value
     *            値
     * @return 値が変更されたエンティティもしくは変更されていないエンティティ
     */
    protected <VALUE> ENTITY modifyIfNecessary(EntityType<ENTITY> entityType,
            ENTITY entity,
            WrapperVisitor<Boolean, VALUE, Void, RuntimeException> visitor,
            VALUE value) {
        if (entityType.isImmutable()) {
            List<EntityPropertyType<ENTITY, ?>> propertyTypes = entityType
                    .getEntityPropertyTypes();
            Map<String, Property<ENTITY, ?>> args = new HashMap<>(
                    propertyTypes.size());
            for (EntityPropertyType<ENTITY, ?> propertyType : propertyTypes) {
                Property<ENTITY, ?> property = propertyType.createProperty();
                property.load(entity);
                if (propertyType == this) {
                    Boolean modified = property.getWrapper().accept(visitor,
                            value, null);
                    if (modified == Boolean.FALSE) {
                        return entity;
                    }
                }
                args.put(propertyType.getName(), property);
            }
            return entityType.newEntity(args);
        } else {
            Property<ENTITY, ?> property = createProperty();
            property.load(entity);
            Boolean modified = property.getWrapper().accept(visitor, value,
                    null);
            if (modified == Boolean.FALSE) {
                return entity;
            }
            property.save(entity);
            return entity;
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
            Object value = field.getValue(entity);
            scalar.set(scalar.cast(value));
            return this;
        }

        @Override
        public Property<ENTITY, BASIC> save(ENTITY entity) {
            field.setValue(entity, scalar.get());
            return this;
        }

        @Override
        public InParameter<BASIC> asInParameter() {
            return new ScalarInParameter<>(scalar);
        }

        @Override
        public Wrapper<BASIC> getWrapper() {
            return scalar.getWrapper();
        }

        @Override
        public Optional<Class<?>> getHolderClass() {
            return scalar.getHolderClass();
        }

    }

}
