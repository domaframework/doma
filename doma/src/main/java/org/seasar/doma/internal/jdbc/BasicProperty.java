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
package org.seasar.doma.internal.jdbc;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.NameConvention;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * @author taedium
 * 
 */
public class BasicProperty<D extends Domain<?, ?>> implements Property<D> {

    protected final String name;

    protected final String columnName;

    protected final D domain;

    protected final boolean insertable;

    protected final boolean updatable;

    public BasicProperty(String name, String columnName, D domain,
            boolean insertable, boolean updatable) {
        this.name = name;
        this.columnName = columnName;
        this.domain = domain;
        this.insertable = insertable;
        this.updatable = updatable;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getColumnName(Config config) {
        if (columnName != null) {
            return columnName;
        }
        Dialect dialect = config.dialect();
        NameConvention nameConvention = config.nameConvention();
        return nameConvention.fromPropertyToColumn(name, dialect);
    }

    @Override
    public D getDomain() {
        return domain;
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
    public String toString() {
        return domain != null ? domain.toString() : null;
    }

}
