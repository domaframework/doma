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

/**
 * Represents an entity that can be linked to others via associations.
 *
 * <p>This class is used as part of an object graph reduction process, where entities and their
 * associations are aggregated into a coherent structure.
 *
 * <p>The entity is uniquely identified by a {@link LinkableEntityKey}, which encapsulates an {@link
 * AssociationIdentifier} and a collection of items. The {@link LinkableEntityKey} helps in
 * associating and organizing entities.
 *
 * <p>This record guarantees non-nullability of its components, ensuring both the key and the entity
 * are valid references.
 *
 * @param key the unique key representing the entity's association context
 * @param entity the actual entity being linked
 */
public record LinkableEntity(LinkableEntityKey key, Object entity) {
  public LinkableEntity {
    Objects.requireNonNull(key);
    Objects.requireNonNull(entity);
  }
}
