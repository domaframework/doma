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
package org.seasar.doma.jdbc.criteria.statement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.DomaException;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.ReturningProperties;
import org.seasar.doma.message.Message;

record SpecificMetamodels(
    EntityMetamodel<?> entityMetamodel, List<PropertyMetamodel<?>> propertyMetamodels)
    implements ReturningProperties {

  @Override
  public List<? extends EntityPropertyType<?, ?>> resolve(EntityType<?> entityType) {
    Objects.requireNonNull(entityType);
    if (!entityMetamodel.asType().equals(entityType)) {
      throw new DomaException(
          Message.DOMA6013,
          entityMetamodel.asType().getEntityClass().getName(),
          entityType.getEntityClass().getName());
    }
    return propertyMetamodels.stream().map(PropertyMetamodel::asType).toList();
  }

  static ReturningProperties of(
      EntityMetamodel<?> entityMetamodel, PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(entityMetamodel);
    if (propertyMetamodels.length == 0) {
      return ReturningProperties.ALL;
    }
    var allProperties = new HashSet<>(entityMetamodel.allPropertyMetamodels());
    for (int i = 0; i < propertyMetamodels.length; i++) {
      var p = propertyMetamodels[i];
      if (!allProperties.contains(p)) {
        throw new DomaException(
            Message.DOMA6012, p.getName(), i, entityMetamodel.asType().getEntityClass().getName());
      }
    }
    return new SpecificMetamodels(entityMetamodel, Arrays.stream(propertyMetamodels).toList());
  }
}
