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
package org.seasar.doma.jdbc.criteria.metamodel;

import java.util.Objects;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;

public class DefaultPropertyMetamodel<PROPERTY> implements PropertyMetamodel<PROPERTY> {

  private final Class<?> clazz;
  private final EntityType<?> entityType;
  private final String name;

  public DefaultPropertyMetamodel(Class<?> clazz, EntityType<?> entityType, String name) {
    this.clazz = Objects.requireNonNull(clazz);
    this.entityType = Objects.requireNonNull(entityType);
    this.name = Objects.requireNonNull(name);
  }

  @Override
  public Class<?> asClass() {
    return clazz;
  }

  @Override
  public EntityPropertyType<?, ?> asType() {
    return entityType.getEntityPropertyType(name);
  }

  @Override
  public String getName() {
    return name;
  }

  public void accept(Visitor visitor) {
    Objects.requireNonNull(visitor);
    visitor.visit(this);
  }
}
