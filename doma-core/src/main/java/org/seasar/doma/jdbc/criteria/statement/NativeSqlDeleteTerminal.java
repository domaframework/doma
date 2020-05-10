package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.DeleteCommand;
import org.seasar.doma.jdbc.criteria.context.DeleteContext;
import org.seasar.doma.jdbc.criteria.context.DeleteSettings;
import org.seasar.doma.jdbc.criteria.declaration.DeleteDeclaration;
import org.seasar.doma.jdbc.criteria.query.CriteriaQuery;
import org.seasar.doma.jdbc.criteria.query.DeleteBuilder;

public class NativeSqlDeleteTerminal extends AbstractStatement<NativeSqlDeleteTerminal, Integer> {

  private final DeleteDeclaration declaration;

  public NativeSqlDeleteTerminal(Config config, DeleteDeclaration declaration) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  @Override
  protected Command<Integer> createCommand() {
    DeleteContext context = declaration.getContext();
    DeleteSettings settings = context.getSettings();
    DeleteBuilder builder =
        new DeleteBuilder(
            config, context, createCommenter(settings.getComment()), settings.getSqlLogType());
    PreparedSql sql = builder.build();
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), EXECUTE_METHOD_NAME);
    query.setQueryTimeout(settings.getQueryTimeout());
    return new DeleteCommand(query) {
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
