/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc;

import java.util.Map;
import org.seasar.doma.internal.jdbc.command.ColumnNameMapFormatter;
import org.seasar.doma.internal.jdbc.command.MappingSupport;
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
  @Deprecated(forRemoval = true)
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

  /**
   * Handles the unknown column with additional context provided by the column name map.
   *
   * @param query the query associated with the operation
   * @param entityType the entity type description
   * @param unknownColumnName the name of the unknown column
   * @param columnNameMap the map containing column names and their corresponding property types
   * @throws UnknownColumnAdditionalInfoException if handling the unknown column fails with
   *     additional information
   */
  default void handle(
      Query query,
      EntityType<?> entityType,
      String unknownColumnName,
      Map<String, MappingSupport.PropType> columnNameMap) {
    try {
      handle(query, entityType, unknownColumnName);
    } catch (UnknownColumnException original) {
      String additionalInfo = ColumnNameMapFormatter.format(columnNameMap);
      UnknownColumnAdditionalInfoException ex =
          new UnknownColumnAdditionalInfoException(
              query.getConfig().getExceptionSqlLogType(), original, additionalInfo);
      ex.addSuppressed(original);
      throw ex;
    }
  }
}
