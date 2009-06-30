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

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.Entity;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.util.Classes;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class EntityResultListHandler<I, E extends Entity<I>> implements
        ResultSetHandler<List<I>> {

    protected final Class<E> entityClass;

    public EntityResultListHandler(Class<E> entityClass) {
        assertNotNull(entityClass);
        this.entityClass = entityClass;
    }

    @Override
    public List<I> handle(ResultSet resultSet, Query query) throws SQLException {
        EntityFetcher fetcher = new EntityFetcher(query);
        List<I> entities = new ArrayList<I>();
        while (resultSet.next()) {
            E entity = null;
            try {
                entity = Classes.newInstance(entityClass);
            } catch (WrapException e) {
                Throwable cause = e.getCause();
                throw new JdbcException(MessageCode.DOMA2005, cause,
                        entityClass.getName(), cause);
            }
            fetcher.fetch(resultSet, entity);
            entities.add(entity.__asInterface());
        }
        return entities;
    }

}
