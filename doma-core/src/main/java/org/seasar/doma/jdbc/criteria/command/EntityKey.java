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
package org.seasar.doma.jdbc.criteria.command;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

public final class EntityKey {
  private final EntityMetamodel<?> entityMetamodel;
  private final List<?> items;

  public EntityKey(EntityMetamodel<?> entityMetamodel, List<?> items) {
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(items);
    this.items = Collections.unmodifiableList(items);
  }

  public EntityMetamodel<?> getEntityMetamodel() {
    return entityMetamodel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EntityKey)) return false;
    EntityKey entityKey = (EntityKey) o;
    return entityMetamodel.equals(entityKey.entityMetamodel) && items.equals(entityKey.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entityMetamodel, items);
  }
}
