package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Supplier;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.ResultSetRowIndexConsumer;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * @author nakamura-to
 * @param <ELEMENT> 要素
 */
public abstract class AbstractResultListHandler<ELEMENT>
    implements ResultSetHandler<List<ELEMENT>> {

  protected final ResultSetHandler<List<ELEMENT>> handler;

  /** @param handler */
  public AbstractResultListHandler(ResultSetHandler<List<ELEMENT>> handler) {
    assertNotNull(handler);
    this.handler = handler;
  }

  @Override
  public Supplier<List<ELEMENT>> handle(
      ResultSet resultSet, SelectQuery query, ResultSetRowIndexConsumer consumer)
      throws SQLException {
    return handler.handle(resultSet, query, consumer);
  }
}
