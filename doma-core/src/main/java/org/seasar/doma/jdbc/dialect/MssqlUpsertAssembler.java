package org.seasar.doma.jdbc.dialect;

import java.util.List;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.DuplicateKeyType;
import org.seasar.doma.jdbc.query.UpsertAssembler;
import org.seasar.doma.jdbc.query.UpsertAssemblerContext;
import org.seasar.doma.jdbc.query.UpsertAssemblerSupport;
import org.seasar.doma.jdbc.query.UpsertSetValue;

public class MssqlUpsertAssembler implements UpsertAssembler {
  private final PreparedSqlBuilder buf;
  private final EntityType<?> entityType;
  private final DuplicateKeyType duplicateKeyType;
  private final UpsertAssemblerSupport upsertAssemblerSupport;
  private final List<EntityPropertyType<?, ?>> keys;
  private final List<Tuple2<EntityPropertyType<?, ?>, InParameter<?>>> insertValues;
  private final List<Tuple2<EntityPropertyType<?, ?>, UpsertSetValue>> setValues;
  private final UpsertSetValue.Visitor upsertSetValueVisitor = new UpsertSetValueVisitor();

  public MssqlUpsertAssembler(UpsertAssemblerContext context) {
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
    buf.appendSql(" on ");
    for (EntityPropertyType<?, ?> key : keys) {
      targetColumn(key);
      buf.appendSql(" = ");
      excludeColumn(key);
      buf.appendSql(" and ");
    }
    buf.cutBackSql(5);
    buf.appendSql(" when not matched then insert (");
    for (Tuple2<EntityPropertyType<?, ?>, InParameter<?>> insertValue : insertValues) {
      column(insertValue.component1());
      buf.appendSql(", ");
    }
    buf.cutBackSql(2);
    buf.appendSql(") values (");
    for (Tuple2<EntityPropertyType<?, ?>, InParameter<?>> insertValue : insertValues) {
      excludeColumn(insertValue.component1());
      buf.appendSql(", ");
    }
    buf.cutBackSql(2);
    buf.appendSql(")");
    if (duplicateKeyType == DuplicateKeyType.UPDATE) {
      buf.appendSql(" when matched then update set ");
      for (Tuple2<EntityPropertyType<?, ?>, UpsertSetValue> setValue : setValues) {
        targetColumn(setValue.component1());
        buf.appendSql(" = ");
        setValue.component2().accept(upsertSetValueVisitor);
        buf.appendSql(", ");
      }
      buf.cutBackSql(2);
    }
    buf.appendSql(";");
  }

  private void excludeQuery() {
    buf.appendSql("values (");
    for (Tuple2<EntityPropertyType<?, ?>, InParameter<?>> insertValue : insertValues) {
      buf.appendParameter(insertValue.component2());
      buf.appendSql(", ");
    }
    buf.cutBackSql(2);
    buf.appendSql(")) as ");
    excludeAlias();
    buf.appendSql(" (");
    for (Tuple2<EntityPropertyType<?, ?>, InParameter<?>> insertValue : insertValues) {
      column(insertValue.component1());
      buf.appendSql(", ");
    }
    buf.cutBackSql(2);
    buf.appendSql(")");
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

  class UpsertSetValueVisitor implements UpsertSetValue.Visitor {
    @Override
    public void visit(UpsertSetValue.Param param) {
      buf.appendParameter(param.inParameter);
    }

    @Override
    public void visit(UpsertSetValue.Prop prop) {
      String sql =
          upsertAssemblerSupport.excludeProp(
              prop.propertyType, UpsertAssemblerSupport.ColumnNameType.NAME_ALIAS);
      buf.appendSql(sql);
    }
  }
}
