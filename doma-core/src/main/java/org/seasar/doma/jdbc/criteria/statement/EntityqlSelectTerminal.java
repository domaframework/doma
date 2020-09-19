package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.command.AssociateCommand;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SelectSettings;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.query.CriteriaQuery;
import org.seasar.doma.jdbc.criteria.query.SelectBuilder;

public class EntityqlSelectTerminal<ENTITY>
    extends AbstractStatement<EntityqlSelectTerminal<ENTITY>, List<ENTITY>>
    implements Listable<ENTITY> {

  private final SelectFromDeclaration declaration;
  private final EntityMetamodel<ENTITY> entityMetamodel;

  public EntityqlSelectTerminal(
      Config config, SelectFromDeclaration declaration, EntityMetamodel<ENTITY> entityMetamodel) {
    super(Objects.requireNonNull(config));
    this.declaration = Objects.requireNonNull(declaration);
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
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
  public List<ENTITY> execute() {
    return super.execute();
  }

  @Override
  protected Command<List<ENTITY>> createCommand() {
    SelectContext context = declaration.getContext();
    SelectSettings settings = context.getSettings();
    SelectBuilder builder =
        new SelectBuilder(
            config, context, createCommenter(settings.getComment()), settings.getSqlLogType());
    PreparedSql sql = builder.build();
    CriteriaQuery query = new CriteriaQuery(config, sql, getClass().getName(), EXECUTE_METHOD_NAME);
    query.setFetchSize(settings.getFetchSize());
    query.setMaxRows(settings.getMaxRows());
    query.setQueryTimeout(settings.getQueryTimeout());
    return new AssociateCommand<ENTITY>(context, query, entityMetamodel) {
      @Override
      public List<ENTITY> execute() {
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
