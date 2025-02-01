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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.internal.jdbc.command.AbstractIterationHandler;
import org.seasar.doma.internal.jdbc.command.ResultListCallback;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.SelectQuery;

public class LinkableEntityPoolIterationHandler
    extends AbstractIterationHandler<LinkableEntityPool, List<LinkableEntityPool>> {

  private final EntityType<?> entityType;
  private final AggregateStrategyType aggregateStrategyType;
  private final boolean resultMappingEnsured;
  private final Map<LinkableEntityKey, Object> cache;

  public LinkableEntityPoolIterationHandler(
      EntityType<?> entityType,
      AggregateStrategyType aggregateStrategyType,
      boolean resultMappingEnsured,
      Map<LinkableEntityKey, Object> cache) {
    super(new ResultListCallback<>());
    this.entityType = Objects.requireNonNull(entityType);
    this.aggregateStrategyType = Objects.requireNonNull(aggregateStrategyType);
    this.resultMappingEnsured = resultMappingEnsured;
    this.cache = Objects.requireNonNull(cache);
  }

  @Override
  protected ObjectProvider<LinkableEntityPool> createObjectProvider(SelectQuery query) {
    return new LinkableEntityPoolProvider(
        entityType, aggregateStrategyType, query, resultMappingEnsured, cache);
  }
}
