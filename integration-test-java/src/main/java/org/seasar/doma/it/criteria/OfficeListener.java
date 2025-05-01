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
package org.seasar.doma.it.criteria;

import java.util.stream.Collectors;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import org.seasar.doma.jdbc.query.ReturningProperties;

public class OfficeListener implements EntityListener<Office> {

  public static final StringBuffer buffer = new StringBuffer();

  @Override
  public void preDelete(Office office, PreDeleteContext<Office> context) {
    log("preDelete", context.getReturningProperties(), context.getEntityType());
  }

  @Override
  public void postDelete(Office office, PostDeleteContext<Office> context) {
    log("postDelete", context.getReturningProperties(), context.getEntityType());
  }

  @Override
  public void preInsert(Office office, PreInsertContext<Office> context) {
    log("preInsert", context.getReturningProperties(), context.getEntityType());
  }

  @Override
  public void postInsert(Office office, PostInsertContext<Office> context) {
    log("postInsert", context.getReturningProperties(), context.getEntityType());
  }

  @Override
  public void preUpdate(Office office, PreUpdateContext<Office> context) {
    log("preUpdate", context.getReturningProperties(), context.getEntityType());
  }

  @Override
  public void postUpdate(Office office, PostUpdateContext<Office> context) {
    log("postUpdate", context.getReturningProperties(), context.getEntityType());
  }

  private void log(
      String methodName, ReturningProperties returningProperties, EntityType<?> entityType) {
    var text =
        returningProperties.resolve(entityType).stream()
            .map(EntityPropertyType::getName)
            .collect(Collectors.joining(","));
    buffer.append(methodName).append(":").append(text).append(". ");
  }
}
