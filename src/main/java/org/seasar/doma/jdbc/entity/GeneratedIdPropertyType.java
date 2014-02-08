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
import java.util.function.Supplier;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.GenerationType;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.jdbc.id.IdGenerationConfig;
import org.seasar.doma.jdbc.id.IdGenerator;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.NumberWrapper;
import org.seasar.doma.wrapper.NumberWrapperVisitor;
import org.seasar.doma.wrapper.Wrapper;

/**
 * 生成される識別子のプロパティ型です。
 * 
 * @author nakamura-to
 * 
 * @param <PARENT>
 *            親エンティティの型
 * @param <ENTITY>
 *            エンティティの型
 * @param <BASIC>
 *            プロパティの基本型
 * @param <DOMAIN>
 *            プロパティのドメイン型
 */
public class GeneratedIdPropertyType<PARENT, ENTITY extends PARENT, BASIC extends Number, DOMAIN>
        extends DefaultPropertyType<PARENT, ENTITY, BASIC, DOMAIN> {

    /** 識別子のジェネレータ */
    protected final IdGenerator idGenerator;

    /**
     * インスタンスを構築します。
     * 
     * @param entityClass
     *            エンティティのクラス
     * @param entityPropertyClass
     *            プロパティのクラス
     * @param basicClass
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
     * @param quoteRequired
     *            カラム名に引用符が必要とされるかどうか
     */
    public GeneratedIdPropertyType(Class<ENTITY> entityClass,
            Class<?> entityPropertyClass, Class<BASIC> basicClass,
            Supplier<Wrapper<BASIC>> wrapperSupplier,
            EntityPropertyType<PARENT, BASIC> parentEntityPropertyType,
            DomainType<BASIC, DOMAIN> domainType, String name,
            String columnName, boolean quoteRequired, IdGenerator idGenerator) {
        super(entityClass, entityPropertyClass, basicClass, wrapperSupplier,
                parentEntityPropertyType, domainType, name, columnName, true,
                true, quoteRequired);
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
    public ENTITY preInsert(EntityType<ENTITY> entityType, ENTITY entity,
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
    public ENTITY postInsert(EntityType<ENTITY> entityType, ENTITY entity,
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
    protected ENTITY setValue(EntityType<ENTITY> entityType, ENTITY entity,
            Supplier<Long> supplier) {
        Long value = supplier.get();
        if (value == null) {
            return entity;
        }
        return modify(entityType, entity, new ValueSetter(), value);
    }

    protected static class ValueSetter implements
            NumberWrapperVisitor<Void, Number, Void, RuntimeException> {

        @Override
        public <V extends Number> Void visitNumberWrapper(
                NumberWrapper<V> wrapper, Number value, Void q)
                throws RuntimeException {
            Number currentValue = wrapper.get();
            if (currentValue == null || currentValue.intValue() < 0) {
                wrapper.set(value);
            }
            return null;
        }
    }

}
