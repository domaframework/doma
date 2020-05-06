package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.InsertCommand;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.context.Options;
import org.seasar.doma.jdbc.criteria.declaration.InsertDeclaration;
import org.seasar.doma.jdbc.criteria.query.CriteriaQuery;
import org.seasar.doma.jdbc.criteria.query.InsertBuilder;

public class NativeSqlInsertTerminal extends AbstractStatement<Integer> {

  private final InsertDeclaration declaration;

  public NativeSqlInsertTerminal(Config config, InsertDeclaration declaration) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  @Override
  protected Command<Integer> createCommand() {
    InsertContext context = declaration.getContext();
    Options options = context.getOptions();
    InsertBuilder builder =
        new InsertBuilder(
            config, context, createCommenter(options.comment()), options.sqlLogType());
    PreparedSql sql = builder.build();
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), EXECUTE_METHOD_NAME);
    return new InsertCommand(query);
  }
}
