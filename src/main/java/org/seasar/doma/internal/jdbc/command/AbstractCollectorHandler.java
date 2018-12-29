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
 * @param <TARGET> 処理対象
 * @param <RESULT> 結果
 */
public abstract class AbstractCollectorHandler<TARGET, RESULT> implements ResultSetHandler<RESULT> {

  protected final ResultSetHandler<RESULT> handler;

  public AbstractCollectorHandler(ResultSetHandler<RESULT> handler) {
    AssertionUtil.assertNotNull(handler);
    this.handler = handler;
  }

  @Override
  public Supplier<RESULT> handle(
      ResultSet resultSet, SelectQuery query, ResultSetRowIndexConsumer consumer)
      throws SQLException {
    return handler.handle(resultSet, query, consumer);
  }
}
