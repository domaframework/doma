package org.seasar.doma.jdbc.dialect;

import java.util.List;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.query.MultiInsertAssembler;
import org.seasar.doma.jdbc.query.MultiInsertAssemblerContext;

public class StandardMultiInsertAssembler<ENTITY> implements MultiInsertAssembler {

  public final PreparedSqlBuilder buf;
  public final EntityType<?> entityType;
  public final Naming naming;
  public final Dialect dialect;
  public final List<EntityPropertyType<ENTITY, ?>> targetPropertyTypes;
  public final List<ENTITY> entities;

  public StandardMultiInsertAssembler(MultiInsertAssemblerContext<ENTITY> context) {
    this.buf = context.buf;
    this.entityType = context.entityType;
    this.naming = context.naming;
    this.dialect = context.dialect;
    this.targetPropertyTypes = context.targetPropertyTypes;
    this.entities = context.entities;
  }

  @Override
  public void assemble() {
    buf.appendSql("insert into ");
    buf.appendSql(entityType.getQualifiedTableName(naming::apply, dialect::applyQuote));
    buf.appendSql(" (");
    if (!targetPropertyTypes.isEmpty()) {
      for (EntityPropertyType<?, ?> propertyType : targetPropertyTypes) {
        buf.appendSql(propertyType.getColumnName(naming::apply, dialect::applyQuote));
        buf.appendSql(", ");
      }
      buf.cutBackSql(2);
    }
    buf.appendSql(") values ");
    if (!entities.isEmpty()) {
      for (ENTITY entity : entities) {
        buf.appendSql("(");
        if (!targetPropertyTypes.isEmpty()) {
          for (EntityPropertyType<ENTITY, ?> propertyType : targetPropertyTypes) {
            Property<ENTITY, ?> property = propertyType.createProperty();
            property.load(entity);
            buf.appendParameter(property.asInParameter());
            buf.appendSql(", ");
          }
          buf.cutBackSql(2);
        }
        buf.appendSql("), ");
      }
      buf.cutBackSql(2);
    }
  }
}
