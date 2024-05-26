package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.FetchType;
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

  private final boolean returnsStream;

  public NativeSqlSetTerminal(
      Config config, SetOperationContext<?> context, ResultSetHandler<RESULT> resultSetHandler) {
    this(config, context, resultSetHandler, false);
  }

  public NativeSqlSetTerminal(
      Config config,
      SetOperationContext<?> context,
      ResultSetHandler<RESULT> resultSetHandler,
      boolean returnsStream) {
    super(Objects.requireNonNull(config));
    this.context = Objects.requireNonNull(context);
    this.resultSetHandler = Objects.requireNonNull(resultSetHandler);
    this.returnsStream = returnsStream;
  }

  /**
   * {@inheritDoc}
   *
   * @throws org.seasar.doma.jdbc.JdbcException if a JDBC related error occurs
   */
  @SuppressWarnings("EmptyMethod")
  @Override
  public RESULT execute() {
    return super.execute();
  }

  @Override
  protected Command<RESULT> createCommand() {
    SelectSettings settings = findSettings();
    SetOperationBuilder builder =
        new SetOperationBuilder(
            config, context, createCommenter(settings.getComment()), settings.getSqlLogType());
    PreparedSql sql = builder.build();
    CriteriaQuery query = createCriteriaQuery(sql, settings);
    return new SelectCommand<>(query, resultSetHandler);
  }

  private CriteriaQuery createCriteriaQuery(PreparedSql sql, SelectSettings settings) {
    CriteriaQuery query =
        new CriteriaQuery(config, sql, getClass().getName(), EXECUTE_METHOD_NAME) {

          @Override
          public boolean isResultStream() {
            return returnsStream;
          }

          @Override
          public FetchType getFetchType() {
            return returnsStream ? FetchType.LAZY : FetchType.EAGER;
          }
        };
    query.setFetchSize(settings.getFetchSize());
    query.setMaxRows(settings.getMaxRows());
    query.setQueryTimeout(settings.getQueryTimeout());
    return query;
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
