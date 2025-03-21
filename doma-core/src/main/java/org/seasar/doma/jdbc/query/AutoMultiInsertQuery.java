/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Collectors;
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

  protected String[] duplicateKeyNames = EMPTY_STRINGS;

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
            || generatedIdPropertyType.isIncluded(
                idGenerationConfig, property.getWrapper().get())) {
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
      if (dialect.supportsUpsertEmulationWithMergeStatement()
          && QueryUtil.isIdentityKeyIncludedInDuplicateKeys(
              generatedIdPropertyType, duplicateKeyNames)) {
        // fallback to INSERT
        assembleInsertSql(builder, naming, dialect);
      } else {
        assembleUpsertSql(builder, naming, dialect);
      }
    }
    sql = builder.build(this::comment);
  }

  private void assembleInsertSql(PreparedSqlBuilder builder, Naming naming, Dialect dialect) {
    MultiInsertAssemblerContext<ENTITY> context =
        MultiInsertAssemblerContextBuilder.buildFromEntityList(
            builder, entityType, naming, dialect, targetPropertyTypes, entities);
    MultiInsertAssembler assembler = dialect.getMultiInsertAssembler(context);
    assembler.assemble();
  }

  private void assembleUpsertSql(PreparedSqlBuilder builder, Naming naming, Dialect dialect) {
    List<EntityPropertyType<ENTITY, ?>> duplicateKeys =
        Arrays.stream(this.duplicateKeyNames)
            .map(entityType::getEntityPropertyType)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    UpsertAssemblerContext context =
        UpsertAssemblerContextBuilder.buildFromEntityList(
            builder,
            entityType,
            duplicateKeyType,
            duplicateKeys,
            naming,
            dialect,
            idPropertyTypes,
            targetPropertyTypes,
            entities);
    UpsertAssembler assembler = dialect.getUpsertAssembler(context);
    assembler.assemble();
  }

  @Override
  public void generateId(Statement statement) {
    if (isAutoGeneratedKeysSupported()) {
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

  public void setDuplicateKeyType(DuplicateKeyType duplicateKeyType) {
    this.duplicateKeyType = duplicateKeyType;
  }

  public void setDuplicateKeyNames(String... duplicateKeyNames) {
    this.duplicateKeyNames = duplicateKeyNames;
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
