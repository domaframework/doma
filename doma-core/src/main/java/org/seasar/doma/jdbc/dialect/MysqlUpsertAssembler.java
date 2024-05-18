package org.seasar.doma.jdbc.dialect;

import java.util.List;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.DuplicateKeyType;
import org.seasar.doma.jdbc.query.QueryOperand;
import org.seasar.doma.jdbc.query.QueryOperandPair;
import org.seasar.doma.jdbc.query.UpsertAssembler;
import org.seasar.doma.jdbc.query.UpsertAssemblerContext;
import org.seasar.doma.jdbc.query.UpsertAssemblerSupport;

public class MysqlUpsertAssembler implements UpsertAssembler {
  private final PreparedSqlBuilder buf;
  private final EntityType<?> entityType;
  private final DuplicateKeyType duplicateKeyType;
  private final UpsertAssemblerSupport upsertAssemblerSupport;
  private final List<QueryOperandPair> insertValues;
  private final List<QueryOperandPair> setValues;
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
    buf.appendSql(" (");
    for (QueryOperandPair pair : insertValues) {
      column(pair.getLeft().getEntityPropertyType());
      buf.appendSql(", ");
    }
    buf.cutBackSql(2);
    buf.appendSql(") values (");
    for (QueryOperandPair pair : insertValues) {
      pair.getRight().accept(queryOperandVisitor);
      buf.appendSql(", ");
    }
    buf.cutBackSql(2);
    switch (version) {
      case V5:
        buf.appendSql(") ");
        break;
      case V8:
        buf.appendSql(") as ");
        excludeAlias();
        break;
      default:
        throw new IllegalStateException(version.toString());
    }
    if (duplicateKeyType == DuplicateKeyType.UPDATE) {
      buf.appendSql(" on duplicate key update ");
      for (QueryOperandPair pair : setValues) {
        column(pair.getLeft().getEntityPropertyType());
        buf.appendSql(" = ");
        pair.getRight().accept(queryOperandVisitor);
        buf.appendSql(", ");
      }
      buf.cutBackSql(2);
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
