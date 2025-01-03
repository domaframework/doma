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
package org.seasar.doma.jdbc.criteria.context;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public interface Projection {

  <R> R accept(Visitor<R> visitor);

  class EntityMetamodels implements Projection {
    public final Map<EntityMetamodel<?>, java.util.List<PropertyMetamodel<?>>> map =
        new LinkedHashMap<>();

    public EntityMetamodels(EntityMetamodel<?> entityMetamodel) {
      Objects.requireNonNull(entityMetamodel);
      map.put(entityMetamodel, entityMetamodel.allPropertyMetamodels());
    }

    public EntityMetamodels(
        EntityMetamodel<?> entityMetamodel,
        java.util.List<PropertyMetamodel<?>> propertyMetamodels) {
      Objects.requireNonNull(entityMetamodel);
      Objects.requireNonNull(propertyMetamodels);
      map.put(entityMetamodel, Collections.unmodifiableList(propertyMetamodels));
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visit(this);
    }
  }

  class PropertyMetamodels implements Projection {
    public final java.util.List<PropertyMetamodel<?>> propertyMetamodels;

    public PropertyMetamodels(PropertyMetamodel<?>... propertyMetamodels) {
      this(Arrays.asList(propertyMetamodels));
    }

    public PropertyMetamodels(java.util.List<PropertyMetamodel<?>> propertyMetamodels) {
      Objects.requireNonNull(propertyMetamodels);
      this.propertyMetamodels = Collections.unmodifiableList(propertyMetamodels);
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visit(this);
    }
  }

  interface Visitor<R> {
    R visit(EntityMetamodels entityMetamodels);

    R visit(PropertyMetamodels propertyMetamodels);
  }
}
