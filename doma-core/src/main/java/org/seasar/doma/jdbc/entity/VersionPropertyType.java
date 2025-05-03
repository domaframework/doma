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

import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.wrapper.NumberWrapper;
import org.seasar.doma.wrapper.NumberWrapperVisitor;

/**
 * Represents a property in an entity class that is used for optimistic locking.
 *
 * <p>A version property is annotated with {@link org.seasar.doma.Version} and contains a numeric
 * value that is automatically incremented during update operations. This property is used to detect
 * concurrent modifications to the same entity.
 *
 * <p>Implementations of this class are typically generated at compile time by the Doma annotation
 * processor.
 *
 * @param <ENTITY> the entity type containing this version property
 * @param <BASIC> the basic type of the version property (must be a numeric type)
 * @param <CONTAINER> the container type of the version property
 * @see org.seasar.doma.Version
 * @see org.seasar.doma.jdbc.entity.EntityType
 */
public class VersionPropertyType<ENTITY, BASIC extends Number, CONTAINER>
    extends DefaultPropertyType<ENTITY, BASIC, CONTAINER> {

  /**
   * Constructs a new version property type.
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
   */
  public VersionPropertyType(
      Class<ENTITY> entityClass,
      Supplier<Scalar<BASIC, CONTAINER>> scalarSupplier,
      String name,
      String columnName,
      NamingType namingType,
      boolean quoteRequired) {
    super(entityClass, scalarSupplier, name, columnName, namingType, true, true, quoteRequired);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation always returns {@code true} since this property type represents a
   * version property.
   *
   * @return {@code true}
   */
  @Override
  public boolean isVersion() {
    return true;
  }

  /**
   * Sets the version value if necessary.
   *
   * <p>This method sets the version value only if the current value is null or negative. This is
   * typically used when initializing a new entity or when loading an entity from the database.
   *
   * @param entityType the entity type metadata
   * @param entity the entity instance to modify
   * @param value the version value to set
   * @return the modified entity instance (may be the same instance if the entity is mutable)
   */
  public ENTITY setIfNecessary(EntityType<ENTITY> entityType, ENTITY entity, Number value) {
    return modifyIfNecessary(entityType, entity, new ValueSetter(), value);
  }

  /**
   * Increments the version value in the entity.
   *
   * <p>This method is called during update operations to increment the version number, which is
   * essential for optimistic locking. The incremented version value is used in the WHERE clause of
   * the update statement to ensure that the record has not been modified since it was read.
   *
   * @param entityType the entity type metadata
   * @param entity the entity instance to modify
   * @return the modified entity instance (may be the same instance if the entity is mutable)
   */
  public ENTITY increment(EntityType<ENTITY> entityType, ENTITY entity) {
    return modifyIfNecessary(entityType, entity, new Incrementer(), null);
  }

  /**
   * A visitor implementation that sets a version value in a number wrapper.
   *
   * <p>This class is used by the {@link #setIfNecessary} method to set the version value only if
   * the current value is null or negative.
   */
  protected static class ValueSetter
      implements NumberWrapperVisitor<Boolean, Number, Void, RuntimeException> {

    @Override
    public <V extends Number> Boolean visitNumberWrapper(
        NumberWrapper<V> wrapper, Number value, Void q) {
      Number currentValue = wrapper.get();
      if (currentValue == null || currentValue.intValue() < 0) {
        wrapper.set(value);
        return true;
      }
      return false;
    }
  }

  /**
   * A visitor implementation that increments a version value in a number wrapper.
   *
   * <p>This class is used by the {@link #increment} method to increment the version value during
   * update operations, which is essential for optimistic locking.
   */
  protected static class Incrementer
      implements NumberWrapperVisitor<Boolean, Void, Void, RuntimeException> {

    @Override
    public <V extends Number> Boolean visitNumberWrapper(NumberWrapper<V> wrapper, Void p, Void q) {
      wrapper.increment();
      return true;
    }
  }
}
