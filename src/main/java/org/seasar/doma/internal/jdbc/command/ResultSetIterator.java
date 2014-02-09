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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.seasar.doma.jdbc.command.ResultSetRowIndexConsumer;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * @author nakamura-to
 *
 */
public class ResultSetIterator<TARGET> implements Iterator<TARGET> {

    protected final ResultSet resultSet;

    protected final SelectQuery query;

    protected final ResultSetRowIndexConsumer consumer;

    protected final ResultProvider<TARGET> provider;

    protected boolean next;

    protected long index = -1;

    public ResultSetIterator(ResultSet resultSet, SelectQuery query,
            ResultSetRowIndexConsumer consumer, ResultProvider<TARGET> provider)
            throws SQLException {
        assertNotNull(resultSet, query, consumer, provider);
        this.resultSet = resultSet;
        this.query = query;
        this.consumer = consumer;
        this.provider = provider;
        this.next = resultSet.next();
        consumer.accept(index, next);
        index++;
    }

    @Override
    public boolean hasNext() {
        return next;
    }

    @Override
    public TARGET next() {
        TARGET result;
        try {
            result = provider.get(resultSet);
            next = resultSet.next();
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
        consumer.accept(index, next);
        index++;
        return result;
    }

    public List<TARGET> toList() {
        List<TARGET> result = new ArrayList<>();
        this.forEachRemaining(result::add);
        return result;
    }

    @SuppressWarnings("serial")
    protected static class SQLRuntimeException extends RuntimeException {

        protected SQLRuntimeException(SQLException cause) {
            super(cause);
        }

        @Override
        public SQLException getCause() {
            return (SQLException) super.getCause();
        }
    }

}
