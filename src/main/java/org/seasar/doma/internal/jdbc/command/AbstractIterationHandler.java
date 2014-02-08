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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;
import org.seasar.doma.jdbc.NoResultException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * 
 * @author nakamura-to
 * 
 * @param <RESULT>
 *            結果
 * @param <TARGET>
 *            反復処理対象
 */
public abstract class AbstractIterationHandler<TARGET, RESULT> implements
        ResultSetHandler<RESULT> {

    protected final IterationCallback<TARGET, RESULT> iterationCallback;

    public AbstractIterationHandler(
            IterationCallback<TARGET, RESULT> iterationCallback) {
        assertNotNull(iterationCallback);
        this.iterationCallback = iterationCallback;
    }

    @Override
    public RESULT handle(ResultSet resultSet, SelectQuery query)
            throws SQLException {
        ResultProvider<TARGET> provider = createResultProvider(query);
        IterationContext context = new IterationContext();
        RESULT result = iterationCallback.defaultResult();
        boolean existent = false;
        while (resultSet.next()) {
            existent = true;
            TARGET target = provider.get(resultSet);
            result = iterationCallback.iterate(target, context);
            if (context.isExited()) {
                break;
            }
        }
        if (query.isResultEnsured() && !existent) {
            Sql<?> sql = query.getSql();
            throw new NoResultException(query.getConfig()
                    .getExceptionSqlLogType(), sql);
        }
        return iterationCallback.postIterate(result, context);
    }

    protected abstract ResultProvider<TARGET> createResultProvider(
            SelectQuery query);
}
