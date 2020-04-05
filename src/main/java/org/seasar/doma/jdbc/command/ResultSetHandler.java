package org.seasar.doma.jdbc.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;
import org.seasar.doma.jdbc.query.SelectQuery;

public interface ResultSetHandler<RESULT> {

  Supplier<RESULT> handle(
      ResultSet resultSet, SelectQuery query, ResultSetRowIndexConsumer consumer)
      throws SQLException;
}
