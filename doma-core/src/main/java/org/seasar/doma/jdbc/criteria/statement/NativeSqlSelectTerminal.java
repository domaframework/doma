package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.FetchType;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SelectSettings;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.query.CriteriaQuery;
import org.seasar.doma.jdbc.criteria.query.SelectBuilder;

public class NativeSqlSelectTerminal<RESULT>
    extends AbstractStatement<NativeSqlSelectTerminal<RESULT>, RESULT> {

  private final SelectFromDeclaration declaration;
  private final ResultSetHandler<RESULT> resultSetHandler;

  private final Boolean returnsStream;

  public NativeSqlSelectTerminal(
      Config config, SelectFromDeclaration declaration, ResultSetHandler<RESULT> resultSetHandler) {
    this(config, declaration, resultSetHandler, false);
  }

  public NativeSqlSelectTerminal(
      Config config,
      SelectFromDeclaration declaration,
      ResultSetHandler<RESULT> resultSetHandler,
      boolean returnsStream) {
    super(Objects.requireNonNull(config));
    this.declaration = Objects.requireNonNull(declaration);
    this.resultSetHandler = Objects.requireNonNull(resultSetHandler);
    this.returnsStream = returnsStream;
  }

  /**
   * {@inheritDoc}
   *
   * @throws EmptyWhereClauseException if {@link SelectSettings#getAllowEmptyWhere()} returns
   *     {@literal false} and the WHERE clause is empty
   * @throws org.seasar.doma.jdbc.JdbcException if a JDBC related error occurs
   */
  @SuppressWarnings("EmptyMethod")
  @Override
  public RESULT execute() {
    return super.execute();
  }

  @Override
  protected Command<RESULT> createCommand() {
    SelectContext context = declaration.getContext();
    SelectSettings settings = context.getSettings();
    SelectBuilder builder =
        new SelectBuilder(
            config, context, createCommenter(settings.getComment()), settings.getSqlLogType());
    PreparedSql sql = builder.build();
    CriteriaQuery query = createCriteriaQuery(sql, settings);
    return new SelectCommand<RESULT>(query, resultSetHandler) {
      @Override
      public RESULT execute() {
        if (!settings.getAllowEmptyWhere()) {
          if (context.where.isEmpty()) {
            throw new EmptyWhereClauseException(sql);
          }
        }
        return super.execute();
      }
    };
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
}
