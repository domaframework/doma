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

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.query.SelectQuery;

public abstract class AbstractSetOperand<STMT extends AbstractSetOperand<STMT, ELEMENT>, ELEMENT>
    extends AbstractStatement<STMT, List<ELEMENT>> implements SetOperand<ELEMENT> {

  protected final Function<SelectQuery, ObjectProvider<ELEMENT>> objectProviderFactory;

  protected AbstractSetOperand(
      Config config, Function<SelectQuery, ObjectProvider<ELEMENT>> objectProviderFactory) {
    super(Objects.requireNonNull(config));
    this.objectProviderFactory = Objects.requireNonNull(objectProviderFactory);
  }

  @Override
  public SetOperator<ELEMENT> union(SetOperand<ELEMENT> other) {
    Objects.requireNonNull(other);
    SetOperationContext<ELEMENT> newContext =
        new SetOperationContext.Union<>(getContext(), other.getContext());
    return new NativeSqlSetStarting<>(config, newContext, objectProviderFactory);
  }

  @Override
  public SetOperator<ELEMENT> unionAll(SetOperand<ELEMENT> other) {
    Objects.requireNonNull(other);
    SetOperationContext<ELEMENT> newContext =
        new SetOperationContext.UnionAll<>(getContext(), other.getContext());
    return new NativeSqlSetStarting<>(config, newContext, objectProviderFactory);
  }
}
