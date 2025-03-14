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
package org.seasar.doma.jdbc.criteria.query;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.context.OrderByItem;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.dialect.Dialect;

public class SetOperationBuilder {

  private final Config config;
  private final SetOperationContext<?> context;
  private final Function<String, String> commenter;
  private final PreparedSqlBuilder buf;
  private final Optional<AliasManager> parentAliasManager;

  public SetOperationBuilder(
      Config config,
      SetOperationContext<?> context,
      Function<String, String> commenter,
      SqlLogType sqlLogType) {
    this.config = Objects.requireNonNull(config);
    this.context = Objects.requireNonNull(context);
    this.commenter = Objects.requireNonNull(commenter);
    this.parentAliasManager = Optional.empty();
    Objects.requireNonNull(sqlLogType);
    this.buf = new PreparedSqlBuilder(config, SqlKind.SELECT, sqlLogType);
  }

  public SetOperationBuilder(
      Config config,
      SetOperationContext<?> context,
      Function<String, String> commenter,
      PreparedSqlBuilder buf,
      AliasManager parentAliasManager) {
    this.config = Objects.requireNonNull(config);
    this.context = Objects.requireNonNull(context);
    this.commenter = Objects.requireNonNull(commenter);
    this.parentAliasManager = Optional.of(parentAliasManager);
    this.buf = Objects.requireNonNull(buf);
  }

  public PreparedSql build() {
    context.accept(
        new SetOperationContext.Visitor<Void>() {
          @Override
          public Void visit(SetOperationContext.Select<?> select) {
            SelectContext context = select.context;
            AliasManager am =
                parentAliasManager
                    .map(it -> AliasManager.createChild(config, context, it))
                    .orElseGet(() -> AliasManager.create(config, context));
            SelectBuilder builder = new SelectBuilder(config, context, commenter, buf, am);
            builder.interpret();
            return null;
          }

          @Override
          public Void visit(SetOperationContext.Union<?> union) {
            union("union", union.left, union.right, union.orderBy);
            return null;
          }

          @Override
          public Void visit(SetOperationContext.UnionAll<?> unionAll) {
            union("union all", unionAll.left, unionAll.right, unionAll.orderBy);
            return null;
          }

          private void union(
              String op,
              SetOperationContext<?> left,
              SetOperationContext<?> right,
              List<Pair<OrderByItem.Index, String>> orderBy) {
            Dialect dialect = config.getDialect();
            String open;
            String close;
            if (orderBy.isEmpty() || !dialect.supportsParenthesesForSetOperands()) {
              open = "";
              close = "";
            } else {
              open = "(";
              close = ")";
            }
            buf.appendSql(open);
            left.accept(this);
            buf.appendSql(close + " " + op + " " + open);
            right.accept(this);
            buf.appendSql(close);
            orderBy(orderBy);
          }

          private void orderBy(List<Pair<OrderByItem.Index, String>> orderBy) {
            if (!orderBy.isEmpty()) {
              buf.appendSql(" order by ");
              for (Pair<OrderByItem.Index, String> pair : orderBy) {
                OrderByItem.Index index = pair.fst;
                buf.appendSql(String.valueOf(index.value));
                buf.appendSql(" " + pair.snd + ", ");
              }
              buf.cutBackSql(2);
            }
          }
        });
    return buf.build(commenter);
  }
}
