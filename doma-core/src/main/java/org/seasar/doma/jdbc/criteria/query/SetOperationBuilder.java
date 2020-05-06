package org.seasar.doma.jdbc.criteria.query;

import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;

public class SetOperationBuilder {

  private final Config config;
  private final SetOperationContext<?> context;
  private final Function<String, String> commenter;
  private final PreparedSqlBuilder buf;

  public SetOperationBuilder(
      Config config,
      SetOperationContext<?> context,
      Function<String, String> commenter,
      SqlLogType sqlLogType) {
    Objects.requireNonNull(config);
    Objects.requireNonNull(context);
    Objects.requireNonNull(commenter);
    Objects.requireNonNull(sqlLogType);
    this.config = config;
    this.context = context;
    this.commenter = commenter;
    this.buf = new PreparedSqlBuilder(config, SqlKind.SELECT, sqlLogType);
  }

  public PreparedSql build() {
    context.accept(
        new SetOperationContext.Visitor<Void>() {
          @Override
          public Void visit(SetOperationContext.Select<?> select) {
            SelectContext context = select.context;
            SelectBuilder builder =
                new SelectBuilder(config, context, commenter, buf, new AliasManager(context));
            builder.interpret();
            return null;
          }

          @Override
          public Void visit(SetOperationContext.Union<?> union) {
            union.left.accept(this);
            buf.appendSql(" union ");
            union.right.accept(this);
            return null;
          }

          @Override
          public Void visit(SetOperationContext.UnionAll<?> unionAll) {
            unionAll.left.accept(this);
            buf.appendSql(" union all ");
            unionAll.right.accept(this);
            return null;
          }
        });
    return buf.build(commenter);
  }
}
