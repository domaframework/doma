package org.seasar.doma.jdbc.criteria.query;

import java.util.List;
import java.util.Objects;
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
    this.config = Objects.requireNonNull(config);
    this.context = Objects.requireNonNull(context);
    this.commenter = Objects.requireNonNull(commenter);
    Objects.requireNonNull(sqlLogType);
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
            String open;
            String close;
            if (orderBy.isEmpty()) {
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
