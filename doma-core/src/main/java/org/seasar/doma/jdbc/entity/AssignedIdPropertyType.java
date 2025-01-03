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
 * A description for an identity property whose value is assigned by an application.
 *
 * @param <ENTITY> the entity type
 * @param <BASIC> the property basic type
 * @param <CONTAINER> the property container type
 */
public class AssignedIdPropertyType<ENTITY, BASIC, CONTAINER>
    extends DefaultPropertyType<ENTITY, BASIC, CONTAINER> {

  public AssignedIdPropertyType(
      Class<ENTITY> entityClass,
      Supplier<Scalar<BASIC, CONTAINER>> scalarSupplier,
      String name,
      String columnName,
      NamingType namingType,
      boolean quoteRequired) {
    super(entityClass, scalarSupplier, name, columnName, namingType, true, true, quoteRequired);
  }

  @Override
  public boolean isId() {
    return true;
  }
}
