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
package org.seasar.doma.internal.jdbc.util;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.NamingConvention;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityMeta;

/**
 * @author taedium
 * 
 */
public final class TableUtil {

    public static String getQualifiedTableName(Config config, EntityMeta<?> entity) {
        assertNotNull(config, entity);
        String catalogName = entity.getCatalogName();
        String schemaName = entity.getSchemaName();
        String tableName = getTableName(config, entity);
        return getQualifiedTableName(catalogName, schemaName, tableName);
    }

    public static String getQualifiedTableName(String catalogName,
            String schemaName, String tableName) {
        assertNotNull(tableName);
        StringBuilder buf = new StringBuilder();
        if (catalogName != null && !catalogName.isEmpty()) {
            buf.append(catalogName).append(".");
        }
        if (schemaName != null && !schemaName.isEmpty()) {
            buf.append(schemaName).append(".");
        }
        return buf.append(tableName).toString();
    }

    protected static String getTableName(Config config, EntityMeta<?> entity) {
        if (entity.getTableName() != null) {
            return entity.getTableName();
        }
        Dialect dialect = config.dialect();
        NamingConvention namingConvention = config.namingConvention();
        return namingConvention.fromEntityToTable(entity.getName(), dialect);
    }
}
