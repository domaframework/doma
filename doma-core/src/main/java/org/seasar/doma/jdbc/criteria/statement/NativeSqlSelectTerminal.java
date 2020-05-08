package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.criteria.context.Options;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.query.CriteriaQuery;
import org.seasar.doma.jdbc.criteria.query.SelectBuilder;

public class NativeSqlSelectTerminal<RESULT>
    extends AbstractStatement<NativeSqlSelectTerminal<RESULT>, RESULT> {

  private final SelectFromDeclaration declaration;
  private final ResultSetHandler<RESULT> resultSetHandler;

  public NativeSqlSelectTerminal(
      Config config, SelectFromDeclaration declaration, ResultSetHandler<RESULT> resultSetHandler) {
    super(Objects.requireNonNull(config));
    this.declaration = Objects.requireNonNull(declaration);
    this.resultSetHandler = Objects.requireNonNull(resultSetHandler);
  }

  @Override
  protected Command<RESULT> createCommand() {
    SelectContext context = declaration.getContext();
    Options options = context.getOptions();
    SelectBuilder builder =
        new SelectBuilder(
            config, context, createCommenter(options.comment()), options.sqlLogType());
    PreparedSql sql = builder.build();
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), EXECUTE_METHOD_NAME);
    return new SelectCommand<>(query, resultSetHandler);
  }
}
