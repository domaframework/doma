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

public class MysqlUpsertAssembler implements UpsertAssembler {
  private final PreparedSqlBuilder buf;
  private final EntityType<?> entityType;
  private final DuplicateKeyType duplicateKeyType;
  private final UpsertAssemblerSupport upsertAssemblerSupport;
  private final QueryRows insertValues;
  private final QueryOperandPairList setValues;
  private final QueryOperand.Visitor queryOperandVisitor = new QueryOperandVisitor();
  private final MysqlDialect.MySqlVersion version;

  public MysqlUpsertAssembler(UpsertAssemblerContext context, MysqlDialect.MySqlVersion version) {
    this.buf = context.buf;
    this.entityType = context.entityType;
    this.duplicateKeyType = context.duplicateKeyType;
    this.insertValues = context.insertValues;
    this.setValues = context.setValues;
    this.version = version;
    this.upsertAssemblerSupport = new UpsertAssemblerSupport(context.naming, context.dialect);
  }

  @Override
  public void assemble() {
    buf.appendSql("insert");
    if (duplicateKeyType == DuplicateKeyType.IGNORE) {
      buf.appendSql(" ignore");
    }
    buf.appendSql(" into ");
    tableNameOnly(entityType);
    join(
        insertValues.first().getPairs(),
        ", ",
        " (",
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
                p.getRight().accept(queryOperandVisitor);
              });
        });
    switch (version) {
      case V5:
        buf.appendSql(" ");
        break;
      case V8:
        buf.appendSql(" as ");
        excludeAlias();
        break;
      default:
        throw new IllegalStateException(version.toString());
    }
    if (duplicateKeyType == DuplicateKeyType.UPDATE) {
      buf.appendSql(" on duplicate key update ");
      join(
          setValues.getPairs(),
          ", ",
          "",
          "",
          buf,
          p -> {
            column(p.getLeft().getEntityPropertyType());
            buf.appendSql(" = ");
            p.getRight().accept(queryOperandVisitor);
          });
    }
  }

  private void tableNameOnly(EntityType<?> entityType) {
    String sql =
        this.upsertAssemblerSupport.targetTable(
            entityType, UpsertAssemblerSupport.TableNameType.NAME);
    buf.appendSql(sql);
  }

  private void excludeAlias() {
    String sql = this.upsertAssemblerSupport.excludeAlias();
    buf.appendSql(sql);
  }

  private void column(EntityPropertyType<?, ?> propertyType) {
    String sql = this.upsertAssemblerSupport.prop(propertyType);
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
      switch (version) {
        case V5:
          {
            String sql =
                upsertAssemblerSupport.excludeProp(
                    prop.propertyType, UpsertAssemblerSupport.ColumnNameType.NAME);
            buf.appendSql("values(");
            buf.appendSql(sql);
            buf.appendSql(")");
            break;
          }
        case V8:
          {
            String sql =
                upsertAssemblerSupport.excludeProp(
                    prop.propertyType, UpsertAssemblerSupport.ColumnNameType.NAME_ALIAS);
            buf.appendSql(sql);
            break;
          }
        default:
          throw new IllegalStateException(version.toString());
      }
    }
  }
}
