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
package org.seasar.doma.entity;

import java.io.Serializable;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.NameConvention;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * @author taedium
 * 
 */
public abstract class DomaAbstractEntity<I> implements Entity<I>, Serializable {

    private static final long serialVersionUID = 1L;

    private final String __catalogName;

    private final String __schemaName;

    private final String __tableName;

    public DomaAbstractEntity(String __catalogName, String __schemaName,
            String __tableName) {
        this.__catalogName = __catalogName;
        this.__schemaName = __schemaName;
        this.__tableName = __tableName;
    }

    public String __getQualifiedTableName(Config __config) {
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
            Dialect dialect = __config.dialect();
            NameConvention nameConvention = __config.nameConvention();
            String tableName = nameConvention
                    .fromEntityToTable(__getName(), dialect);
            buf.append(tableName);
        }
        return buf.toString();
    }

}
