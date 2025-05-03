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

/**
 * Represents a property that defines an association between entity classes.
 *
 * <p>This interface is used to define relationships between entities, such as one-to-one,
 * one-to-many, or many-to-many associations. Association properties are typically created for
 * fields annotated with {@link org.seasar.doma.Association} in entity classes.
 *
 * <p>Implementations of this interface are typically generated at compile time by the Doma
 * annotation processor.
 *
 * @see org.seasar.doma.Association
 * @see org.seasar.doma.jdbc.entity.EntityType
 */
public interface AssociationPropertyType {

  /**
   * Returns the name of this association property.
   *
   * <p>The name typically corresponds to the field name in the entity class that is annotated with
   * {@link org.seasar.doma.Association}.
   *
   * @return the name of this association property
   */
  String getName();
}
