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
 * @param <RESULT> the result type
 * @see SqlProcessor
 */
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
  public RESULT execute() {
    return handler.apply(query.getConfig(), query.getSql());
  }
}
