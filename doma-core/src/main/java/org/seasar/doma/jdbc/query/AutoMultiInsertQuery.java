package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.seasar.doma.GenerationType;
import org.seasar.doma.internal.jdbc.entity.AbstractPostInsertContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreInsertContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.id.IdGenerationConfig;
import org.seasar.doma.message.Message;

public class AutoMultiInsertQuery<ENTITY> extends AutoModifyQuery<ENTITY> implements InsertQuery {

  protected List<ENTITY> entities;
  protected GeneratedIdPropertyType<ENTITY, ?, ?> generatedIdPropertyType;

  protected IdGenerationConfig idGenerationConfig;

  protected DuplicateKeyType duplicateKeyType = DuplicateKeyType.EXCEPTION;

  public AutoMultiInsertQuery(EntityType<ENTITY> entityType) {
    super(entityType);
  }

  @Override
  public void prepare() {
    Dialect dialect = config.getDialect();
    if (!dialect.supportsMultiRowInsertStatement()) {
      throw new JdbcException(Message.DOMA2236, dialect.getName());
    }

    super.prepare();
    assertNotNull(method, entityType, entities);
    if (entities.isEmpty()) {
      sqlExecutionSkipCause = SqlExecutionSkipCause.MULTI_INSERT_TARGET_NONEXISTENT;
      return;
    }
    executable = true;
    entity = entities.stream().findFirst().orElseThrow(IllegalStateException::new);
    preInsert();
    prepareSpecialPropertyTypes();
    prepareOptions();
    prepareTargetPropertyType();
    prepareIdValue();
    prepareVersionValue();
    prepareSql();
    assertNotNull(sql);
  }

  protected void preInsert() {
    ListIterator<ENTITY> iterator = entities.listIterator();
    while (iterator.hasNext()) {
      ENTITY entity = iterator.next();
      AutoPreInsertContext<ENTITY> context =
          new AutoPreInsertContext<>(entityType, method, config, duplicateKeyType);
      entityType.preInsert(entity, context);
      ENTITY newEntity = context.getNewEntity();
      if (newEntity != null) {
        iterator.set(newEntity);
      }
    }
  }

  @Override
  protected void prepareSpecialPropertyTypes() {
    super.prepareSpecialPropertyTypes();
    generatedIdPropertyType = entityType.getGeneratedIdPropertyType();
    if (generatedIdPropertyType != null) {
      GenerationType generationType = generatedIdPropertyType.getGenerationType();
      Dialect dialect = config.getDialect();
      if (generationType == GenerationType.IDENTITY
          && !dialect.supportsAutoIncrementWhenInsertingMultipleRows()) {
        throw new JdbcException(Message.DOMA2235, dialect.getName());
      }
      idGenerationConfig = new IdGenerationConfig(config, entityType);
      generatedIdPropertyType.validateGenerationStrategy(idGenerationConfig);
      autoGeneratedKeysSupported =
          generatedIdPropertyType.isAutoGeneratedKeysSupported(idGenerationConfig);
    }
  }

  protected void prepareTargetPropertyType() {
    targetPropertyTypes = new ArrayList<>(entityType.getEntityPropertyTypes().size());
    for (EntityPropertyType<ENTITY, ?> propertyType : entityType.getEntityPropertyTypes()) {
      if (!propertyType.isInsertable()) {
        continue;
      }
      Property<ENTITY, ?> property = propertyType.createProperty();
      property.load(entity);
      if (propertyType.isId()) {
        if (propertyType != generatedIdPropertyType
            || generatedIdPropertyType.isIncluded(idGenerationConfig)) {
          targetPropertyTypes.add(propertyType);
        }
        if (generatedIdPropertyType == null && property.getWrapper().get() == null) {
          throw new JdbcException(Message.DOMA2020, entityType.getName(), propertyType.getName());
        }
        continue;
      }
      if (propertyType.isVersion()) {
        targetPropertyTypes.add(propertyType);
        continue;
      }
      if (!isTargetPropertyName(propertyType.getName())) {
        continue;
      }
      targetPropertyTypes.add(propertyType);
    }
  }

  protected void prepareIdValue() {
    if (generatedIdPropertyType != null && idGenerationConfig != null) {
      List<ENTITY> newEntities =
          generatedIdPropertyType.preInsert(entityType, entities, idGenerationConfig);
      if (entities.size() == newEntities.size()) {
        entities = newEntities;
      }
    }
  }

  protected void prepareVersionValue() {
    if (versionPropertyType != null) {
      ListIterator<ENTITY> iterator = entities.listIterator();
      while (iterator.hasNext()) {
        ENTITY entity = iterator.next();
        ENTITY newEntity = versionPropertyType.setIfNecessary(entityType, entity, 1);
        iterator.set(newEntity);
      }
    }
  }

  protected void prepareSql() {
    Naming naming = config.getNaming();
    Dialect dialect = config.getDialect();
    PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.MULTI_INSERT, sqlLogType);
    if (duplicateKeyType == DuplicateKeyType.EXCEPTION) {
      assembleInsertSql(builder, naming, dialect);
    } else {
      assembleUpsertSql(builder, naming, dialect);
    }
    sql = builder.build(this::comment);
  }

  private void assembleInsertSql(PreparedSqlBuilder builder, Naming naming, Dialect dialect) {
    builder.appendSql("insert into ");
    builder.appendSql(entityType.getQualifiedTableName(naming::apply, dialect::applyQuote));
    builder.appendSql(" (");
    if (!targetPropertyTypes.isEmpty()) {
      for (EntityPropertyType<ENTITY, ?> propertyType : targetPropertyTypes) {
        builder.appendSql(propertyType.getColumnName(naming::apply, dialect::applyQuote));
        builder.appendSql(", ");
      }
      builder.cutBackSql(2);
    }
    builder.appendSql(") values ");
    if (!entities.isEmpty()) {
      for (ENTITY entity : entities) {
        builder.appendSql("(");
        if (!targetPropertyTypes.isEmpty()) {
          for (EntityPropertyType<ENTITY, ?> propertyType : targetPropertyTypes) {
            Property<ENTITY, ?> property = propertyType.createProperty();
            property.load(entity);
            builder.appendParameter(property.asInParameter());
            builder.appendSql(", ");
          }
          builder.cutBackSql(2);
        }
        builder.appendSql("), ");
      }
      builder.cutBackSql(2);
    }
  }

  private void assembleUpsertSql(PreparedSqlBuilder builder, Naming naming, Dialect dialect) {
    UpsertAssemblerContext context =
        UpsertAssemblerContextBuilder.buildFromEntity(
            builder,
            entityType,
            duplicateKeyType,
            naming,
            dialect,
            idPropertyTypes,
            targetPropertyTypes,
            entity);
    UpsertAssembler upsertAssembler = dialect.getUpsertAssembler(context);
    upsertAssembler.assemble();
    sql = builder.build(this::comment);
  }

  @Override
  public void generateId(Statement statement) {
    if (generatedIdPropertyType != null && idGenerationConfig != null) {
      List<ENTITY> newEntities =
          generatedIdPropertyType.postInsert(entityType, entities, idGenerationConfig, statement);
      if (entities.size() == newEntities.size()) {
        entities = newEntities;
      }
    }
  }

  @Override
  public void complete() {
    postInsert();
  }

  protected void postInsert() {
    ListIterator<ENTITY> iterator = entities.listIterator();
    while (iterator.hasNext()) {
      ENTITY entity = iterator.next();
      AutoPostInsertContext<ENTITY> context =
          new AutoPostInsertContext<>(entityType, method, config, duplicateKeyType);
      entityType.postInsert(entity, context);
      ENTITY newEntity = context.getNewEntity();
      if (newEntity != null) {
        iterator.set(newEntity);
      }
    }
  }

  protected static class AutoPreInsertContext<E> extends AbstractPreInsertContext<E> {

    public AutoPreInsertContext(
        EntityType<E> entityType, Method method, Config config, DuplicateKeyType duplicateKeyType) {
      super(entityType, method, config, duplicateKeyType);
    }
  }

  protected static class AutoPostInsertContext<E> extends AbstractPostInsertContext<E> {

    public AutoPostInsertContext(
        EntityType<E> entityType, Method method, Config config, DuplicateKeyType duplicateKeyType) {
      super(entityType, method, config, duplicateKeyType);
    }
  }

  public void setEntities(List<ENTITY> entities) {
    if (entities != null) {
      this.entities = new ArrayList<>(entities);
    }
  }

  public List<ENTITY> getEntities() {
    return this.entities;
  }

  @Override
  public void setEntity(ENTITY entity) {
    throw new UnsupportedOperationException("Use the setEntities method instead.");
  }

  @Override
  public ENTITY getEntity() {
    throw new UnsupportedOperationException("Use the getEntities method instead.");
  }
}