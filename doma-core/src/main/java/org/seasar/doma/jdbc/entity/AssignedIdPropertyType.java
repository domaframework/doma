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

/**
 * Represents a property in an entity class that is used as a primary key with an application-assigned value.
 *
 * <p>An assigned ID property is annotated with {@link org.seasar.doma.Id} but not with
 * {@link org.seasar.doma.GeneratedValue}, indicating that the application is responsible
 * for assigning the ID value before the entity is inserted into the database.
 *
 * <p>Implementations of this class are typically generated at compile time by the
 * Doma annotation processor.
 *
 * @param <ENTITY> the entity type containing this assigned ID property
 * @param <BASIC> the basic type of the assigned ID property
 * @param <CONTAINER> the container type of the assigned ID property
 * @see org.seasar.doma.Id
 * @see org.seasar.doma.jdbc.entity.EntityType
 */
public class AssignedIdPropertyType<ENTITY, BASIC, CONTAINER>
    extends DefaultPropertyType<ENTITY, BASIC, CONTAINER> {

  /**
   * Constructs a new assigned ID property type.
   *
   * <p>This constructor is typically called by the Doma annotation processor
   * when generating implementations of {@link EntityType}.
   *
   * @param entityClass the entity class
   * @param scalarSupplier the supplier of scalar that represents the property value
   * @param name the property name
   * @param columnName the column name
   * @param namingType the naming convention
   * @param quoteRequired whether the column name requires quoting in SQL
   */
  public AssignedIdPropertyType(
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
   * <p>This implementation always returns {@code true} since this property type
   * represents an ID property with an application-assigned value.
   * 
   * @return {@code true}
   */
  @Override
  public boolean isId() {
    return true;
  }
}
