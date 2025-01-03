package org.seasar.doma.jdbc.criteria.query;

import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.context.Context;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.DeleteContext;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.dialect.Dialect;

public class DeleteBuilder {
  private final Config config;
  private final DeleteContext context;
  private final Function<String, String> commenter;
  private final PreparedSqlBuilder buf;
  private final BuilderSupport support;

  public DeleteBuilder(
      Config config,
      DeleteContext context,
      Function<String, String> commenter,
      SqlLogType sqlLogType) {
    this(
        config,
        context,
        commenter,
        new PreparedSqlBuilder(config, SqlKind.DELETE, sqlLogType),
        createAliasManager(config, context));
  }

  public DeleteBuilder(
      Config config,
      DeleteContext context,
      Function<String, String> commenter,
      PreparedSqlBuilder buf,
      AliasManager aliasManager) {
    Objects.requireNonNull(config);
    this.config = Objects.requireNonNull(config);
    this.context = Objects.requireNonNull(context);
    this.commenter = Objects.requireNonNull(commenter);
    this.buf = Objects.requireNonNull(buf);
    Objects.requireNonNull(aliasManager);
    support = new BuilderSupport(config, commenter, buf, aliasManager);
  }

  private static AliasManager createAliasManager(Config config, Context context) {
    Objects.requireNonNull(config);
    Objects.requireNonNull(context);
    Dialect dialect = config.getDialect();
    if (dialect.supportsAliasInDeleteClause() || dialect.supportsAliasInDeleteStatement()) {
      return AliasManager.create(config, context);
    } else {
      return AliasManager.createEmpty(config, context);
    }
  }

  public PreparedSql build() {
    buf.appendSql("delete");
    if (config.getDialect().supportsAliasInDeleteClause()) {
      buf.appendSql(" ");
      alias(context.entityMetamodel);
    }
    buf.appendSql(" from ");
    tableOnly(context.entityMetamodel);
    if (config.getDialect().supportsAliasInDeleteStatement()) {
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

  private void alias(EntityMetamodel<?> entityMetamodel) {
    support.alias(entityMetamodel);
  }

  private void tableOnly(EntityMetamodel<?> entityMetamodel) {
    support.tableOnly(entityMetamodel);
  }

  private void visitCriterion(int index, Criterion criterion) {
    support.visitCriterion(index, criterion);
  }
}
