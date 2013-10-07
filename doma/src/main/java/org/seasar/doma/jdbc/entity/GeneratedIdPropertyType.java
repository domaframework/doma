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

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.GenerationType;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.jdbc.id.IdGenerationConfig;
import org.seasar.doma.jdbc.id.IdGenerator;
import org.seasar.doma.message.Message;
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

/**
 * 生成される識別子のプロパティ型です。
 * 
 * @author taedium
 * 
 */
public class GeneratedIdPropertyType<PE, E extends PE, P, V extends Number, D>
        extends BasicPropertyType<PE, E, P, V, D> {

    /** 識別子のジェネレータ */
    protected final IdGenerator idGenerator;

    /**
     * インスタンスを構築します。
     * 
     * @param entityClass
     *            エンティティのクラス
     * @param entityPropertyClass
     *            プロパティのクラス
     * @param valueClass
     *            値のクラス
     * @param wrapperSupplier
     *            ラッパーのサプライヤ
     * @param parentEntityPropertyType
     *            親のエンティティのプロパティ型、親のエンティティを持たない場合 {@code null}
     * @param domainType
     *            ドメインのメタタイプ、ドメインでない場合 {@code null}
     * @param name
     *            プロパティの名前
     * @param columnName
     *            カラム名
     * @param idGenerator
     *            識別子のジェネレータ
     */
    public GeneratedIdPropertyType(Class<E> entityClass,
            Class<?> entityPropertyClass, Class<V> valueClass,
            Supplier<Wrapper<V>> wrapperSupplier,
            EntityPropertyType<PE, P, V> parentEntityPropertyType,
            DomainType<V, D> domainType, String name, String columnName,
            IdGenerator idGenerator) {
        super(entityClass, entityPropertyClass, valueClass, wrapperSupplier,
                parentEntityPropertyType, domainType, name, columnName, true,
                true);
        if (idGenerator == null) {
            throw new DomaNullPointerException("idGenerator");
        }
        this.idGenerator = idGenerator;
    }

    @Override
    public boolean isId() {
        return true;
    }

    /**
     * 識別子の生成方法を検証します。
     * 
     * @param config
     *            識別子の生成に関する設定
     */
    public void validateGenerationStrategy(IdGenerationConfig config) {
        Dialect dialect = config.getDialect();
        GenerationType generationType = idGenerator.getGenerationType();
        if (!isGenerationTypeSupported(generationType, dialect)) {
            EntityType<?> entityType = config.getEntityType();
            throw new JdbcException(Message.DOMA2021, entityType.getName(),
                    name, generationType.name(), dialect.getName());
        }
    }

    /**
     * 識別子を生成する方法がサポートされているかどうかを返します。
     * 
     * @param generationType
     *            識別子の生成方法
     * @param dialect
     *            方言
     * @return サポートされている場合 {@code true}
     */
    protected boolean isGenerationTypeSupported(GenerationType generationType,
            Dialect dialect) {
        switch (generationType) {
        case IDENTITY:
            return dialect.supportsIdentity();
        case SEQUENCE:
            return dialect.supportsSequence();
        default:
            return true;
        }
    }

    /**
     * 識別子がINSERT文に含まれるかどうかを返します。
     * 
     * @param config
     *            識別子の生成に関する設定
     * @return 含まれる場合 {@code true}
     */
    public boolean isIncluded(IdGenerationConfig config) {
        return idGenerator.includesIdentityColumn(config);
    }

    /**
     * バッチ挿入での識別子生成がサポートされているかどうかを返します。
     * 
     * @param config
     *            識別子の生成に関する設定
     * @return サポートされている場合 {@code true}
     */
    public boolean isBatchSupported(IdGenerationConfig config) {
        return idGenerator.supportsBatch(config);
    }

    /**
     * バ{@link Statement#getGeneratedKeys()} をサポートしているかどうかを返します。
     * 
     * @param config
     *            識別子の生成に関する設定
     * @return サポートされている場合 {@code true}
     */
    public boolean isAutoGeneratedKeysSupported(IdGenerationConfig config) {
        return idGenerator.supportsAutoGeneratedKeys(config);
    }

    /**
     * INSERTの実行前に識別子を生成します。
     * 
     * @param entityType
     *            エンティティタイプ
     * @param entity
     *            エンティティ
     * @param config
     *            識別子の生成に関する設定
     * @return エンティティ
     */
    public E preInsert(EntityType<E> entityType, E entity,
            IdGenerationConfig config) {
        return setValue(entityType, entity,
                () -> idGenerator.generatePreInsert(config));
    }

    /**
     * INSERTの実行後に識別子の生成を行います。
     * 
     * @param entityType
     *            エンティティタイプ
     * @param entity
     *            エンティティ
     * @param config
     *            識別子の生成に関する設定
     * @param statement
     *            INSERT文を実行した文
     * @return エンティティ
     */
    public E postInsert(EntityType<E> entityType, E entity,
            IdGenerationConfig config, Statement statement) {
        return setValue(entityType, entity,
                () -> idGenerator.generatePostInsert(config, statement));
    }

    /**
     * エンティティに値を設定して返します。
     * 
     * @param entityType
     *            エンティティタイプ
     * @param entity
     *            エンティティ
     * @param supplier
     *            値のサプライヤ
     * @return エンティティ
     */
    protected E setValue(EntityType<E> entityType, E entity,
            Supplier<Long> supplier) {
        Long value = supplier.get();
        if (value == null) {
            return entity;
        }
        ValueSetter valueSetter = new ValueSetter();
        if (entityType.isImmutable()) {
            Map<String, PropertyState<E, ?>> accessors = new HashMap<>();
            for (EntityPropertyType<E, ?, ?> p : entityType
                    .getEntityPropertyTypes()) {
                PropertyState<E, ?> accessor = p.createState();
                accessor.load(entity);
                if (p == this) {
                    accessor.getWrapper().accept(valueSetter, value);
                }
                accessors.put(p.getName(), accessor);
            }
            return entityType.newEntity(accessors);
        } else {
            PropertyState<E, ?> accessor = createState();
            accessor.load(entity);
            accessor.getWrapper().accept(valueSetter, value);
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
            setValue(wrapper, p);
            return null;
        }

        @Override
        public Void visitBigDecimalWrapper(BigDecimalWrapper wrapper, Number p)
                throws RuntimeException {
            setValue(wrapper, p);
            return null;
        }

        @Override
        public Void visitByteWrapper(ByteWrapper wrapper, Number p)
                throws RuntimeException {
            setValue(wrapper, p);
            return null;
        }

        @Override
        public Void visitDoubleWrapper(DoubleWrapper wrapper, Number p)
                throws RuntimeException {
            setValue(wrapper, p);
            return null;
        }

        @Override
        public Void visitFloatWrapper(FloatWrapper wrapper, Number p)
                throws RuntimeException {
            setValue(wrapper, p);
            return null;
        }

        @Override
        public Void visitIntegerWrapper(IntegerWrapper wrapper, Number p)
                throws RuntimeException {
            setValue(wrapper, p);
            return null;
        }

        @Override
        public Void visitLongWrapper(LongWrapper wrapper, Number p)
                throws RuntimeException {
            setValue(wrapper, p);
            return null;
        }

        @Override
        public Void visitShortWrapper(ShortWrapper wrapper, Number p)
                throws RuntimeException {
            setValue(wrapper, p);
            return null;
        }

        protected void setValue(NumberWrapper<? extends Number> wrapper,
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

}
