package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.criteria.context.Options;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.criteria.query.CriteriaQuery;
import org.seasar.doma.jdbc.criteria.query.SetOperationBuilder;

public class NativeSqlSetTerminal<RESULT>
    extends AbstractStatement<NativeSqlSetTerminal<RESULT>, RESULT> {

  private final SetOperationContext<?> context;
  private final ResultSetHandler<RESULT> resultSetHandler;

  public NativeSqlSetTerminal(
      Config config, SetOperationContext<?> context, ResultSetHandler<RESULT> resultSetHandler) {
    super(Objects.requireNonNull(config));
    this.context = Objects.requireNonNull(context);
    this.resultSetHandler = Objects.requireNonNull(resultSetHandler);
  }

  @Override
  protected Command<RESULT> createCommand() {
    Options options = findOptions();
    SetOperationBuilder builder =
        new SetOperationBuilder(
            config, context, createCommenter(options.comment()), options.sqlLogType());
    PreparedSql sql = builder.build();
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), EXECUTE_METHOD_NAME);
    return new SelectCommand<>(query, resultSetHandler);
  }

  private Options findOptions() {
    return context.accept(
        new SetOperationContext.Visitor<Options>() {
          @Override
          public Options visit(SetOperationContext.Select<?> select) {
            return select.context.getOptions();
          }

          @Override
          public Options visit(SetOperationContext.Union<?> union) {
            return union.left.accept(this);
          }

          @Override
          public Options visit(SetOperationContext.UnionAll<?> unionAll) {
            return unionAll.left.accept(this);
          }
        });
  }
}
