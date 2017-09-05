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

import java.util.function.BiFunction;
import java.util.function.Function;

import org.seasar.doma.internal.jdbc.util.DatabaseObjectUtil;

public abstract class AbstractEntityDesc<ENTITY> implements EntityDesc<ENTITY> {

    protected AbstractEntityDesc() {
    }

    @Override
    public String getQualifiedTableName(BiFunction<NamingType, String, String> namingFunction,
            Function<String, String> quoteFunction) {
        String catalogName = getCatalogName();
        String schemaName = getSchemaName();
        String tableName = getTableName(namingFunction);
        Function<String, String> mapper = isQuoteRequired() ? quoteFunction : Function.identity();
        return DatabaseObjectUtil.getQualifiedName(mapper, catalogName, schemaName, tableName);
    }
}
