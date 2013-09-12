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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.query.SelectQuery;
import org.seasar.doma.jdbc.NoResultException;
import org.seasar.doma.jdbc.NonUniqueResultException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * @author taedium
 * 
 */
public class EntitySingleResultHandler<E> implements ResultSetHandler<E> {

    protected final EntityType<E> entityType;

    public EntitySingleResultHandler(EntityType<E> entityType) {
        assertNotNull(entityType);
        this.entityType = entityType;
    }

    @Override
    public E handle(ResultSet resultSet, SelectQuery query) throws SQLException {
        EntityBuilder<E> builder = new EntityBuilder<E>(query, entityType,
                query.isResultMappingEnsured());
        if (resultSet.next()) {
            E entity = builder.build(resultSet);
            if (resultSet.next()) {
                Sql<?> sql = query.getSql();
                throw new NonUniqueResultException(query.getConfig()
                        .getExceptionSqlLogType(), sql);
            }
            return entity;
        } else if (query.isResultEnsured()) {
            Sql<?> sql = query.getSql();
            throw new NoResultException(query.getConfig()
                    .getExceptionSqlLogType(), sql);
        }
        return null;
    }

}
