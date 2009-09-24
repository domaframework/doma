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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.entity.EntityTypeFactory;
import org.seasar.doma.internal.jdbc.query.Query;

/**
 * @author taedium
 * 
 */
public class EntityResultListHandler<E> implements ResultSetHandler<List<E>> {

    protected final EntityTypeFactory<E> entityTypeFactory;

    public EntityResultListHandler(EntityTypeFactory<E> entityTypeFactory) {
        assertNotNull(entityTypeFactory);
        this.entityTypeFactory = entityTypeFactory;
    }

    @Override
    public List<E> handle(ResultSet resultSet, Query query) throws SQLException {
        EntityFetcher fetcher = new EntityFetcher(query);
        List<E> entities = new ArrayList<E>();
        while (resultSet.next()) {
            EntityType<E> entityType = entityTypeFactory.createEntityType();
            fetcher.fetch(resultSet, entityType);
            entities.add(entityType.getEntity());
        }
        return entities;
    }

}
