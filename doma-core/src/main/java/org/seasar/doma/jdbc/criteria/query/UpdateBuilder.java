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
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

public class UpdateBuilder {
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
        new AliasManager(context));
  }

  public UpdateBuilder(
      Config config,
      UpdateContext context,
      Function<String, String> commenter,
      PreparedSqlBuilder buf,
      AliasManager aliasManager) {
    Objects.requireNonNull(config);
    this.context = Objects.requireNonNull(context);
    this.commenter = Objects.requireNonNull(commenter);
    this.buf = Objects.requireNonNull(buf);
    Objects.requireNonNull(aliasManager);
    support = new BuilderSupport(config, commenter, buf, aliasManager);
  }

  public PreparedSql build() {
    buf.appendSql("update ");
    table(context.entityMetamodel);
    if (!context.set.isEmpty()) {
      buf.appendSql(" set ");
      Set<Map.Entry<Operand.Prop, Operand.Param>> entries = context.set.entrySet();
      entries.forEach(
          entry -> {
            column(entry.getKey());
            buf.appendSql(" = ");
            param(entry.getValue());
            buf.appendSql(", ");
          });
      buf.cutBackSql(2);
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

  private void table(EntityMetamodel<?> entityMetamodel) {
    support.table(entityMetamodel);
  }

  private void column(Operand.Prop prop) {
    support.column(prop);
  }

  private void param(Operand.Param param) {
    support.param(param);
  }

  private void visitCriterion(int index, Criterion criterion) {
    support.visitCriterion(index, criterion);
  }
}
