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

import org.seasar.doma.entity.Entity;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.DomaMessageCode;


/**
 * @author taedium
 * 
 */
public class EntityIterationHandler<R, I, E extends Entity<I>> implements
        ResultSetHandler<R> {

    protected final Class<E> entityClass;

    protected final IterationCallback<R, I> iterationCallback;

    public EntityIterationHandler(Class<E> entityClass,
            IterationCallback<R, I> iterationCallback) {
        assertNotNull(entityClass, iterationCallback);
        this.entityClass = entityClass;
        this.iterationCallback = iterationCallback;
    }

    @Override
    public R handle(ResultSet resultSet, Query query) throws SQLException {
        EntityFetcher fetcher = new EntityFetcher(query);
        IterationContext iterationContext = new IterationContext();
        R result = null;
        while (resultSet.next()) {
            E entity = null;
            try {
                entity = ClassUtil.newInstance(entityClass);
            } catch (WrapException e) {
                Throwable cause = e.getCause();
                throw new JdbcException(DomaMessageCode.DOMA2005, cause,
                        entityClass.getName(), cause);
            }
            fetcher.fetch(resultSet, entity);
            result = iterationCallback
                    .iterate(entity.__asInterface(), iterationContext);
            if (iterationContext.isExited()) {
                return result;
            }
        }
        return result;
    }

}
