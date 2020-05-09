package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.criteria.context.SelectSettings;
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
    SelectSettings settings = findSettings();
    SetOperationBuilder builder =
        new SetOperationBuilder(
            config, context, createCommenter(settings.getComment()), settings.getSqlLogType());
    PreparedSql sql = builder.build();
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), EXECUTE_METHOD_NAME);
    query.setFetchSize(settings.getQueryTimeout());
    query.setMaxRows(settings.getMaxRows());
    query.setQueryTimeout(settings.getQueryTimeout());
    return new SelectCommand<>(query, resultSetHandler);
  }

  private SelectSettings findSettings() {
    return context.accept(
        new SetOperationContext.Visitor<SelectSettings>() {
          @Override
          public SelectSettings visit(SetOperationContext.Select<?> select) {
            return select.context.getSettings();
          }

          @Override
          public SelectSettings visit(SetOperationContext.Union<?> union) {
            return union.left.accept(this);
          }

          @Override
          public SelectSettings visit(SetOperationContext.UnionAll<?> unionAll) {
            return unionAll.left.accept(this);
          }
        });
  }
}
