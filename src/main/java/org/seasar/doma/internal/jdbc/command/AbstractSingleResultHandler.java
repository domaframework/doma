package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

import org.seasar.doma.jdbc.NonUniqueResultException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.ResultSetRowIndexConsumer;
import org.seasar.doma.jdbc.query.SelectQuery;

public abstract class AbstractSingleResultHandler<RESULT> implements ResultSetHandler<RESULT> {

    protected final ResultSetHandler<RESULT> handler;

    public AbstractSingleResultHandler(ResultSetHandler<RESULT> handler) {
        assertNotNull(handler);
        this.handler = handler;
    }

    @Override
    public Supplier<RESULT> handle(ResultSet resultSet, SelectQuery query,
            ResultSetRowIndexConsumer consumer) throws SQLException {
        return handler.handle(resultSet, query, (index, next) -> {
            consumer.accept(index, next);
            if (index == 0 && next) {
                Sql<?> sql = query.getSql();
                throw new NonUniqueResultException(query.getConfig().getExceptionSqlLogType(), sql);
            }
        });
    }
}
