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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.seasar.doma.internal.jdbc.command.AbstractIterationHandler;
import org.seasar.doma.internal.jdbc.command.ResultListCallback;
import org.seasar.doma.jdbc.EntityId;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.query.SelectQuery;

public class EntityPoolIterationHandler
    extends AbstractIterationHandler<EntityPool, List<EntityPool>> {
  private final EntityMetamodel<?> rootEntityMetamodel;
  private final Set<EntityId> rootEntityIds;
  private final Map<EntityMetamodel<?>, List<PropertyMetamodel<?>>> projectionEntityMetamodels;

  public EntityPoolIterationHandler(
      EntityMetamodel<?> rootEntityMetamodel,
      Set<EntityId> rootEntityIds,
      Map<EntityMetamodel<?>, List<PropertyMetamodel<?>>> projectionEntityMetamodels) {
    super(new ResultListCallback<>());
    this.rootEntityMetamodel = Objects.requireNonNull(rootEntityMetamodel);
    this.rootEntityIds = Objects.requireNonNull(rootEntityIds);
    this.projectionEntityMetamodels = Objects.requireNonNull(projectionEntityMetamodels);
  }

  @Override
  protected ObjectProvider<EntityPool> createObjectProvider(SelectQuery query) {
    return new EntityPoolProvider(
        rootEntityMetamodel, rootEntityIds, projectionEntityMetamodels, query);
  }
}
