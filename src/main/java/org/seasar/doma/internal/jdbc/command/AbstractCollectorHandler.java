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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.ResultSetRowIndexConsumer;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * @author nakamura-to
 * 
 * @param <TARGET>
 *            処理対象
 * @param <RESULT>
 *            結果
 */
public abstract class AbstractCollectorHandler<TARGET, RESULT> implements ResultSetHandler<RESULT> {

    protected final ResultSetHandler<RESULT> handler;

    public AbstractCollectorHandler(ResultSetHandler<RESULT> handler) {
        AssertionUtil.assertNotNull(handler);
        this.handler = handler;
    }

    @Override
    public Supplier<RESULT> handle(ResultSet resultSet, SelectQuery query,
            ResultSetRowIndexConsumer consumer) throws SQLException {
        return handler.handle(resultSet, query, consumer);
    }

}
