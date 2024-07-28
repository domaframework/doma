package org.seasar.doma.jdbc.dialect;

import java.util.List;
import java.util.function.Consumer;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.DuplicateKeyType;
import org.seasar.doma.jdbc.query.QueryOperand;
import org.seasar.doma.jdbc.query.QueryOperandPairList;
import org.seasar.doma.jdbc.query.QueryRows;
import org.seasar.doma.jdbc.query.UpsertAssembler;
import org.seasar.doma.jdbc.query.UpsertAssemblerContext;
import org.seasar.doma.jdbc.query.UpsertAssemblerSupport;

public class OracleUpsertAssembler implements UpsertAssembler {
  private final PreparedSqlBuilder buf;
  private final EntityType<?> entityType;
  private final DuplicateKeyType duplicateKeyType;
  private final UpsertAssemblerSupport upsertAssemblerSupport;
  private final List<? extends EntityPropertyType<?, ?>> keys;
  private final QueryRows insertValues;
  private final QueryOperandPairList setValues;
  private final QueryOperand.Visitor queryOperandVisitor = new QueryOperandVisitor();

  public OracleUpsertAssembler(UpsertAssemblerContext context) {
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
    buf.appendSql(") ");
    excludeAlias();
    buf.appendSql(" on ");
    join(
        keys,
        " and ",
        "(",
        ")",
        buf,
        key -> {
          targetColumn(key);
          buf.appendSql(" = ");
          excludeColumn(key);
        });
    buf.appendSql(" when not matched then insert ");
    join(
        setValues.getPairs(),
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
      join(
          setValues.getPairs(),
          ", ",
          "",
          "",
          buf,
          p -> {
            targetColumn(p.getLeft().getEntityPropertyType());
            buf.appendSql(" = ");
            p.getRight().accept(queryOperandVisitor);
          });
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
          p.getRight().accept(queryOperandVisitor);
          buf.appendSql(" as ");
          column(p.getLeft().getEntityPropertyType());
        });
    buf.appendSql(" from dual");
  }

  private void tableNameAndAlias(EntityType<?> entityType) {
    String sql =
        this.upsertAssemblerSupport.targetTable(
            entityType, UpsertAssemblerSupport.TableNameType.NAME_ALIAS);
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
