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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.SelectQuery;

public class EntityIterationHandler<ENTITY, RESULT>
    extends AbstractIterationHandler<ENTITY, RESULT> {

  protected final EntityType<ENTITY> entityType;

  public EntityIterationHandler(
      EntityType<ENTITY> entityType, IterationCallback<ENTITY, RESULT> iterationCallback) {
    super(iterationCallback);
    assertNotNull(entityType);
    this.entityType = entityType;
  }

  @Override
  protected ObjectProvider<ENTITY> createObjectProvider(SelectQuery query) {
    return new EntityProvider<>(entityType, query, query.isResultMappingEnsured());
  }
}
