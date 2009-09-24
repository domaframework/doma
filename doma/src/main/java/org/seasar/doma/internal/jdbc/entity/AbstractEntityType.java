/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.doma.internal.jdbc.entity;

/**
 * {@link EntityType} の骨格実装です。
 * 
 * @author taedium
 * 
 */
public abstract class AbstractEntityType<E> implements EntityType<E> {

    private static final long serialVersionUID = 1L;

    private final String __catalogName;

    private final String __schemaName;

    private final String __tableName;

    /**
     * インスタンスを構築します。
     * 
     * @param catalogName
     *            カタログ名
     * @param schemaName
     *            スキーマ名
     * @param tableName
     *            テーブル名
     */
    public AbstractEntityType(String catalogName, String schemaName,
            String tableName) {
        this.__catalogName = catalogName;
        this.__schemaName = schemaName;
        this.__tableName = tableName;
    }

    @Override
    public String getCatalogName() {
        return __catalogName;
    }

    @Override
    public String getSchemaName() {
        return __schemaName;
    }

    @Override
    public String getTableName() {
        return __tableName;
    }

    protected char toPrimitive(Character value) {
        return value != null ? value.charValue() : 0;
    }

    protected boolean toPrimitive(Boolean value) {
        return value != null ? value.booleanValue() : false;
    }

    protected byte toPrimitive(Byte value) {
        return value != null ? value.byteValue() : 0;
    }

    protected short toPrimitive(Short value) {
        return value != null ? value.shortValue() : 0;
    }

    protected int toPrimitive(Integer value) {
        return value != null ? value.intValue() : 0;
    }

    protected long toPrimitive(Long value) {
        return value != null ? value.longValue() : 0L;
    }

    protected float toPrimitive(Float value) {
        return value != null ? value.floatValue() : 0f;
    }

    protected double toPrimitive(Double value) {
        return value != null ? value.doubleValue() : 0d;
    }
}
