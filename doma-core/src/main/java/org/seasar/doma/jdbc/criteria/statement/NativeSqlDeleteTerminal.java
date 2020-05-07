package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.DeleteCommand;
import org.seasar.doma.jdbc.criteria.context.DeleteContext;
import org.seasar.doma.jdbc.criteria.context.Options;
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
    Options options = context.getOptions();
    DeleteBuilder builder =
        new DeleteBuilder(
            config, context, createCommenter(options.getComment()), options.getSqlLogType());
    PreparedSql sql = builder.build();
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), EXECUTE_METHOD_NAME);
    return new DeleteCommand(query);
  }
}
