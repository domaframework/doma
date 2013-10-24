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
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.seasar.doma.jdbc.NoResultException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * @author nakamura-to
 * 
 */
public abstract class AbstractStreamHandler<RESULT, TARGET> implements
        ResultSetHandler<RESULT> {

    protected final Function<Stream<TARGET>, RESULT> mapper;

    public AbstractStreamHandler(Function<Stream<TARGET>, RESULT> mapper) {
        assertNotNull(mapper);
        this.mapper = mapper;
    }

    @Override
    public RESULT handle(ResultSet resultSet, SelectQuery query)
            throws SQLException {
        ResultProvider<TARGET> provider = createResultProvider(query);
        Spliterator<TARGET> spliterator = Spliterators.spliteratorUnknownSize(
                new ResultIterator<>(resultSet, query, provider), 0);
        Stream<TARGET> stream = StreamSupport.stream(spliterator, false);
        try {
            return mapper.apply(stream);
        } catch (SQLRuntimeException e) {
            throw e.getCause();
        }
    }

    protected abstract ResultProvider<TARGET> createResultProvider(
            SelectQuery query);

    protected static class ResultIterator<TARGET> implements Iterator<TARGET> {

        protected final ResultSet resultSet;

        protected final SelectQuery query;

        protected final ResultProvider<TARGET> provider;

        protected boolean invoked = false;

        public ResultIterator(ResultSet resultSet, SelectQuery query,
                ResultProvider<TARGET> provider) {
            assertNotNull(resultSet, query, provider);
            this.resultSet = resultSet;
            this.query = query;
            this.provider = provider;
        }

        @Override
        public boolean hasNext() {
            boolean existent;
            try {
                existent = resultSet.next();
            } catch (SQLException e) {
                throw new SQLRuntimeException(e);
            }
            if (!invoked) {
                if (query.isResultEnsured() && !existent) {
                    Sql<?> sql = query.getSql();
                    throw new NoResultException(query.getConfig()
                            .getExceptionSqlLogType(), sql);
                }
                invoked = true;
            }
            return existent;
        }

        @Override
        public TARGET next() {
            try {
                return provider.get(resultSet);
            } catch (SQLException e) {
                throw new SQLRuntimeException(e);
            }
        }
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
