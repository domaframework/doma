package org.seasar.doma.jdbc.query;

import java.lang.reflect.Method;
import org.seasar.doma.internal.jdbc.entity.AbstractPostInsertContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreInsertContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityType;

public class AutoBatchUpsertQuery<ENTITY> extends AutoBatchInsertQuery<ENTITY>
    implements BatchUpsertQuery {
  private DuplicateKeyType duplicateKeyType;

  public AutoBatchUpsertQuery(EntityType<ENTITY> entityType) {
    super(entityType);
  }

  @Override
  protected void preInsert() {
    AutoBatchPreUpsertContext<ENTITY> context =
        new AutoBatchPreUpsertContext<>(entityType, method, config, duplicateKeyType);
    entityType.preInsert(currentEntity, context);
    if (context.getNewEntity() != null) {
      currentEntity = context.getNewEntity();
    }
  }

  @Override
  protected void prepareSql() {
    Naming naming = config.getNaming();
    Dialect dialect = config.getDialect();
    PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.BATCH_UPSERT, sqlLogType);
    UpsertContext context =
        UpsertContextBuilder.fromEntity(
            builder,
            entityType,
            duplicateKeyType,
            naming,
            dialect,
            idPropertyTypes,
            targetPropertyTypes,
            currentEntity);
    UpsertBuilder upsertBuilderQuery = dialect.getUpsertBuilder(context);
    upsertBuilderQuery.build();
    PreparedSql sql = builder.build(this::comment);
    sqls.add(sql);
  }

  @Override
  public void setDuplicateKeyType(DuplicateKeyType duplicateKeyType) {
    this.duplicateKeyType = duplicateKeyType;
  }

  @Override
  protected void postInsert() {
    AutoBatchPostUpsertContext<ENTITY> context =
        new AutoBatchPostUpsertContext<>(entityType, method, config, duplicateKeyType);
    entityType.postInsert(currentEntity, context);
    if (context.getNewEntity() != null) {
      currentEntity = context.getNewEntity();
    }
  }

  protected static class AutoBatchPreUpsertContext<E> extends AbstractPreInsertContext<E> {

    private final DuplicateKeyType duplicateKeyType;

    public AutoBatchPreUpsertContext(
        EntityType<E> entityType, Method method, Config config, DuplicateKeyType duplicateKeyType) {
      super(entityType, method, config);
      this.duplicateKeyType = duplicateKeyType;
    }

    @Override
    public java.util.Optional<DuplicateKeyType> getDuplicateKeyType() {
      return java.util.Optional.of(duplicateKeyType);
    }
  }

  protected static class AutoBatchPostUpsertContext<E> extends AbstractPostInsertContext<E> {

    private final DuplicateKeyType duplicateKeyType;

    public AutoBatchPostUpsertContext(
        EntityType<E> entityType, Method method, Config config, DuplicateKeyType duplicateKeyType) {
      super(entityType, method, config);
      this.duplicateKeyType = duplicateKeyType;
    }

    @Override
    public java.util.Optional<DuplicateKeyType> getDuplicateKeyType() {
      return java.util.Optional.of(duplicateKeyType);
    }
  }
}
