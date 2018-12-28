/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
