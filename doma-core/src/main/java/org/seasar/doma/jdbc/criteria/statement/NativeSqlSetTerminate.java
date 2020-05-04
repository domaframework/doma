package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.criteria.command.SetOperationResultIterationHandler;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.criteria.query.CriteriaQuery;
import org.seasar.doma.jdbc.criteria.query.SetOperationBuilder;

public class NativeSqlSetTerminate<ELEMENT> extends AbstractStatement<List<ELEMENT>>
    implements SelectStatement<ELEMENT> {

  private final SetOperationContext<ELEMENT> context;
  private final Function<Row, ELEMENT> mapper;

  public NativeSqlSetTerminate(
      SetOperationContext<ELEMENT> context, Function<Row, ELEMENT> mapper) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(mapper);
    this.context = context;
    this.mapper = mapper;
  }

  @Override
  protected Command<List<ELEMENT>> createCommand(
      Config config, Function<String, String> commenter, SqlLogType sqlLogType) {
    SetOperationBuilder builder = new SetOperationBuilder(config, context, commenter, sqlLogType);
    PreparedSql sql = builder.build();
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), executeMethodName);
    ResultSetHandler<List<ELEMENT>> handler = new SetOperationResultIterationHandler<>(mapper);
    return new SelectCommand<>(query, handler);
  }
}
