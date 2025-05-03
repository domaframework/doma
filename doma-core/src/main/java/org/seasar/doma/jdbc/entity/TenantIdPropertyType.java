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
 * Represents a property in an entity class that is used for multi-tenancy support.
 *
 * <p>A tenant ID property is annotated with {@link org.seasar.doma.TenantId} and contains a value
 * that identifies the tenant to which the entity belongs. This property is automatically included
 * in WHERE clauses for entity operations to ensure data isolation between tenants.
 *
 * <p>Implementations of this class are typically generated at compile time by the Doma annotation
 * processor.
 *
 * @param <ENTITY> the entity type containing this tenant ID property
 * @param <BASIC> the basic type of the tenant ID property
 * @param <CONTAINER> the container type of the tenant ID property
 * @see org.seasar.doma.TenantId
 * @see org.seasar.doma.jdbc.entity.EntityType
 */
public class TenantIdPropertyType<ENTITY, BASIC, CONTAINER>
    extends DefaultPropertyType<ENTITY, BASIC, CONTAINER> {

  /**
   * Constructs a new tenant ID property type.
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
  public TenantIdPropertyType(
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
   * <p>This implementation always returns {@code true} since this property type represents a tenant
   * ID property.
   *
   * @return {@code true}
   */
  @Override
  public boolean isTenantId() {
    return true;
  }
}
