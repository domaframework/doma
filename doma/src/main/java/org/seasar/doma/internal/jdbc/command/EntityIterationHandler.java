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

import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;

/**
 * @author taedium
 * 
 */
public class EntityIterationHandler<R, E> implements ResultSetHandler<R> {

    protected final EntityType<E> entityType;

    protected final IterationCallback<R, E> iterationCallback;

    public EntityIterationHandler(EntityType<E> entityType,
            IterationCallback<R, E> iterationCallback) {
        assertNotNull(entityType, iterationCallback);
        this.entityType = entityType;
        this.iterationCallback = iterationCallback;
    }

    @Override
    public R handle(ResultSet resultSet, Query query) throws SQLException {
        EntityFetcher<E> fetcher = new EntityFetcher<E>(query);
        IterationContext iterationContext = new IterationContext();
        R result = null;
        while (resultSet.next()) {
            E entity = fetcher.fetch(resultSet, entityType);
            result = iterationCallback.iterate(entity, iterationContext);
            if (iterationContext.isExited()) {
                return result;
            }
        }
        return result;
    }

}
