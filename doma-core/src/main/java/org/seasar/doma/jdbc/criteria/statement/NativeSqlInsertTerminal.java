package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.InsertCommand;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.declaration.InsertDeclaration;
import org.seasar.doma.jdbc.criteria.query.CriteriaQuery;
import org.seasar.doma.jdbc.criteria.query.InsertBuilder;

public class NativeSqlInsertTerminal extends AbstractStatement<NativeSqlInsertTerminal, Integer> {

  private final InsertDeclaration declaration;

  public NativeSqlInsertTerminal(Config config, InsertDeclaration declaration) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  /**
   * Create statement builder that inserts or updates
   *
   * @return selecting keys statement builder
   */
  public NativeSqlUpsertOnDuplicateKeyUpdateSelectingKeys onDuplicateKeyUpdate() {
    return new NativeSqlUpsertOnDuplicateKeyUpdateSelectingKeys(config, declaration);
  }

  /**
   * Create statement builder that inserts or ignore
   *
   * @return selecting keys statement builder
   */
  public NativeSqlUpsertOnDuplicateKeyIgnoreSelectingKeys onDuplicateKeyIgnore() {
    return new NativeSqlUpsertOnDuplicateKeyIgnoreSelectingKeys(config, declaration);
  }

  /**
   * {@inheritDoc}
   *
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
    InsertContext context = declaration.getContext();
    InsertSettings settings = context.getSettings();
    PreparedSql sql = getPreparedSql(context, settings);
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), EXECUTE_METHOD_NAME);
    query.setQueryTimeout(settings.getQueryTimeout());
    return new InsertCommand(query);
  }

  private PreparedSql getPreparedSql(InsertContext context, InsertSettings settings) {
    InsertBuilder builder =
        new InsertBuilder(
            config, context, createCommenter(settings.getComment()), settings.getSqlLogType());
    PreparedSql sql = builder.build();
    return sql;
  }
}
