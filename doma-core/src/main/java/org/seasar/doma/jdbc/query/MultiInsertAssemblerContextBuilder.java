package org.seasar.doma.jdbc.query;

import java.util.List;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;

public class MultiInsertAssemblerContextBuilder {

  public static <ENTITY> MultiInsertAssemblerContext<ENTITY> buildFromEntityList(
      PreparedSqlBuilder buf,
      EntityType<ENTITY> entityType,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<ENTITY, ?>> targetPropertyTypes,
      List<ENTITY> entities) {

    return buildInternal(buf, entityType, naming, dialect, targetPropertyTypes, entities);
  }

  private static <ENTITY> MultiInsertAssemblerContext<ENTITY> buildInternal(
      PreparedSqlBuilder buf,
      EntityType<ENTITY> entityType,
      Naming naming,
      Dialect dialect,
      List<EntityPropertyType<ENTITY, ?>> targetPropertyTypes,
      List<ENTITY> entities) {

    return new MultiInsertAssemblerContext<ENTITY>(
        buf, entityType, naming, dialect, targetPropertyTypes, entities);
  }
}
