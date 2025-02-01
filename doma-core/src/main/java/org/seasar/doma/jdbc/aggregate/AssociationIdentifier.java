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
package org.seasar.doma.jdbc.aggregate;

import java.util.Objects;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * Represents an identifier for an association in a data model. This identifier consists of a
 * property path and an entity type.
 *
 * @param propertyPath the path to the property, must not be {@code null}
 * @param entityType the entity type associated with the property, must not be {@code null}
 */
public record AssociationIdentifier(String propertyPath, EntityType<?> entityType) {
  public AssociationIdentifier {
    Objects.requireNonNull(propertyPath);
    Objects.requireNonNull(entityType);
  }
}
