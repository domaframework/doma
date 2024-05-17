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

public class PostgreSqlUpsertAssembler implements UpsertAssembler {
  private final PreparedSqlBuilder buf;
  private final EntityType<?> entityType;
  private final DuplicateKeyType duplicateKeyType;
  private final UpsertAssemblerSupport upsertAssemblerSupport;

  private final boolean isKeysSpecified;
  private final List<? extends EntityPropertyType<?, ?>> keys;

  private final List<? extends Tuple2<? extends EntityPropertyType<?, ?>, ? extends InParameter<?>>>
      insertValues;
  private final List<? extends Tuple2<? extends EntityPropertyType<?, ?>, ? extends UpsertSetValue>>
      setValues;
  private final UpsertSetValue.Visitor upsertSetValueVisitor = new UpsertSetValueVisitor();

  public PostgreSqlUpsertAssembler(UpsertAssemblerContext context) {
    this.buf = context.buf;
    this.entityType = context.entityType;
    this.duplicateKeyType = context.duplicateKeyType;
    this.keys = context.keys;
    this.isKeysSpecified = context.isKeysSpecified;
    this.insertValues = context.insertValues;
    this.setValues = context.setValues;
    this.upsertAssemblerSupport = new UpsertAssemblerSupport(context.naming, context.dialect);
  }

  @Override
  public void assemble() {
    buf.appendSql("insert into ");
    tableNameAndAlias(entityType);
    buf.appendSql(" (");
    for (Tuple2<? extends EntityPropertyType<?, ?>, ? extends InParameter<?>> insertValue :
        insertValues) {
      column(insertValue.component1());
      buf.appendSql(", ");
    }
    buf.cutBackSql(2);
    buf.appendSql(") values (");
    for (Tuple2<? extends EntityPropertyType<?, ?>, ? extends InParameter<?>> insertValue :
        insertValues) {
      buf.appendParameter(insertValue.component2());
      buf.appendSql(", ");
    }
    buf.cutBackSql(2);
    if (duplicateKeyType == DuplicateKeyType.IGNORE) {
      buf.appendSql(") on conflict");
      if (isKeysSpecified) {
        buf.appendSql(" (");
        for (EntityPropertyType<?, ?> key : keys) {
          column(key);
          buf.appendSql(", ");
        }
        buf.cutBackSql(2);
        buf.appendSql(")");
      }
      buf.appendSql(" do nothing");
    } else if (duplicateKeyType == DuplicateKeyType.UPDATE) {
      buf.appendSql(") on conflict (");
      for (EntityPropertyType<?, ?> key : keys) {
        column(key);
        buf.appendSql(", ");
      }
      buf.cutBackSql(2);
      buf.appendSql(")");
      buf.appendSql(" do update set ");
      for (Tuple2<? extends EntityPropertyType<?, ?>, ? extends UpsertSetValue> setValue :
          setValues) {
        column(setValue.component1());
        buf.appendSql(" = ");
        setValue.component2().accept(upsertSetValueVisitor);
        buf.appendSql(", ");
      }
      buf.cutBackSql(2);
    }
  }

  private void tableNameAndAlias(EntityType<?> entityType) {
    String sql =
        this.upsertAssemblerSupport.targetTable(
            entityType, UpsertAssemblerSupport.TableNameType.NAME_AS_ALIAS);
    buf.appendSql(sql);
  }

  private void column(EntityPropertyType<?, ?> propertyType) {
    String sql = this.upsertAssemblerSupport.prop(propertyType);
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
