package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.seasar.doma.jdbc.ObjectProvider;
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

    protected final ObjectProvider<TARGET> provider;

    protected boolean next;

    protected long index = -1;

    public ResultSetIterator(ResultSet resultSet, SelectQuery query,
            ResultSetRowIndexConsumer consumer, ObjectProvider<TARGET> provider)
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
