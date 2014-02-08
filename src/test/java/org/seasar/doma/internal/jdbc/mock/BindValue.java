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
package org.seasar.doma.internal.jdbc.mock;

/**
 * 
 * @author taedium
 * 
 */
public class BindValue {

    protected final String typeName;

    protected final int index;

    protected final Object value;

    protected final Integer sqlType;

    public BindValue(String typeName, int index, Object value) {
        this.typeName = typeName;
        this.index = index;
        this.value = value;
        this.sqlType = null;
    }

    public BindValue(int index, int sqlType) {
        this.typeName = null;
        this.index = index;
        this.value = null;
        this.sqlType = sqlType;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getIndex() {
        return index;
    }

    public Object getValue() {
        return value;
    }

    public Integer getSqlType() {
        return sqlType;
    }
}
