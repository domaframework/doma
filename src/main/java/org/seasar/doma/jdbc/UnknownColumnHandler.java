package org.seasar.doma.jdbc;

import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.query.Query;

/** A handler for the column that is unknown to an entity. */
public interface UnknownColumnHandler {

  /**
   * Handles the unknown column.
   *
   * @param query the query
   * @param entityType the entity description
   * @param unknownColumnName the name of the unknown column
   * @throws UnknownColumnException if this handler does not allow the unknown column
   */
  default void handle(Query query, EntityType<?> entityType, String unknownColumnName) {
    Sql<?> sql = query.getSql();
    Naming naming = query.getConfig().getNaming();
    NamingType namingType = entityType.getNamingType();
    throw new UnknownColumnException(
        query.getConfig().getExceptionSqlLogType(),
        unknownColumnName,
        naming.revert(namingType, unknownColumnName),
        entityType.getEntityClass().getName(),
        sql.getKind(),
        sql.getRawSql(),
        sql.getFormattedSql(),
        sql.getSqlFilePath());
  }
}
