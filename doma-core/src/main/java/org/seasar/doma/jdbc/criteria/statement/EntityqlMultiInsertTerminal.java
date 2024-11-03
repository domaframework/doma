package org.seasar.doma.jdbc.criteria.statement;

import static org.seasar.doma.jdbc.criteria.statement.EntityqlMultiInsertStatement.EMPTY_SQL;

import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.MultiResult;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.InsertCommand;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoMultiInsertQuery;
import org.seasar.doma.jdbc.query.DuplicateKeyType;
import org.seasar.doma.jdbc.query.Query;

public class EntityqlMultiInsertTerminal<ENTITY>
    extends AbstractStatement<EntityqlMultiInsertTerminal<ENTITY>, MultiResult<ENTITY>> {

  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final List<ENTITY> entities;
  private final InsertSettings settings;
  private final DuplicateKeyType duplicateKeyType;
  private final List<PropertyMetamodel<?>> keys;

  public EntityqlMultiInsertTerminal(
      Config config,
      EntityMetamodel<ENTITY> entityMetamodel,
      List<ENTITY> entities,
      InsertSettings settings,
      DuplicateKeyType duplicateKeyType,
      List<PropertyMetamodel<?>> keys) {
    super(Objects.requireNonNull(config));
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entities = Objects.requireNonNull(entities);
    this.settings = Objects.requireNonNull(settings);
    this.duplicateKeyType = Objects.requireNonNull(duplicateKeyType);
    this.keys = Objects.requireNonNull(keys);
  }

  /**
   * {@inheritDoc}
   *
   * @throws org.seasar.doma.jdbc.UniqueConstraintException if an unique constraint is violated
   * @throws org.seasar.doma.jdbc.JdbcException if a JDBC related error occurs
   */
  @SuppressWarnings("EmptyMethod")
  @Override
  public MultiResult<ENTITY> execute() {
    return super.execute();
  }

  @Override
  protected Command<MultiResult<ENTITY>> createCommand() {
    EntityType<ENTITY> entityType = entityMetamodel.asType();
    AutoMultiInsertQuery<ENTITY> query =
        config.getQueryImplementors().createAutoMultiInsertQuery(EXECUTE_METHOD, entityType);
    query.setMethod(EXECUTE_METHOD);
    query.setConfig(config);
    query.setEntities(entities);
    query.setCallerClassName(getClass().getName());
    query.setCallerMethodName(EXECUTE_METHOD_NAME);
    query.setQueryTimeout(settings.getQueryTimeout());
    query.setSqlLogType(settings.getSqlLogType());
    query.setIncludedPropertyNames(
        settings.include().stream().map(PropertyMetamodel::getName).toArray(String[]::new));
    query.setExcludedPropertyNames(
        settings.exclude().stream().map(PropertyMetamodel::getName).toArray(String[]::new));
    query.setMessage(settings.getComment());
    query.setDuplicateKeyType(this.duplicateKeyType);
    query.setDuplicateKeyNames(
        keys.stream().map(PropertyMetamodel::getName).toArray(String[]::new));
    query.prepare();
    InsertCommand command =
        config.getCommandImplementors().createInsertCommand(EXECUTE_METHOD, query);
    return new Command<>() {
      @Override
      public Query getQuery() {
        return query;
      }

      @Override
      public MultiResult<ENTITY> execute() {
        int count = command.execute();
        query.complete();
        return new MultiResult<>(count, query.getEntities());
      }
    };
  }

  @Override
  public Sql<?> asSql() {
    if (entities.isEmpty()) {
      return EMPTY_SQL;
    }
    return super.asSql();
  }
}