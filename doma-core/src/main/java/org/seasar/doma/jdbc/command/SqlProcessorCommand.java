package org.seasar.doma.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.BiFunction;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.query.SqlProcessorQuery;

public class SqlProcessorCommand<RESULT> implements Command<RESULT> {

  protected final SqlProcessorQuery query;

  protected final BiFunction<Config, PreparedSql, RESULT> handler;

  public SqlProcessorCommand(
      SqlProcessorQuery query, BiFunction<Config, PreparedSql, RESULT> handler) {
    assertNotNull(query, handler);
    this.query = query;
    this.handler = handler;
  }

  @Override
  public SqlProcessorQuery getQuery() {
    return query;
  }

  @Override
  public RESULT execute() {
    return handler.apply(query.getConfig(), query.getSql());
  }
}
