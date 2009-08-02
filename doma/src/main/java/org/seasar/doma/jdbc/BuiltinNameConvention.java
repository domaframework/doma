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
package org.seasar.doma.jdbc;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.util.StringUtil;

/**
 * キャメルケースを大文字のアンダースコア区切りに、また逆に、アンダースコア区切りをキャメルケースに変換するネーミング規約です。
 * 
 * @author taedium
 * 
 */
public class BuiltinNameConvention implements NameConvention {

    /**
     * キャメルケースのエンティティ名を大文字のアンダースコア区切りテーブル名に変換します。
     * <p>
     * 変換には {@link StringUtil#decamelize(String)} を使用しています。
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String fromEntityToTable(String entityName, Dialect dialect)
            throws DomaNullPointerException {
        if (entityName == null) {
            throw new DomaNullPointerException("entityName");
        }
        if (dialect == null) {
            throw new DomaNullPointerException("dialect");
        }
        return StringUtil.decamelize(entityName);
    }

    /**
     * キャメルケースのプロパティ名を大文字のアンダースコア区切りカラム名に変換します。
     * <p>
     * 変換には {@link StringUtil#decamelize(String)} を使用しています。
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String fromPropertyToColumn(String propertyName, Dialect dialect)
            throws DomaNullPointerException {
        if (propertyName == null) {
            throw new DomaNullPointerException("propertyName");
        }
        if (dialect == null) {
            throw new DomaNullPointerException("dialect");
        }
        return StringUtil.decamelize(propertyName);
    }

    /**
     * アンダースコア区切りのテーブル名をキャメルケースのエンティティ名に変換します。
     * <p>
     * 変換には {@link StringUtil#capitalize(String)}を使用しています。
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String fromTableToEntity(String tableName, Dialect dialect)
            throws DomaNullPointerException {
        if (tableName == null) {
            throw new DomaNullPointerException("tableName");
        }
        if (dialect == null) {
            throw new DomaNullPointerException("dialect");
        }
        String name = StringUtil.camelize(tableName);
        return StringUtil.capitalize(name);
    }

    /**
     * アンダースコア区切りのカラム名をキャメルケースのプロパティ名に変換します。
     * <p>
     * 変換には {@link StringUtil#capitalize(String)}を使用しています。
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String fromColumnToProperty(String columnName, Dialect dialect)
            throws DomaNullPointerException {
        if (columnName == null) {
            throw new DomaNullPointerException("columnName");
        }
        if (dialect == null) {
            throw new DomaNullPointerException("dialect");
        }
        return StringUtil.camelize(columnName);
    }

}
