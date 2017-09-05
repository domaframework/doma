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
package org.seasar.doma.jdbc;

import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.query.Query;

/**
 * A handler for the column that is unknown to an entity.
 */
public interface UnknownColumnHandler {

    /**
     * Handles the unknown column.
     * 
     * @param query
     *            the query
     * @param entityDesc
     *            the entity description
     * @param unknownColumnName
     *            the name of the unknown column
     * @throws UnknownColumnException
     *             if this handler does not allow the unknown column
     */
    default void handle(Query query, EntityDesc<?> entityDesc, String unknownColumnName) {
        Sql<?> sql = query.getSql();
        Naming naming = query.getConfig().getNaming();
        NamingType namingType = entityDesc.getNamingType();
        throw new UnknownColumnException(query.getConfig().getExceptionSqlLogType(),
                unknownColumnName, naming.revert(namingType, unknownColumnName),
                entityDesc.getEntityClass().getName(), sql.getKind(), sql.getRawSql(),
                sql.getFormattedSql(), sql.getSqlFilePath());
    }
}
