package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.InsertCommand;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.declaration.InsertIntoDeclaration;
import org.seasar.doma.jdbc.criteria.query.CriteriaQuery;
import org.seasar.doma.jdbc.criteria.query.InsertBuilder;

public class NativeSqlInsertTerminal extends AbstractStatement<Integer> {

  private final InsertIntoDeclaration declaration;

  public NativeSqlInsertTerminal(InsertContext context) {
    Objects.requireNonNull(context);
    this.declaration = new InsertIntoDeclaration(context);
  }

  @Override
  protected Command<Integer> createCommand(
      Config config, Function<String, String> commenter, SqlLogType sqlLogType) {
    InsertContext context = declaration.getContext();
    InsertBuilder builder = new InsertBuilder(config, context, commenter, sqlLogType);
    PreparedSql sql = builder.build();
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), executeMethodName);
    return new InsertCommand(query);
  }
}
