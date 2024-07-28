package org.seasar.doma.jdbc.dialect;

import java.util.List;
import java.util.function.Consumer;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.DuplicateKeyType;
import org.seasar.doma.jdbc.query.QueryOperand;
import org.seasar.doma.jdbc.query.QueryOperandPair;
import org.seasar.doma.jdbc.query.QueryOperandPairList;
import org.seasar.doma.jdbc.query.QueryRows;
import org.seasar.doma.jdbc.query.UpsertAssembler;
import org.seasar.doma.jdbc.query.UpsertAssemblerContext;
import org.seasar.doma.jdbc.query.UpsertAssemblerSupport;

public class H2UpsertAssembler implements UpsertAssembler {

  private final PreparedSqlBuilder buf;

  private final EntityType<?> entityType;

  private final DuplicateKeyType duplicateKeyType;

  private final UpsertAssemblerSupport upsertAssemblerSupport;

  private final List<? extends EntityPropertyType<?, ?>> keys;

  private final QueryRows insertValues;

  private final QueryOperandPairList setValues;

  private final QueryOperand.Visitor queryOperandVisitor = new QueryOperandVisitor();

  public H2UpsertAssembler(UpsertAssemblerContext context) {
    this.buf = context.buf;
    this.entityType = context.entityType;
    this.duplicateKeyType = context.duplicateKeyType;
    this.keys = context.keys;
    this.insertValues = context.insertValues;
    this.setValues = context.setValues;
    this.upsertAssemblerSupport = new UpsertAssemblerSupport(context.naming, context.dialect);
  }

  @Override
  public void assemble() {
    buf.appendSql("merge into ");
    tableNameAndAlias(entityType);
    buf.appendSql(" using (");
    excludeQuery();
    buf.appendSql(") as ");
    excludeAlias();
    buf.appendSql(" on ");
    for (EntityPropertyType<?, ?> key : keys) {
      targetColumn(key);
      buf.appendSql(" = ");
      excludeColumn(key);
      buf.appendSql(" and ");
    }
    buf.cutBackSql(5);
    buf.appendSql(" when not matched then insert ");
    join(
        insertValues.first().getPairs(),
        ", ",
        "(",
        ")",
        buf,
        p -> {
          column(p.getLeft().getEntityPropertyType());
        });
    buf.appendSql(" values ");
    join(
        insertValues.getRows(),
        ", ",
        "",
        "",
        buf,
        row -> {
          join(
              row.getPairs(),
              ", ",
              "(",
              ")",
              buf,
              p -> {
                excludeColumn(p.getLeft().getEntityPropertyType());
              });
        });
    if (duplicateKeyType == DuplicateKeyType.UPDATE) {
      buf.appendSql(" when matched then update set ");
      for (QueryOperandPair pair : setValues.getPairs()) {
        targetColumn(pair.getLeft().getEntityPropertyType());
        buf.appendSql(" = ");
        pair.getRight().accept(queryOperandVisitor);
        buf.appendSql(", ");
      }
      buf.cutBackSql(2);
    }
  }

  private void excludeQuery() {
    buf.appendSql("select ");
    join(
        insertValues.first().getPairs(),
        ", ",
        "",
        "",
        buf,
        p -> {
          column(p.getLeft().getEntityPropertyType());
        });
    buf.appendSql(" from values ");
    join(
        insertValues.getRows(),
        ", ",
        "",
        "",
        buf,
        row -> {
          join(
              row.getPairs(),
              ", ",
              "(",
              ")",
              buf,
              p -> {
                p.getRight().accept(queryOperandVisitor);
              });
        });
    buf.appendSql(" as x ");
    join(
        insertValues.first().getPairs(),
        ", ",
        "(",
        ")",
        buf,
        p -> {
          column(p.getLeft().getEntityPropertyType());
        });
  }

  private <T> void join(
      List<T> list,
      String delimiter,
      String start,
      String end,
      PreparedSqlBuilder buf,
      Consumer<T> consumer) {
    buf.appendSql(start);
    for (T val : list) {
      consumer.accept(val);
      buf.appendSql(delimiter);
    }
    buf.cutBackSql(delimiter.length());
    buf.appendSql(end);
  }

  private void tableNameAndAlias(EntityType<?> entityType) {
    String sql =
        this.upsertAssemblerSupport.targetTable(
            entityType, UpsertAssemblerSupport.TableNameType.NAME_AS_ALIAS);
    buf.appendSql(sql);
  }

  private void excludeAlias() {
    String sql = this.upsertAssemblerSupport.excludeAlias();
    buf.appendSql(sql);
  }

  private void targetColumn(EntityPropertyType<?, ?> propertyType) {
    String sql =
        this.upsertAssemblerSupport.targetProp(
            propertyType, UpsertAssemblerSupport.ColumnNameType.NAME_ALIAS);
    buf.appendSql(sql);
  }

  private void excludeColumn(EntityPropertyType<?, ?> propertyType) {
    String sql =
        this.upsertAssemblerSupport.excludeProp(
            propertyType, UpsertAssemblerSupport.ColumnNameType.NAME_ALIAS);
    buf.appendSql(sql);
  }

  private void column(EntityPropertyType<?, ?> propertyType) {
    String sql =
        this.upsertAssemblerSupport.excludeProp(
            propertyType, UpsertAssemblerSupport.ColumnNameType.NAME);
    buf.appendSql(sql);
  }

  class QueryOperandVisitor implements QueryOperand.Visitor {

    @Override
    public void visit(QueryOperand.Param param) {
      buf.appendParameter(param.inParameter);
    }

    @Override
    public void visit(QueryOperand.Prop prop) {
      String sql =
          upsertAssemblerSupport.excludeProp(
              prop.propertyType, UpsertAssemblerSupport.ColumnNameType.NAME_ALIAS);
      buf.appendSql(sql);
    }
  }
}
