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
package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.criteria.context.OrderByItem;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;

public class OrderByIndexDeclaration {

  private final SetOperationContext<?> context;

  public OrderByIndexDeclaration(SetOperationContext<?> context) {
    this.context = Objects.requireNonNull(context);
  }

  public void asc(int index) {
    add(new Pair<>(new OrderByItem.Index(index), "asc"));
  }

  public void desc(int index) {
    add(new Pair<>(new OrderByItem.Index(index), "desc"));
  }

  private void add(Pair<OrderByItem.Index, String> pair) {
    context.accept(
        new SetOperationContext.Visitor<Void>() {
          @Override
          public Void visit(SetOperationContext.Select<?> select) {
            return null;
          }

          @Override
          public Void visit(SetOperationContext.Union<?> union) {
            union.orderBy.add(pair);
            return null;
          }

          @Override
          public Void visit(SetOperationContext.UnionAll<?> unionAll) {
            unionAll.orderBy.add(pair);
            return null;
          }
        });
  }
}
