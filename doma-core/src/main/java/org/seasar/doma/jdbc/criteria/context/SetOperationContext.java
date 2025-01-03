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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.internal.util.Pair;

public interface SetOperationContext<ELEMENT> {

  <R> R accept(Visitor<R> visitor);

  class Select<ELEMENT> implements SetOperationContext<ELEMENT> {
    public final SelectContext context;

    public Select(SelectContext context) {
      this.context = Objects.requireNonNull(context);
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visit(this);
    }
  }

  class Union<ELEMENT> implements SetOperationContext<ELEMENT> {
    public final SetOperationContext<ELEMENT> left;
    public final SetOperationContext<ELEMENT> right;
    public final List<Pair<OrderByItem.Index, String>> orderBy = new ArrayList<>();

    public Union(SetOperationContext<ELEMENT> left, SetOperationContext<ELEMENT> right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visit(this);
    }
  }

  class UnionAll<ELEMENT> implements SetOperationContext<ELEMENT> {
    public final SetOperationContext<ELEMENT> left;
    public final SetOperationContext<ELEMENT> right;
    public final List<Pair<OrderByItem.Index, String>> orderBy = new ArrayList<>();

    public UnionAll(SetOperationContext<ELEMENT> left, SetOperationContext<ELEMENT> right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visit(this);
    }
  }

  interface Visitor<R> {
    R visit(Select<?> select);

    R visit(Union<?> union);

    R visit(UnionAll<?> unionAll);
  }
}
