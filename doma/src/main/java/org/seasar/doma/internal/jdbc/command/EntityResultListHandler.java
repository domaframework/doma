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
import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * @author taedium
 * 
 */
public class EntityResultListHandler<E> implements ResultSetHandler<List<E>> {

    protected final EntityType<E> entityType;

    public EntityResultListHandler(EntityType<E> entityType) {
        assertNotNull(entityType);
        this.entityType = entityType;
    }

    @Override
    public List<E> handle(ResultSet resultSet, Query query) throws SQLException {
        EntityFetcher<E> fetcher = new EntityFetcher<E>(query, entityType);
        List<E> entities = new ArrayList<E>();
        while (resultSet.next()) {
            E entity = entityType.newEntity();
            fetcher.fetch(resultSet, entity);
            entities.add(entity);
        }
        return entities;
    }

}
