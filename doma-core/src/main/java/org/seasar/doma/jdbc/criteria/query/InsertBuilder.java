package org.seasar.doma.jdbc.criteria.query;

import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;

public class InsertBuilder {
  private final Config config;
  private final InsertContext context;
  private final Function<String, String> commenter;
  private final PreparedSqlBuilder buf;

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
    this.config = Objects.requireNonNull(config);
    this.context = Objects.requireNonNull(context);
    this.commenter = Objects.requireNonNull(commenter);
    this.buf = Objects.requireNonNull(buf);
  }

  public PreparedSql build() {
    buf.appendSql("insert into ");
    table(context.entityMetamodel);
    if (!context.values.isEmpty()) {
      values();
    } else {
      if (context.selectContext != null) {
        select();
      }
    }
    return buf.build(commenter);
  }

  private void values() {
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

  private void select() {
    buf.appendSql(" (");
    context
        .entityMetamodel
        .allPropertyMetamodels()
        .forEach(
            p -> {
              column(p);
              buf.appendSql(", ");
            });
    buf.cutBackSql(2);
    buf.appendSql(") ");
    AliasManager aliasManager = new AliasManager(context.selectContext);
    SelectBuilder builder =
        new SelectBuilder(config, context.selectContext, Function.identity(), buf, aliasManager);
    builder.interpret();
  }

  private void table(EntityMetamodel<?> entityMetamodel) {
    EntityType<?> entityType = entityMetamodel.asType();
    buf.appendSql(
        entityType.getQualifiedTableName(
            config.getNaming()::apply, config.getDialect()::applyQuote));
  }

  private void column(Operand.Prop prop) {
    column(prop.value);
  }

  private void column(PropertyMetamodel<?> propertyMetamodel) {
    EntityPropertyType<?, ?> propertyType = propertyMetamodel.asType();
    buf.appendSql(
        propertyType.getColumnName(config.getNaming()::apply, config.getDialect()::applyQuote));
  }

  private void param(Operand.Param param) {
    InParameter<?> parameter = param.createInParameter(config);
    buf.appendParameter(parameter);
  }
}
