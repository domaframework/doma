package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.DeleteCommand;
import org.seasar.doma.jdbc.criteria.context.DeleteContext;
import org.seasar.doma.jdbc.criteria.declaration.DeleteFromDeclaration;
import org.seasar.doma.jdbc.criteria.query.CriteriaQuery;
import org.seasar.doma.jdbc.criteria.query.DeleteBuilder;

public class NativeSqlDeleteTerminal extends AbstractStatement<Integer> {

  private final DeleteFromDeclaration declaration;

  public NativeSqlDeleteTerminal(DeleteFromDeclaration declaration) {
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  @Override
  protected Command<Integer> createCommand(
      Config config, Function<String, String> commenter, SqlLogType sqlLogType) {
    DeleteContext context = declaration.getContext();
    DeleteBuilder builder = new DeleteBuilder(config, context, commenter, sqlLogType);
    PreparedSql sql = builder.build();
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), EXECUTE_METHOD_NAME);
    return new DeleteCommand(query);
  }
}
