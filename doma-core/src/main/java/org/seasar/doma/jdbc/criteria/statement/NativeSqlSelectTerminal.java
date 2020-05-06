package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.query.CriteriaQuery;
import org.seasar.doma.jdbc.criteria.query.SelectBuilder;

public class NativeSqlSelectTerminal<RESULT> extends AbstractStatement<RESULT> {

  private final SelectFromDeclaration declaration;
  private final ResultSetHandler<RESULT> resultSetHandler;

  public NativeSqlSelectTerminal(
      Config config, SelectFromDeclaration declaration, ResultSetHandler<RESULT> resultSetHandler) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(declaration);
    Objects.requireNonNull(resultSetHandler);
    this.declaration = declaration;
    this.resultSetHandler = resultSetHandler;
  }

  @Override
  protected Command<RESULT> createCommand(
      Config config, Function<String, String> commenter, SqlLogType sqlLogType) {
    SelectContext context = declaration.getContext();
    SelectBuilder builder = new SelectBuilder(config, context, commenter, sqlLogType);
    PreparedSql sql = builder.build();
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), EXECUTE_METHOD_NAME);
    return new SelectCommand<>(query, resultSetHandler);
  }
}
