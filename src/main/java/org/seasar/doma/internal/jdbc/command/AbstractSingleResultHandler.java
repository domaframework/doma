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

import org.seasar.doma.jdbc.NonUniqueResultException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * 
 * @author nakamura-to
 * 
 * @param <RESULT>
 *            結果
 */
public abstract class AbstractSingleResultHandler<RESULT> implements
        ResultSetHandler<RESULT> {

    protected final ResultSetHandler<RESULT> handler;

    /**
     * @param handler
     */
    public AbstractSingleResultHandler(ResultSetHandler<RESULT> handler) {
        assertNotNull(handler);
        this.handler = handler;
    }

    @Override
    public RESULT handle(ResultSet resultSet, SelectQuery query)
            throws SQLException {
        RESULT result = handler.handle(resultSet, query);
        if (resultSet.next()) {
            Sql<?> sql = query.getSql();
            throw new NonUniqueResultException(query.getConfig()
                    .getExceptionSqlLogType(), sql);
        }
        return result;
    }
}
