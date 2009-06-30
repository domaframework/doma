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

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Dialect;
import org.seasar.doma.jdbc.NameConvention;

/**
 * @author taedium
 * 
 */
public abstract class DomaAbstractEntity<I> implements Entity<I> {

    private final String __catalogName;

    private final String __schemaName;

    private final String __tableName;

    public DomaAbstractEntity(String catalogName, String schemaName,
            String tableName) {
        __catalogName = catalogName;
        __schemaName = schemaName;
        __tableName = tableName;
    }

    public String __getQualifiedTableName(Config config) {
        StringBuilder buf = new StringBuilder();
        if (__catalogName != null) {
            buf.append(__catalogName).append(".");
        }
        if (__schemaName != null) {
            buf.append(__schemaName).append(".");
        }
        if (__tableName != null) {
            buf.append(__tableName);
        } else {
            Dialect dialect = config.dialect();
            NameConvention nameConvention = config.nameConvention();
            String tableName = nameConvention
                    .fromEntityToTable(__getName(), dialect);
            buf.append(tableName);
        }
        return buf.toString();
    }

}
