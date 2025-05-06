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

/**
 * A query implementation for automatically inserting multiple entities into a database table.
 *
 * <p>This class provides functionality to generate and execute SQL multi-row INSERT statements
 * based on entity definitions. It handles various insert scenarios including:
 *
 * <ul>
 *   <li>Batch inserts with multiple entities in a single statement
 *   <li>Inserts with generated ID values (sequence-based only for multiple rows)
 *   <li>Inserts with version number initialization
 *   <li>Handling of duplicate key scenarios (exception or update)
 * </ul>
 *
 * <p>The query execution process includes:
 *
 * <ol>
 *   <li>Pre-insert entity processing for each entity
 *   <li>SQL statement preparation for multiple rows
 *   <li>Statement execution
 *   <li>Generated ID retrieval (if applicable)
 *   <li>Post-insert entity processing for each entity
 * </ol>
 *
 * @param <ENTITY> the entity type
 */
public class AutoMultiInsertQuery<ENTITY> extends AutoModifyQuery<ENTITY> implements InsertQuery {

  /** The list of entities to be inserted. */
  protected List<ENTITY> entities;

  /** The property type for the generated ID, if the entity has one. */
  protected GeneratedIdPropertyType<ENTITY, ?, ?> generatedIdPropertyType;

  /** Configuration for ID generation, used when the entity has a generated ID property. */
  protected IdGenerationConfig idGenerationConfig;

  /**
   * The strategy for handling duplicate key violations. Default is {@link
   * DuplicateKeyType#EXCEPTION}, which throws an exception on duplicate key.
   */
  protected DuplicateKeyType duplicateKeyType = DuplicateKeyType.EXCEPTION;

  /**
   * The names of properties that form the unique key for duplicate key handling. Used when {@link
   * #duplicateKeyType} is not {@link DuplicateKeyType#EXCEPTION}.
   */
  protected String[] duplicateKeyNames = EMPTY_STRINGS;

  /**
   * Constructs an instance.
   *
   * @param entityType the entity type
   */
  public AutoMultiInsertQuery(EntityType<ENTITY> entityType) {
    super(entityType);
  }

  /**
   * Prepares this query for execution.
   *
   * <p>This method performs the following operations:
   *
   * <ol>
   *   <li>Validates that required components are not null
   *   <li>Checks if the dialect supports multi-row INSERT statements
   *   <li>Skips execution if the entity list is empty
   *   <li>Executes pre-insert entity processing for each entity
   *   <li>Prepares special property types (ID, version, etc.)
   *   <li>Prepares query options
   *   <li>Determines target properties for the INSERT statement
   *   <li>Prepares ID and version values
   *   <li>Builds the SQL statement
   * </ol>
   */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, entityType, entities);

    Dialect dialect = config.getDialect();
    if (!dialect.supportsMultiRowInsertStatement()) {
      throw new JdbcException(Message.DOMA2236, dialect.getName());
    }

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

  /**
   * Executes pre-insert entity processing for each entity.
   *
   * <p>This method creates a pre-insert context for each entity and calls the entity's preInsert
   * method, allowing entity listeners to modify the entities before insertion.
   */
  protected void preInsert() {
    ListIterator<ENTITY> iterator = entities.listIterator();
    while (iterator.hasNext()) {
      ENTITY entity = iterator.next();
      AutoPreInsertContext<ENTITY> context =
          new AutoPreInsertContext<>(entityType, method, config, duplicateKeyType, returning);
      entityType.preInsert(entity, context);
      ENTITY newEntity = context.getNewEntity();
      if (newEntity != null) {
        iterator.set(newEntity);
      }
    }
  }

  /**
   * Prepares special property types for this query.
   *
   * <p>This method initializes the generated ID property type and its configuration, and determines
   * if auto-generated keys are supported for this query.
   *
   * <p>It also checks if the dialect supports auto-increment when inserting multiple rows, and
   * throws an exception if not supported with IDENTITY generation type.
   */
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

  /**
   * Prepares the target property types for the INSERT statement.
   *
   * <p>This method determines which entity properties should be included in the INSERT statement
   * based on the following rules:
   *
   * <ul>
   *   <li>Properties must be insertable
   *   <li>ID properties are included if they are not auto-generated or if they have a value
   *   <li>Version properties are always included
   *   <li>Properties must match the include/exclude name filters if specified
   * </ul>
   *
   * <p>This method throws a JdbcException if a non-generated ID property has a null value.
   */
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

  /**
   * Prepares the ID values for the entities before insertion.
   *
   * <p>If the entities have a generated ID property, this method calls its preInsert method to
   * generate or prepare the ID values before the INSERT operation.
   */
  protected void prepareIdValue() {
    if (generatedIdPropertyType != null && idGenerationConfig != null) {
      List<ENTITY> newEntities =
          generatedIdPropertyType.preInsert(entityType, entities, idGenerationConfig);
      if (entities.size() == newEntities.size()) {
        entities = newEntities;
      }
    }
  }

  /**
   * Prepares the version values for the entities before insertion.
   *
   * <p>If the entities have a version property, this method initializes it to 1 for optimistic
   * locking.
   */
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

  /**
   * Prepares the SQL statement for this query.
   *
   * <p>This method builds either a standard multi-row INSERT statement or an UPSERT statement based
   * on the duplicate key handling strategy. It uses the dialect-specific SQL assemblers to generate
   * the appropriate SQL syntax.
   *
   * <p>If the duplicate key type is EXCEPTION, a standard INSERT statement is generated. Otherwise,
   * an UPSERT statement is generated, unless the dialect supports MERGE statements and an identity
   * key is included in the duplicate keys, in which case it falls back to a standard INSERT.
   */
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

  /**
   * Assembles a standard multi-row INSERT SQL statement.
   *
   * <p>This method creates a multi-insert assembler context and uses the dialect-specific
   * multi-insert assembler to generate the SQL statement.
   *
   * @param builder the SQL builder
   * @param naming the naming convention
   * @param dialect the database dialect
   */
  private void assembleInsertSql(PreparedSqlBuilder builder, Naming naming, Dialect dialect) {
    MultiInsertAssemblerContext<ENTITY> context =
        MultiInsertAssemblerContextBuilder.buildFromEntityList(
            builder, entityType, naming, dialect, targetPropertyTypes, entities, returning);
    MultiInsertAssembler assembler = dialect.getMultiInsertAssembler(context);
    assembler.assemble();
  }

  /**
   * Assembles an UPSERT SQL statement for multiple entities.
   *
   * <p>This method creates an UPSERT assembler context and uses the dialect-specific UPSERT
   * assembler to generate the SQL statement. The UPSERT statement handles duplicate key scenarios
   * according to the specified duplicate key type.
   *
   * @param builder the SQL builder
   * @param naming the naming convention
   * @param dialect the database dialect
   */
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
            entities,
            returning);
    UpsertAssembler assembler = dialect.getUpsertAssembler(context);
    assembler.assemble();
  }

  /**
   * Generates IDs for the inserted entities.
   *
   * <p>This method is called after executing the INSERT statement to retrieve and set
   * auto-generated keys for the entities. It's only executed if auto-generated keys are supported
   * for this query.
   *
   * @param statement the statement used for the INSERT operation
   */
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

  /**
   * Completes this query by executing post-insert processing.
   *
   * <p>This method is called after the INSERT statement has been executed and any generated IDs
   * have been retrieved.
   */
  @Override
  public void complete() {
    postInsert();
  }

  /**
   * Executes post-insert entity processing for each entity.
   *
   * <p>This method creates a post-insert context for each entity and calls the entity's postInsert
   * method, allowing entity listeners to modify the entities after insertion.
   */
  protected void postInsert() {
    ListIterator<ENTITY> iterator = entities.listIterator();
    while (iterator.hasNext()) {
      ENTITY entity = iterator.next();
      AutoPostInsertContext<ENTITY> context =
          new AutoPostInsertContext<>(entityType, method, config, duplicateKeyType, returning);
      entityType.postInsert(entity, context);
      ENTITY newEntity = context.getNewEntity();
      if (newEntity != null) {
        iterator.set(newEntity);
      }
    }
  }

  /**
   * Sets the strategy for handling duplicate key violations.
   *
   * @param duplicateKeyType the duplicate key handling strategy
   */
  public void setDuplicateKeyType(DuplicateKeyType duplicateKeyType) {
    this.duplicateKeyType = duplicateKeyType;
  }

  /**
   * Sets the names of properties that form the unique key for duplicate key handling.
   *
   * @param duplicateKeyNames the property names that form the unique key
   */
  public void setDuplicateKeyNames(String... duplicateKeyNames) {
    this.duplicateKeyNames = duplicateKeyNames;
  }

  /**
   * A context class for pre-insert entity processing.
   *
   * <p>This class extends AbstractPreInsertContext to add support for returning properties. It's
   * used to pass information to entity listeners during the pre-insert phase.
   *
   * @param <E> the entity type
   */
  protected static class AutoPreInsertContext<E> extends AbstractPreInsertContext<E> {

    /** The properties to be returned from the INSERT statement. */
    private final ReturningProperties returningProperties;

    /**
     * Constructs an instance.
     *
     * @param entityType the entity type
     * @param method the method that triggered this context
     * @param config the configuration
     * @param duplicateKeyType the duplicate key handling strategy
     * @param returningProperties the properties to be returned
     */
    public AutoPreInsertContext(
        EntityType<E> entityType,
        Method method,
        Config config,
        DuplicateKeyType duplicateKeyType,
        ReturningProperties returningProperties) {
      super(entityType, method, config, duplicateKeyType);
      this.returningProperties = Objects.requireNonNull(returningProperties);
    }

    /**
     * Returns the properties to be returned from the INSERT statement.
     *
     * @return the returning properties
     */
    @Override
    public ReturningProperties getReturningProperties() {
      return returningProperties;
    }
  }

  /**
   * A context class for post-insert entity processing.
   *
   * <p>This class extends AbstractPostInsertContext to add support for returning properties. It's
   * used to pass information to entity listeners during the post-insert phase.
   *
   * @param <E> the entity type
   */
  protected static class AutoPostInsertContext<E> extends AbstractPostInsertContext<E> {

    /** The properties to be returned from the INSERT statement. */
    private final ReturningProperties returningProperties;

    /**
     * Constructs an instance.
     *
     * @param entityType the entity type
     * @param method the method that triggered this context
     * @param config the configuration
     * @param duplicateKeyType the duplicate key handling strategy
     * @param returningProperties the properties to be returned
     */
    public AutoPostInsertContext(
        EntityType<E> entityType,
        Method method,
        Config config,
        DuplicateKeyType duplicateKeyType,
        ReturningProperties returningProperties) {
      super(entityType, method, config, duplicateKeyType);
      this.returningProperties = Objects.requireNonNull(returningProperties);
    }

    /**
     * Returns the properties to be returned from the INSERT statement.
     *
     * @return the returning properties
     */
    @Override
    public ReturningProperties getReturningProperties() {
      return returningProperties;
    }
  }

  /**
   * Sets the list of entities to be inserted.
   *
   * @param entities the list of entities
   */
  public void setEntities(List<ENTITY> entities) {
    if (entities != null) {
      this.entities = new ArrayList<>(entities);
    }
  }

  /**
   * Returns the list of entities to be inserted.
   *
   * @return the list of entities
   */
  public List<ENTITY> getEntities() {
    return this.entities;
  }

  /**
   * This method is not supported for multi-insert queries. Use {@link #setEntities(List)} instead.
   *
   * @param entity the entity
   * @throws UnsupportedOperationException always thrown
   */
  @Override
  public void setEntity(ENTITY entity) {
    throw new UnsupportedOperationException("Use the setEntities method instead.");
  }

  /**
   * This method is not supported for multi-insert queries. Use {@link #getEntities()} instead.
   *
   * @return never returns
   * @throws UnsupportedOperationException always thrown
   */
  @Override
  public ENTITY getEntity() {
    throw new UnsupportedOperationException("Use the getEntities method instead.");
  }
}
