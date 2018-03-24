package org.seasar.doma.jdbc.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.seasar.doma.internal.jdbc.command.ResultSetState;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * A {@link ResultSet} handler.
 * 
 * @param <RESULT>
 *            the result type
 */
public interface ResultSetHandler<RESULT> {

    Supplier<RESULT> handle(ResultSet resultSet, SelectQuery query,
                            Consumer<ResultSetState> stateChecker) throws SQLException;
}
