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

/**
 * A query implementation for automatically inserting an entity into a database table.
 *
 * <p>This class provides functionality to generate and execute SQL INSERT statements based on
 * entity definitions. It handles various insert scenarios including:
 *
 * <ul>
 *   <li>Standard inserts with all entity properties
 *   <li>Inserts with null property exclusion
 *   <li>Inserts with generated ID values (identity or sequence-based)
 *   <li>Inserts with version number initialization
 *   <li>Handling of duplicate key scenarios (exception or update)
 * </ul>
 *
 * <p>The query execution process includes:
 *
 * <ol>
 *   <li>Pre-insert entity processing
 *   <li>SQL statement preparation
 *   <li>Statement execution
 *   <li>Generated ID retrieval (if applicable)
 *   <li>Post-insert entity processing
 * </ol>
 *
 * @param <ENTITY> the entity type
 */
public class AutoInsertQuery<ENTITY> extends AutoModifyQuery<ENTITY> implements InsertQuery {

  /** Indicates whether null properties should be excluded from the INSERT statement. */
  protected boolean nullExcluded;

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
  public AutoInsertQuery(EntityType<ENTITY> entityType) {
    super(entityType);
  }

  /**
   * Prepares this query for execution.
   *
   * <p>This method performs the following operations:
   *
   * <ol>
   *   <li>Validates that required components are not null
   *   <li>Executes pre-insert entity processing
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

  /**
   * Executes pre-insert entity processing.
   *
   * <p>This method creates a pre-insert context and calls the entity's preInsert method, allowing
   * entity listeners to modify the entity before insertion.
   */
  protected void preInsert() {
    AutoPreInsertContext<ENTITY> context =
        new AutoPreInsertContext<>(entityType, method, config, duplicateKeyType, returning);
    entityType.preInsert(entity, context);
    if (context.getNewEntity() != null) {
      entity = context.getNewEntity();
    }
  }

  /**
   * Prepares special property types for this query.
   *
   * <p>This method initializes the generated ID property type and its configuration, and determines
   * if auto-generated keys are supported for this query.
   */
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
   *   <li>If nullExcluded is true, null-valued properties are excluded
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

  /**
   * Prepares the ID value for the entity before insertion.
   *
   * <p>If the entity has a generated ID property, this method calls its preInsert method to
   * generate or prepare the ID value before the INSERT operation.
   */
  protected void prepareIdValue() {
    if (generatedIdPropertyType != null && idGenerationConfig != null) {
      entity = generatedIdPropertyType.preInsert(entityType, entity, idGenerationConfig);
    }
  }

  /**
   * Prepares the version value for the entity before insertion.
   *
   * <p>If the entity has a version property, this method initializes it to 1 for optimistic
   * locking.
   */
  protected void prepareVersionValue() {
    if (versionPropertyType != null) {
      entity = versionPropertyType.setIfNecessary(entityType, entity, 1);
    }
  }

  /**
   * Prepares the SQL statement for this query.
   *
   * <p>This method builds either a standard INSERT statement or an UPSERT statement based on the
   * duplicate key handling strategy. It uses the dialect-specific SQL assemblers to generate the
   * appropriate SQL syntax.
   *
   * <p>If the duplicate key type is EXCEPTION, a standard INSERT statement is generated. Otherwise,
   * an UPSERT statement is generated, unless the dialect supports MERGE statements and an identity
   * key is included in the duplicate keys, in which case it falls back to a standard INSERT.
   */
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

  /**
   * Assembles a standard INSERT SQL statement.
   *
   * <p>This method creates an INSERT assembler context and uses the dialect-specific INSERT
   * assembler to generate the SQL statement.
   *
   * @param builder the SQL builder
   * @param naming the naming convention
   * @param dialect the database dialect
   */
  private void assembleInsertSql(PreparedSqlBuilder builder, Naming naming, Dialect dialect) {
    InsertAssemblerContext<ENTITY> context =
        InsertAssemblerContextBuilder.build(
            builder, entityType, naming, dialect, targetPropertyTypes, entity, returning);
    InsertAssembler insertAssembler = dialect.getInsertAssembler(context);
    insertAssembler.assemble();
  }

  /**
   * Assembles an UPSERT SQL statement.
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
        UpsertAssemblerContextBuilder.buildFromEntity(
            builder,
            entityType,
            duplicateKeyType,
            duplicateKeys,
            naming,
            dialect,
            idPropertyTypes,
            targetPropertyTypes,
            entity,
            returning);
    UpsertAssembler upsertAssembler = dialect.getUpsertAssembler(context);
    upsertAssembler.assemble();
  }

  /**
   * Generates an ID for the inserted entity.
   *
   * <p>This method is called after executing the INSERT statement to retrieve and set
   * auto-generated keys for the entity. It's only executed if auto-generated keys are supported for
   * this query.
   *
   * @param statement the statement used for the INSERT operation
   */
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
   * Executes post-insert entity processing.
   *
   * <p>This method creates a post-insert context and calls the entity's postInsert method, allowing
   * entity listeners to modify the entity after insertion.
   */
  protected void postInsert() {
    AutoPostInsertContext<ENTITY> context =
        new AutoPostInsertContext<>(entityType, method, config, duplicateKeyType, returning);
    entityType.postInsert(entity, context);
    if (context.getNewEntity() != null) {
      entity = context.getNewEntity();
    }
  }

  /**
   * Sets whether null properties should be excluded from the INSERT statement.
   *
   * @param nullExcluded true to exclude null properties, false otherwise
   */
  public void setNullExcluded(boolean nullExcluded) {
    this.nullExcluded = nullExcluded;
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
}
