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

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Collectors;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.jdbc.entity.AbstractPostInsertContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreInsertContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.id.IdGenerationConfig;
import org.seasar.doma.message.Message;

/**
 * An auto batch insert query that is executed against an entity class.
 *
 * <p>This class implements {@link BatchInsertQuery} to provide batch INSERT operations for
 * entities. It handles ID generation, version initialization, and supports various duplicate key
 * handling strategies.
 *
 * @param <ENTITY> the entity type
 */
public class AutoBatchInsertQuery<ENTITY> extends AutoBatchModifyQuery<ENTITY>
    implements BatchInsertQuery {

  /** The property type for the generated ID. */
  protected GeneratedIdPropertyType<ENTITY, ?, ?> generatedIdPropertyType;

  /** The configuration for ID generation. */
  protected IdGenerationConfig idGenerationConfig;

  /** Whether batch operations are supported. */
  protected boolean batchSupported = true;

  /** Whether to ignore auto-generated keys. */
  protected boolean generatedKeysIgnored = false;

  /** The strategy for handling duplicate keys. */
  protected DuplicateKeyType duplicateKeyType = DuplicateKeyType.EXCEPTION;

  /** The names of columns that may cause duplicate key violations. */
  protected String[] duplicateKeyNames = EMPTY_STRINGS;

  /**
   * Constructs an instance.
   *
   * @param entityType the entity type
   */
  public AutoBatchInsertQuery(EntityType<ENTITY> entityType) {
    super(entityType);
  }

  /**
   * Sets whether to ignore auto-generated keys.
   *
   * @param generatedKeysIgnored whether to ignore auto-generated keys
   */
  public void setGeneratedKeysIgnored(boolean generatedKeysIgnored) {
    this.generatedKeysIgnored = generatedKeysIgnored;
  }

  /**
   * Sets the strategy for handling duplicate keys.
   *
   * @param duplicateKeyType the strategy for handling duplicate keys
   */
  public void setDuplicateKeyType(DuplicateKeyType duplicateKeyType) {
    this.duplicateKeyType = duplicateKeyType;
  }

  /**
   * Sets the names of columns that may cause duplicate key violations.
   *
   * @param duplicateKeyNames the names of columns that may cause duplicate key violations
   */
  public void setDuplicateKeyNames(String... duplicateKeyNames) {
    this.duplicateKeyNames = duplicateKeyNames;
  }

  /**
   * Prepares this query for execution.
   * 
   * <p>This method processes all entities in the batch, applying pre-insert hooks,
   * preparing ID and version values, and generating SQL statements for each entity.
   */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, entities, sqls);
    int size = entities.size();
    if (size == 0) {
      return;
    }
    executable = true;
    sqlExecutionSkipCause = null;
    currentEntity = entities.get(0);
    preInsert();
    prepareIdAndVersionPropertyTypes();
    prepareOptions();
    prepareTargetPropertyTypes();
    prepareIdValue();
    prepareVersionValue();
    prepareSql();
    entities.set(0, currentEntity);
    for (ListIterator<ENTITY> it = entities.listIterator(1); it.hasNext(); ) {
      currentEntity = it.next();
      preInsert();
      prepareIdValue();
      prepareVersionValue();
      prepareSql();
      it.set(currentEntity);
    }
    currentEntity = null;
    assertEquals(entities.size(), sqls.size());
  }

  /**
   * Executes pre-insert hooks on the current entity.
   * 
   * <p>This method creates a context for pre-insert processing and applies
   * entity-specific pre-insert logic to the current entity.
   */
  protected void preInsert() {
    AutoBatchPreInsertContext<ENTITY> context =
        new AutoBatchPreInsertContext<>(entityType, method, config, duplicateKeyType);
    entityType.preInsert(currentEntity, context);
    if (context.getNewEntity() != null) {
      currentEntity = context.getNewEntity();
    }
  }

  /**
   * Prepares ID and version property types for this query.
   * 
   * <p>This method initializes the generated ID property type and configures
   * ID generation settings, including whether batch operations and auto-generated
   * keys are supported.
   */
  @Override
  protected void prepareIdAndVersionPropertyTypes() {
    super.prepareIdAndVersionPropertyTypes();
    generatedIdPropertyType = entityType.getGeneratedIdPropertyType();
    if (generatedIdPropertyType != null) {
      if (idGenerationConfig == null) {
        idGenerationConfig = new IdGenerationConfig(config, entityType);
        generatedIdPropertyType.validateGenerationStrategy(idGenerationConfig);
        autoGeneratedKeysSupported =
            !generatedKeysIgnored
                && generatedIdPropertyType.isAutoGeneratedKeysSupported(idGenerationConfig);
        batchSupported =
            generatedKeysIgnored || generatedIdPropertyType.isBatchSupported(idGenerationConfig);
      }
    }
  }

  /**
   * Prepares the target property types for this query.
   * 
   * <p>This method determines which entity properties should be included in the INSERT
   * statement based on their insertability, ID status, and other criteria. It also
   * validates that non-generated ID properties have non-null values.
   */
  protected void prepareTargetPropertyTypes() {
    targetPropertyTypes = new ArrayList<>(entityType.getEntityPropertyTypes().size());
    for (EntityPropertyType<ENTITY, ?> propertyType : entityType.getEntityPropertyTypes()) {
      if (!propertyType.isInsertable()) {
        continue;
      }
      if (propertyType.isId()) {
        Property<ENTITY, ?> property = propertyType.createProperty();
        property.load(currentEntity);
        if (propertyType != generatedIdPropertyType
            || generatedIdPropertyType.isIncluded(
                idGenerationConfig, property.getWrapper().get())) {
          targetPropertyTypes.add(propertyType);
        }
        if (generatedIdPropertyType == null) {
          if (property.getWrapper().get() == null) {
            throw new JdbcException(Message.DOMA2020, entityType.getName(), propertyType.getName());
          }
        }
        continue;
      }
      if (!isTargetPropertyName(propertyType.getName())) {
        continue;
      }
      targetPropertyTypes.add(propertyType);
    }
  }

  /**
   * Prepares the ID value for the current entity.
   * 
   * <p>If a generated ID property type is configured, this method applies
   * pre-insert ID generation logic to the current entity.
   */
  protected void prepareIdValue() {
    if (generatedIdPropertyType != null && idGenerationConfig != null) {
      currentEntity =
          generatedIdPropertyType.preInsert(entityType, currentEntity, idGenerationConfig);
    }
  }

  /**
   * Prepares the version value for the current entity.
   * 
   * <p>If a version property type is configured, this method initializes
   * the version value to 1 for the current entity.
   */
  protected void prepareVersionValue() {
    if (versionPropertyType != null) {
      currentEntity = versionPropertyType.setIfNecessary(entityType, currentEntity, 1);
    }
  }

  /**
   * Prepares the SQL statement for the current entity.
   * 
   * <p>This method builds the appropriate SQL statement (INSERT or UPSERT)
   * based on the duplicate key handling strategy and dialect capabilities.
   */
  protected void prepareSql() {
    Naming naming = config.getNaming();
    Dialect dialect = config.getDialect();
    PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.BATCH_INSERT, sqlLogType);
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
    PreparedSql sql = builder.build(this::comment);
    sqls.add(sql);
  }

  private void assembleInsertSql(PreparedSqlBuilder builder, Naming naming, Dialect dialect) {
    builder.appendSql("insert into ");
    builder.appendSql(entityType.getQualifiedTableName(naming::apply, dialect::applyQuote));
    builder.appendSql(" (");
    for (EntityPropertyType<ENTITY, ?> p : targetPropertyTypes) {
      builder.appendSql(p.getColumnName(naming::apply, dialect::applyQuote));
      builder.appendSql(", ");
    }
    builder.cutBackSql(2);
    builder.appendSql(") values (");
    for (EntityPropertyType<ENTITY, ?> propertyType : targetPropertyTypes) {
      Property<ENTITY, ?> property = propertyType.createProperty();
      property.load(currentEntity);
      builder.appendParameter(property.asInParameter());
      builder.appendSql(", ");
    }
    builder.cutBackSql(2);
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
            currentEntity);
    UpsertAssembler upsertAssembler = dialect.getUpsertAssembler(context);
    upsertAssembler.assemble();
  }

  /**
   * Determines whether batch operations are supported for this query.
   * 
   * @return true if batch operations are supported, false otherwise
   */
  @Override
  public boolean isBatchSupported() {
    return batchSupported;
  }

  /**
   * Generates an ID for the entity at the specified index using auto-generated keys.
   * 
   * <p>This method retrieves the auto-generated key from the statement and
   * updates the entity with the generated ID value.
   * 
   * @param statement the statement that executed the insert
   * @param index the index of the entity in the batch
   */
  @Override
  public void generateId(Statement statement, int index) {
    if (isAutoGeneratedKeysSupported()) {
      ENTITY entity = entities.get(index);
      ENTITY newEntity =
          generatedIdPropertyType
              .postInsert(
                  entityType, Collections.singletonList(entity), idGenerationConfig, statement)
              .stream()
              .findFirst()
              .orElse(entity);
      entities.set(index, newEntity);
    }
  }

  /**
   * Generates IDs for a range of entities using auto-generated keys.
   * 
   * <p>This method retrieves auto-generated keys from the statement and
   * updates the specified range of entities with the generated ID values.
   * 
   * @param statement the statement that executed the batch insert
   * @param position the starting position in the entities list
   * @param size the number of entities to process
   * @throws DomaIllegalArgumentException if the position and size parameters are invalid
   */
  @Override
  public void generateIds(Statement statement, int position, int size) {
    if (isAutoGeneratedKeysSupported() && isBatchSupported()) {
      if ((position + size) > entities.size()) {
        throw new DomaIllegalArgumentException(
            "position or length",
            "position = "
                + position
                + ", size = "
                + size
                + ", entities.size() = "
                + entities.size());
      }
      List<ENTITY> subEntities = entities.subList(position, position + size);
      List<ENTITY> newEntities =
          generatedIdPropertyType
              .postInsert(entityType, subEntities, idGenerationConfig, statement)
              .stream()
              .toList();
      if (subEntities.size() == newEntities.size()) {
        subEntities.clear();
        subEntities.addAll(newEntities);
      }
    }
  }

  /**
   * Completes the batch insert operation.
   * 
   * <p>This method applies post-insert hooks to all entities in the batch
   * after the database operation has been executed.
   */
  @Override
  public void complete() {
    for (ListIterator<ENTITY> it = entities.listIterator(); it.hasNext(); ) {
      currentEntity = it.next();
      postInsert();
      it.set(currentEntity);
    }
  }

  /**
   * Executes post-insert hooks on the current entity.
   * 
   * <p>This method creates a context for post-insert processing and applies
   * entity-specific post-insert logic to the current entity.
   */
  protected void postInsert() {
    AutoBatchPostInsertContext<ENTITY> context =
        new AutoBatchPostInsertContext<>(entityType, method, config, duplicateKeyType);
    entityType.postInsert(currentEntity, context);
    if (context.getNewEntity() != null) {
      currentEntity = context.getNewEntity();
    }
  }

  /**
   * The context class for pre-insert batch processing.
   *
   * <p>This context is used during the pre-insert phase of batch insert operations
   * to provide entity-specific processing.
   *
   * @param <E> the entity type
   */
  protected static class AutoBatchPreInsertContext<E> extends AbstractPreInsertContext<E> {

    /**
     * Constructs an instance.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     * @param duplicateKeyType the strategy for handling duplicate keys
     */
    public AutoBatchPreInsertContext(
        EntityType<E> entityType, Method method, Config config, DuplicateKeyType duplicateKeyType) {
      super(entityType, method, config, duplicateKeyType);
    }
  }

  /**
   * The context class for post-insert batch processing.
   *
   * <p>This context is used during the post-insert phase of batch insert operations
   * to provide entity-specific processing after database operations have been executed.
   *
   * @param <E> the entity type
   */
  protected static class AutoBatchPostInsertContext<E> extends AbstractPostInsertContext<E> {

    /**
     * Constructs an instance.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     * @param duplicateKeyType the strategy for handling duplicate keys
     */
    public AutoBatchPostInsertContext(
        EntityType<E> entityType, Method method, Config config, DuplicateKeyType duplicateKeyType) {
      super(entityType, method, config, duplicateKeyType);
    }
  }
}
