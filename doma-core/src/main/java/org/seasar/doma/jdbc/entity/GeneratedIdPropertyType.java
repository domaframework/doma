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
package org.seasar.doma.jdbc.entity;

import java.sql.Statement;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.GenerationType;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.internal.util.Zip;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.id.IdGenerationConfig;
import org.seasar.doma.jdbc.id.IdGenerator;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.NumberWrapper;
import org.seasar.doma.wrapper.NumberWrapperVisitor;

/**
 * A property type for entity ID fields whose values are automatically generated.
 *
 * <p>This class handles ID generation strategies such as database identity columns, sequences, and
 * table-based ID generation. It provides methods for generating IDs both before and after database
 * insert operations.
 *
 * <p>This property type is used for fields annotated with both {@link org.seasar.doma.Id} and
 * {@link org.seasar.doma.GeneratedValue} in entity classes.
 *
 * @param <ENTITY> the entity type that contains this property
 * @param <BASIC> the numeric type of the ID property (must extend Number)
 * @param <CONTAINER> the container type that holds the property value (e.g., Optional)
 * @see org.seasar.doma.Id
 * @see org.seasar.doma.GeneratedValue
 * @see org.seasar.doma.GenerationType
 */
public class GeneratedIdPropertyType<ENTITY, BASIC extends Number, CONTAINER>
    extends DefaultPropertyType<ENTITY, BASIC, CONTAINER> {

  protected final IdGenerator idGenerator;

  /**
   * Constructs a new generated ID property type.
   *
   * <p>This constructor is typically called by the Doma annotation processor when generating
   * implementations of {@link EntityType}.
   *
   * @param entityClass the entity class
   * @param scalarSupplier the supplier of scalar that represents the property value
   * @param name the property name
   * @param columnName the column name
   * @param namingType the naming convention
   * @param quoteRequired whether the column name requires quoting in SQL
   * @param idGenerator the ID generator for this property
   * @throws DomaNullPointerException if {@code idGenerator} is {@code null}
   */
  public GeneratedIdPropertyType(
      Class<ENTITY> entityClass,
      Supplier<Scalar<BASIC, CONTAINER>> scalarSupplier,
      String name,
      String columnName,
      NamingType namingType,
      boolean quoteRequired,
      IdGenerator idGenerator) {
    super(entityClass, scalarSupplier, name, columnName, namingType, true, true, quoteRequired);
    if (idGenerator == null) {
      throw new DomaNullPointerException("idGenerator");
    }
    this.idGenerator = idGenerator;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation always returns {@code true} since this property type represents an ID
   * property with a generated value.
   *
   * @return {@code true}
   */
  @Override
  public boolean isId() {
    return true;
  }

  /**
   * Validates that the ID generation strategy is supported by the current database dialect.
   *
   * <p>This method checks if the configured generation strategy (IDENTITY, SEQUENCE, etc.) is
   * compatible with the database dialect being used. If not, a JdbcException is thrown.
   *
   * @param config the ID generation configuration
   * @throws JdbcException if the generation strategy is not supported by the dialect
   */
  public void validateGenerationStrategy(IdGenerationConfig config) {
    Dialect dialect = config.getDialect();
    GenerationType generationType = idGenerator.getGenerationType();
    if (!isGenerationTypeSupported(generationType, dialect)) {
      EntityType<?> entityType = config.getEntityType();
      throw new JdbcException(
          Message.DOMA2021, entityType.getName(), name, generationType.name(), dialect.getName());
    }
  }

  /**
   * Determines if the specified generation type is supported by the given dialect.
   *
   * <p>Different databases support different ID generation strategies. For example, not all
   * databases support identity columns or sequences.
   *
   * @param generationType the ID generation type to check
   * @param dialect the database dialect
   * @return true if the generation type is supported, false otherwise
   */
  protected boolean isGenerationTypeSupported(GenerationType generationType, Dialect dialect) {
    switch (generationType) {
      case IDENTITY:
        return dialect.supportsIdentity();
      case SEQUENCE:
        return dialect.supportsSequence();
      default:
        return true;
    }
  }

  @Deprecated
  public boolean isIncluded(IdGenerationConfig config) {
    return idGenerator.includesIdentityColumn(config);
  }

  public boolean isIncluded(IdGenerationConfig config, Object idValue) {
    return idGenerator.includesIdentityColumn(config, idValue);
  }

  public boolean isBatchSupported(IdGenerationConfig config) {
    return idGenerator.supportsBatch(config);
  }

  public boolean isAutoGeneratedKeysSupported(IdGenerationConfig config) {
    return idGenerator.supportsAutoGeneratedKeys(config);
  }

  public GenerationType getGenerationType() {
    return idGenerator.getGenerationType();
  }

  /**
   * Generates and sets an ID value for an entity before it is inserted into the database.
   *
   * <p>This method is used for ID generation strategies that generate values before the insert
   * operation, such as SEQUENCE and TABLE.
   *
   * @param entityType the entity type
   * @param entity the entity instance
   * @param config the ID generation configuration
   * @return the entity with the generated ID set (if necessary)
   */
  public ENTITY preInsert(EntityType<ENTITY> entityType, ENTITY entity, IdGenerationConfig config) {
    return setIfNecessary(entityType, entity, () -> idGenerator.generatePreInsert(config));
  }

  /**
   * Generates and sets ID values for multiple entities before they are inserted into the database.
   *
   * <p>This method is used for batch operations with ID generation strategies that generate values
   * before the insert operation, such as SEQUENCE and TABLE.
   *
   * @param entityType the entity type
   * @param entities the list of entity instances
   * @param config the ID generation configuration
   * @return the list of entities with generated IDs set (if necessary)
   */
  public List<ENTITY> preInsert(
      EntityType<ENTITY> entityType, List<ENTITY> entities, IdGenerationConfig config) {
    List<Long> values = idGenerator.generateValuesPreInsert(config, entities.size());
    return Zip.stream(entities, values)
        .map(
            pair -> {
              ENTITY entity = pair.fst;
              Long value = pair.snd;
              return setIfNecessary(entityType, entity, () -> value);
            })
        .collect(Collectors.toList());
  }

  /**
   * Retrieves and sets an ID value for an entity after it has been inserted into the database.
   *
   * <p>This method is used for ID generation strategies that generate values during the insert
   * operation, such as IDENTITY.
   *
   * @param entityType the entity type
   * @param entity the entity instance
   * @param config the ID generation configuration
   * @param statement the JDBC statement used for the insert operation
   * @return the entity with the generated ID set (if necessary)
   */
  public ENTITY postInsert(
      EntityType<ENTITY> entityType,
      ENTITY entity,
      IdGenerationConfig config,
      Statement statement) {
    return setIfNecessary(
        entityType, entity, () -> idGenerator.generatePostInsert(config, statement));
  }

  /**
   * Retrieves and sets ID values for multiple entities after they have been inserted into the
   * database.
   *
   * <p>This method is used for batch operations with ID generation strategies that generate values
   * during the insert operation, such as IDENTITY.
   *
   * @param entityType the entity type
   * @param entities the list of entity instances
   * @param config the ID generation configuration
   * @param statement the JDBC statement used for the insert operation
   * @return the list of entities with generated IDs set (if necessary)
   */
  public List<ENTITY> postInsert(
      EntityType<ENTITY> entityType,
      List<ENTITY> entities,
      IdGenerationConfig config,
      Statement statement) {
    List<Long> values = idGenerator.generateValuesPostInsert(config, statement);
    return Zip.stream(entities, values)
        .map(
            pair -> {
              ENTITY entity = pair.fst;
              Long value = pair.snd;
              return setIfNecessary(entityType, entity, () -> value);
            })
        .collect(Collectors.toList());
  }

  /**
   * Sets the ID value if necessary.
   *
   * <p>This method sets the ID value only if the current value is null or negative. It is used
   * internally by the preInsert and postInsert methods.
   *
   * @param entityType the entity type metadata
   * @param entity the entity instance to modify
   * @param supplier the supplier of the ID value to set
   * @return the modified entity instance (may be the same instance if the entity is mutable)
   */
  protected ENTITY setIfNecessary(
      EntityType<ENTITY> entityType, ENTITY entity, Supplier<Long> supplier) {
    return modifyIfNecessary(entityType, entity, new ValueSetter(), supplier);
  }

  /**
   * A visitor implementation that sets an ID value in a number wrapper.
   *
   * <p>This class is used by the {@link #setIfNecessary} method to set the ID value only if the
   * current value is null or negative.
   */
  protected static class ValueSetter
      implements NumberWrapperVisitor<Boolean, Supplier<Long>, Void, RuntimeException> {

    @Override
    public <V extends Number> Boolean visitNumberWrapper(
        NumberWrapper<V> wrapper, Supplier<Long> valueSupplier, Void q) throws RuntimeException {
      Number currentValue = wrapper.get();
      if (currentValue == null || currentValue.intValue() < 0) {
        Long value = valueSupplier.get();
        if (value != null) {
          wrapper.set(value);
          return true;
        }
      }
      return false;
    }
  }
}
