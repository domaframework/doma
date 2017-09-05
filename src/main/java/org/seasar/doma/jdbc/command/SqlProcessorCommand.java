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
package org.seasar.doma.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.BiFunction;

import org.seasar.doma.SqlProcessor;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.query.SqlProcessorQuery;

/**
 * A command to handle an SQL statement.
 * 
 * @param <RESULT>
 *            the result type
 * @see SqlProcessor
 */
public class SqlProcessorCommand<RESULT> implements Command<RESULT> {

    protected final SqlProcessorQuery query;

    protected final BiFunction<Config, PreparedSql, RESULT> handler;

    public SqlProcessorCommand(SqlProcessorQuery query,
            BiFunction<Config, PreparedSql, RESULT> handler) {
        assertNotNull(query, handler);
        this.query = query;
        this.handler = handler;
    }

    @Override
    public RESULT execute() {
        return handler.apply(query.getConfig(), query.getSql());
    }
}
