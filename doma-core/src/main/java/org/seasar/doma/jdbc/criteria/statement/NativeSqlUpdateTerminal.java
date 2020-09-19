package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.UpdateCommand;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;
import org.seasar.doma.jdbc.criteria.context.UpdateSettings;
import org.seasar.doma.jdbc.criteria.declaration.UpdateDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import org.seasar.doma.jdbc.criteria.query.CriteriaQuery;
import org.seasar.doma.jdbc.criteria.query.UpdateBuilder;

public class NativeSqlUpdateTerminal extends AbstractStatement<NativeSqlUpdateTerminal, Integer> {

  private final UpdateDeclaration declaration;

  public NativeSqlUpdateTerminal(Config config, UpdateDeclaration declaration) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  public Statement<Integer> where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.where(block);
    return this;
  }

  /**
   * {@inheritDoc}
   *
   * @throws EmptyWhereClauseException if {@link UpdateSettings#getAllowEmptyWhere()} returns
   *     {@literal false} and the WHERE clause is empty
   * @throws org.seasar.doma.jdbc.UniqueConstraintException if an unique constraint is violated
   * @throws org.seasar.doma.jdbc.JdbcException if a JDBC related error occurs
   */
  @SuppressWarnings("EmptyMethod")
  @Override
  public Integer execute() {
    return super.execute();
  }

  @Override
  protected Command<Integer> createCommand() {
    UpdateContext context = declaration.getContext();
    UpdateSettings settings = context.getSettings();
    UpdateBuilder builder =
        new UpdateBuilder(
            config, context, createCommenter(settings.getComment()), settings.getSqlLogType());
    PreparedSql sql = builder.build();
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), EXECUTE_METHOD_NAME);
    query.setQueryTimeout(settings.getQueryTimeout());
    return new UpdateCommand(query) {
      @Override
      public Integer execute() {
        if (!settings.getAllowEmptyWhere()) {
          if (context.where.isEmpty()) {
            throw new EmptyWhereClauseException(sql);
          }
        }
        return super.execute();
      }
    };
  }
}
