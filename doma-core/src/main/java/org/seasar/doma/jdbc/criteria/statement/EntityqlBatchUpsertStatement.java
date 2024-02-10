package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.command.BatchInsertCommand;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.context.UpdateSettings;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoBatchUpsertQuery;
import org.seasar.doma.jdbc.query.DuplicateKeyType;
import org.seasar.doma.jdbc.query.Query;

public class EntityqlBatchUpsertStatement<ENTITY>
    extends AbstractStatement<EntityqlBatchUpsertStatement<ENTITY>, BatchResult<ENTITY>>
    implements Statement<BatchResult<ENTITY>> {

  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final List<ENTITY> entities;
  private final InsertSettings settings;
  private final DuplicateKeyType duplicateKeyType;

  public EntityqlBatchUpsertStatement(
      Config config,
      EntityMetamodel<ENTITY> entityMetamodel,
      List<ENTITY> entities,
      InsertSettings settings,
      DuplicateKeyType duplicateKeyType) {
    super(Objects.requireNonNull(config));
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entities = Objects.requireNonNull(entities);
    this.settings = Objects.requireNonNull(settings);
    this.duplicateKeyType = Objects.requireNonNull(duplicateKeyType);
  }

  /**
   * {@inheritDoc}
   *
   * @throws EmptyWhereClauseException if {@link UpdateSettings#getAllowEmptyWhere()} returns
   *     {@literal false} and the WHERE clause is empty
   * @throws OptimisticLockException if the entity has a version property and an update count is
   *     {@literal 0}
   * @throws org.seasar.doma.jdbc.UniqueConstraintException if an unique constraint is violated
   * @throws org.seasar.doma.jdbc.JdbcException if a JDBC related error occurs
   */
  @SuppressWarnings("EmptyMethod")
  @Override
  public BatchResult<ENTITY> execute() {
    return super.execute();
  }

  @Override
  protected Command<BatchResult<ENTITY>> createCommand() {
    EntityType<ENTITY> entityType = entityMetamodel.asType();
    AutoBatchUpsertQuery<ENTITY> query =
        config.getQueryImplementors().createAutoBatchUpsertQuery(EXECUTE_METHOD, entityType);
    query.setMethod(EXECUTE_METHOD);
    query.setConfig(config);
    query.setEntities(entities);
    query.setDuplicateKeyType(duplicateKeyType);
    query.setCallerClassName(getClass().getName());
    query.setCallerMethodName(EXECUTE_METHOD_NAME);
    query.setQueryTimeout(settings.getQueryTimeout());
    query.setBatchSize(settings.getBatchSize());
    query.setSqlLogType(settings.getSqlLogType());
    query.setIncludedPropertyNames();
    query.setExcludedPropertyNames();
    query.setGeneratedKeysIgnored(settings.getIgnoreGeneratedKeys());
    query.setMessage(settings.getComment());
    query.prepare();
    BatchInsertCommand command =
        config.getCommandImplementors().createBatchInsertCommand(EXECUTE_METHOD, query);
    return new Command<BatchResult<ENTITY>>() {
      @Override
      public Query getQuery() {
        return query;
      }

      @Override
      public BatchResult<ENTITY> execute() {
        int[] counts = command.execute();
        query.complete();
        return new BatchResult<>(counts, query.getEntities());
      }
    };
  }
}
