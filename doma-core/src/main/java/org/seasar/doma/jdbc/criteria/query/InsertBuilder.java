package org.seasar.doma.jdbc.criteria.query;

import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

public class InsertBuilder {
  private final InsertContext context;
  private final Function<String, String> commenter;
  private final PreparedSqlBuilder buf;
  private final BuilderSupport support;

  public InsertBuilder(
      Config config,
      InsertContext context,
      Function<String, String> commenter,
      SqlLogType sqlLogType) {
    this(config, context, commenter, new PreparedSqlBuilder(config, SqlKind.INSERT, sqlLogType));
  }

  public InsertBuilder(
      Config config,
      InsertContext context,
      Function<String, String> commenter,
      PreparedSqlBuilder buf) {
    this.context = Objects.requireNonNull(context);
    this.commenter = Objects.requireNonNull(commenter);
    this.buf = Objects.requireNonNull(buf);
    support = new BuilderSupport(config, commenter, buf, null);
  }

  public PreparedSql build() {
    buf.appendSql("insert into ");
    table(context.entityMetamodel);
    if (!context.values.isEmpty()) {
      buf.appendSql(" (");
      context
          .values
          .keySet()
          .forEach(
              key -> {
                column(key);
                buf.appendSql(", ");
              });
      buf.cutBackSql(2);
      buf.appendSql(") values (");
      context
          .values
          .values()
          .forEach(
              value -> {
                param(value);
                buf.appendSql(", ");
              });
      buf.cutBackSql(2);
      buf.appendSql(")");
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
}
