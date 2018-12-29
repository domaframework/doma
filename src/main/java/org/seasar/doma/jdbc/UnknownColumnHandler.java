package org.seasar.doma.jdbc;

import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.query.Query;

/**
 * エンティティにとっての未知のカラムを処理します。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public interface UnknownColumnHandler {

  /**
   * 未知のカラムを処理します。
   *
   * @param query クエリ
   * @param entityType エンティティのメタタイプ
   * @param unknownColumnName 未知のカラム名
   * @throws UnknownColumnException 未知のカラムを許可しない場合
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
