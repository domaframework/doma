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
import org.seasar.doma.internal.util.StringUtil;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * キャメルケースを大文字のアンダースコア区切りに変換するネーミング規約です。
 * 
 * @author taedium
 * 
 */
public class CamelNamingConvention implements NamingConvention {

    /**
     * キャメルケースのエンティティ名を大文字のアンダースコア区切りのテーブル名に変換します。
     * <p>
     * たとえば、{@code EmployeeInfo} というエンティティ名を {@code EMPLOYEE_INFO}
     * というテーブル名に変換します。
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String fromEntityToTable(String entityName, Dialect dialect) {
        if (entityName == null) {
            throw new DomaNullPointerException("entityName");
        }
        if (dialect == null) {
            throw new DomaNullPointerException("dialect");
        }
        return StringUtil.decamelize(entityName);
    }

    /**
     * キャメルケースのプロパティ名を大文字のアンダースコア区切りのカラム名に変換します。
     * <p>
     * たとえば、{@code employeeName} というプロパティ名を {@code EMPLOYEE_NAME} というカラム名に変換します。
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String fromPropertyToColumn(String propertyName, Dialect dialect) {
        if (propertyName == null) {
            throw new DomaNullPointerException("propertyName");
        }
        if (dialect == null) {
            throw new DomaNullPointerException("dialect");
        }
        return StringUtil.decamelize(propertyName);
    }

}
