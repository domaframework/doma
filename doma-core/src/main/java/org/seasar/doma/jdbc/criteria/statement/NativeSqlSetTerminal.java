package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.criteria.query.CriteriaQuery;
import org.seasar.doma.jdbc.criteria.query.SetOperationBuilder;

public class NativeSqlSetTerminal<RESULT> extends AbstractStatement<RESULT> {

  private final SetOperationContext<?> context;
  private final ResultSetHandler<RESULT> resultSetHandler;

  public NativeSqlSetTerminal(
      SetOperationContext<?> context, ResultSetHandler<RESULT> resultSetHandler) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(resultSetHandler);
    this.context = context;
    this.resultSetHandler = resultSetHandler;
  }

  @Override
  protected Command<RESULT> createCommand(
      Config config, Function<String, String> commenter, SqlLogType sqlLogType) {
    SetOperationBuilder builder = new SetOperationBuilder(config, context, commenter, sqlLogType);
    PreparedSql sql = builder.build();
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), executeMethodName);
    return new SelectCommand<>(query, resultSetHandler);
  }
}
