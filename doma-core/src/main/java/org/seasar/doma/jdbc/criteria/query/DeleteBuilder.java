package org.seasar.doma.jdbc.criteria.query;

import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.def.EntityDef;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.DeleteContext;

public class DeleteBuilder {
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
        new AliasManager(context));
  }

  public DeleteBuilder(
      Config config,
      DeleteContext context,
      Function<String, String> commenter,
      PreparedSqlBuilder buf,
      AliasManager aliasManager) {
    Objects.requireNonNull(config);
    Objects.requireNonNull(context);
    Objects.requireNonNull(commenter);
    Objects.requireNonNull(buf);
    Objects.requireNonNull(aliasManager);
    this.context = context;
    this.commenter = commenter;
    this.buf = buf;
    support = new BuilderSupport(config, commenter, buf, aliasManager);
  }

  public PreparedSql build() {
    buf.appendSql("delete from ");
    table(context.entityDef);
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

  private void table(EntityDef<?> entityDef) {
    support.table(entityDef);
  }

  private void visitCriterion(int index, Criterion criterion) {
    support.visitCriterion(index, criterion);
  }
}
