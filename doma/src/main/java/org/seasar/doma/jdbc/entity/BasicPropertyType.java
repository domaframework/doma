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

import org.seasar.doma.DomaNullPointerException;

/**
 * 基本のプロパティ型です。
 * 
 * @author taedium
 * 
 */
public abstract class BasicPropertyType<E, V> implements
        EntityPropertyType<E, V> {

    /** プロパティの名前 */
    protected final String name;

    /** カラム名 */
    protected final String columnName;

    /** 挿入可能かどうか */
    protected final boolean insertable;

    /** 更新可能かどうか */
    protected final boolean updatable;

    /**
     * インスタンスを構築します。
     * 
     * @param name
     *            プロパティの名前
     * @param columnName
     *            カラム名
     * @param insertable
     *            挿入可能かどうか
     * @param updatable
     *            更新可能かどうか
     */
    protected BasicPropertyType(String name, String columnName,
            boolean insertable, boolean updatable) {
        if (name == null) {
            throw new DomaNullPointerException("name");
        }
        if (columnName == null) {
            throw new DomaNullPointerException("columnName");
        }
        this.name = name;
        this.columnName = columnName;
        this.insertable = insertable;
        this.updatable = updatable;
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

}
