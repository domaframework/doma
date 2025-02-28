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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.seasar.doma.internal.jdbc.entity.AbstractPostInsertContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreInsertContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.id.IdGenerationConfig;
import org.seasar.doma.message.Message;

public class AutoInsertQuery<ENTITY> extends AutoModifyQuery<ENTITY> implements InsertQuery {

  protected boolean nullExcluded;

  protected GeneratedIdPropertyType<ENTITY, ?, ?> generatedIdPropertyType;

  protected IdGenerationConfig idGenerationConfig;

  protected DuplicateKeyType duplicateKeyType = DuplicateKeyType.EXCEPTION;

  protected String[] duplicateKeyNames = EMPTY_STRINGS;

  public AutoInsertQuery(EntityType<ENTITY> entityType) {
    super(entityType);
  }

  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, entityType, entity);
    executable = true;
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
    AutoPreInsertContext<ENTITY> context =
        new AutoPreInsertContext<>(entityType, method, config, duplicateKeyType);
    entityType.preInsert(entity, context);
    if (context.getNewEntity() != null) {
      entity = context.getNewEntity();
    }
  }

  @Override
  protected void prepareSpecialPropertyTypes() {
    super.prepareSpecialPropertyTypes();
    generatedIdPropertyType = entityType.getGeneratedIdPropertyType();
    if (generatedIdPropertyType != null) {
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
            || generatedIdPropertyType.isIncluded(idGenerationConfig, property.get())) {
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
      if (nullExcluded) {
        if (property.getWrapper().get() == null) {
          continue;
        }
      }
      if (!isTargetPropertyName(propertyType.getName())) {
        continue;
      }
      targetPropertyTypes.add(propertyType);
    }
  }

  protected void prepareIdValue() {
    if (generatedIdPropertyType != null && idGenerationConfig != null) {
      entity = generatedIdPropertyType.preInsert(entityType, entity, idGenerationConfig);
    }
  }

  protected void prepareVersionValue() {
    if (versionPropertyType != null) {
      entity = versionPropertyType.setIfNecessary(entityType, entity, 1);
    }
  }

  protected void prepareSql() {
    Naming naming = config.getNaming();
    Dialect dialect = config.getDialect();
    PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.INSERT, sqlLogType);
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
    builder.appendSql(") values (");
    if (!targetPropertyTypes.isEmpty()) {
      for (EntityPropertyType<ENTITY, ?> propertyType : targetPropertyTypes) {
        Property<ENTITY, ?> property = propertyType.createProperty();
        property.load(entity);
        builder.appendParameter(property.asInParameter());
        builder.appendSql(", ");
      }
      builder.cutBackSql(2);
    }
    builder.appendSql(")");
  }

  private void assembleUpsertSql(PreparedSqlBuilder builder, Naming naming, Dialect dialect) {
    List<EntityPropertyType<ENTITY, ?>> duplicateKeys =
        Arrays.stream(this.duplicateKeyNames)
            .map(entityType::getEntityPropertyType)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    UpsertAssemblerContext context =
        UpsertAssemblerContextBuilder.buildFromEntity(
            builder,
            entityType,
            duplicateKeyType,
            duplicateKeys,
            naming,
            dialect,
            idPropertyTypes,
            targetPropertyTypes,
            entity);
    UpsertAssembler upsertAssembler = dialect.getUpsertAssembler(context);
    upsertAssembler.assemble();
  }

  @Override
  public void generateId(Statement statement) {
    if (isAutoGeneratedKeysSupported()) {
      entity =
          generatedIdPropertyType
              .postInsert(
                  entityType, Collections.singletonList(entity), idGenerationConfig, statement)
              .stream()
              .findFirst()
              .orElse(entity);
    }
  }

  @Override
  public void complete() {
    postInsert();
  }

  protected void postInsert() {
    AutoPostInsertContext<ENTITY> context =
        new AutoPostInsertContext<>(entityType, method, config, duplicateKeyType);
    entityType.postInsert(entity, context);
    if (context.getNewEntity() != null) {
      entity = context.getNewEntity();
    }
  }

  public void setNullExcluded(boolean nullExcluded) {
    this.nullExcluded = nullExcluded;
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
}
