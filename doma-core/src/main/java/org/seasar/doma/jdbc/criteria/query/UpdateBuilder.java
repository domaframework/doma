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

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.context.Context;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.dialect.Dialect;

public class UpdateBuilder {
  private final Config config;
  private final UpdateContext context;
  private final Function<String, String> commenter;
  private final PreparedSqlBuilder buf;
  private final BuilderSupport support;

  public UpdateBuilder(
      Config config,
      UpdateContext context,
      Function<String, String> commenter,
      SqlLogType sqlLogType) {
    this(
        config,
        context,
        commenter,
        new PreparedSqlBuilder(config, SqlKind.UPDATE, sqlLogType),
        createAliasManager(config, context));
  }

  private static AliasManager createAliasManager(Config config, Context context) {
    Objects.requireNonNull(config);
    Objects.requireNonNull(context);
    Dialect dialect = config.getDialect();
    if (dialect.supportsAliasInUpdateClause() || dialect.supportsAliasInUpdateStatement()) {
      return AliasManager.create(config, context);
    } else {
      return AliasManager.createEmpty(config, context);
    }
  }

  public UpdateBuilder(
      Config config,
      UpdateContext context,
      Function<String, String> commenter,
      PreparedSqlBuilder buf,
      AliasManager aliasManager) {
    this.config = Objects.requireNonNull(config);
    this.context = Objects.requireNonNull(context);
    this.commenter = Objects.requireNonNull(commenter);
    this.buf = Objects.requireNonNull(buf);
    Objects.requireNonNull(aliasManager);
    support = new BuilderSupport(config, commenter, buf, aliasManager);
  }

  public PreparedSql build() {
    buf.appendSql("update ");
    if (config.getDialect().supportsAliasInUpdateClause()) {
      alias(context.entityMetamodel);
    } else {
      tableOnly(context.entityMetamodel);
      if (config.getDialect().supportsAliasInUpdateStatement()) {
        buf.appendSql(" ");
        alias(context.entityMetamodel);
      }
    }
    if (!context.set.isEmpty()) {
      buf.appendSql(" set ");
      Set<Map.Entry<Operand.Prop, Operand>> entries = context.set.entrySet();
      entries.forEach(
          entry -> {
            column(entry.getKey());
            buf.appendSql(" = ");
            operand(entry.getValue());
            buf.appendSql(", ");
          });
      buf.cutBackSql(2);
    }
    if (config.getDialect().supportsAliasInUpdateClause()) {
      buf.appendSql(" from ");
      tableOnly(context.entityMetamodel);
      buf.appendSql(" ");
      alias(context.entityMetamodel);
    }
    if (!context.where.isEmpty()) {
      buf.appendSql(" where ");
      int index = 0;
      for (Criterion criterion : context.where) {
        visitCriterion(index++, criterion);
        buf.appendSql(" and ");
      }
      buf.cutBackSql(5);
    }
    return buf.build(commenter);
  }

  private void tableOnly(EntityMetamodel<?> entityMetamodel) {
    support.tableOnly(entityMetamodel);
  }

  private void alias(EntityMetamodel<?> entityMetamodel) {
    support.alias(entityMetamodel);
  }

  private void operand(Operand operand) {
    support.operand(operand);
  }

  private void column(Operand.Prop prop) {
    support.columnWithoutAlias(prop);
  }

  private void visitCriterion(int index, Criterion criterion) {
    support.visitCriterion(index, criterion);
  }
}
