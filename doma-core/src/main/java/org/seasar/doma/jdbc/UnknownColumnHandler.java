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

import java.util.function.Supplier;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.query.Query;

/**
 * A handler for columns in a result set that are not mapped to any property in an entity.
 *
 * <p>This interface defines how to handle situations where a column in a database result set
 * doesn't correspond to any property in the entity class being used for mapping. Implementations
 * can choose to ignore unknown columns, throw exceptions, or handle them in custom ways.
 *
 * <p>The default implementation throws an {@link UnknownColumnException} when an unknown column is
 * encountered, providing detailed information about the mismatch.
 *
 * @see UnknownColumnException
 * @see UnknownColumnAdditionalInfoException
 */
public interface UnknownColumnHandler {

  /**
   * Handles the unknown column.
   *
   * <p>This method is called when a column in a result set doesn't match any property in the entity
   * being mapped. The default implementation throws an {@link UnknownColumnException} with detailed
   * information about the mismatch.
   *
   * @param query the query being executed
   * @param entityType the entity type containing metadata about the entity
   * @param unknownColumnName the name of the unknown column
   * @throws UnknownColumnException if this handler does not allow the unknown column
   * @deprecated This method has been deprecated in favor of {@link #handle(Query, EntityType,
   *     String, Supplier)} which provides additional context information about the unknown column.
   *     Will be removed in a future version.
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
   * Handles an unknown column during query execution. Provides additional information for the
   * exception when the column is unrecognized by the entity.
   *
   * @param query the query being executed
   * @param entityType the entity type containing the metadata about the entity
   * @param unknownColumnName the name of the column that is unknown
   * @param informationSupplier the supplier that provides additional information about the unknown
   *     column
   * @throws UnknownColumnAdditionalInfoException if the unknown column cannot be handled
   */
  default void handle(
      Query query,
      EntityType<?> entityType,
      String unknownColumnName,
      Supplier<String> informationSupplier) {
    try {
      handle(query, entityType, unknownColumnName);
    } catch (UnknownColumnException original) {
      String additionalInfo = informationSupplier.get();
      UnknownColumnAdditionalInfoException ex =
          new UnknownColumnAdditionalInfoException(
              query.getConfig().getExceptionSqlLogType(), original, additionalInfo);
      ex.addSuppressed(original);
      throw ex;
    }
  }
}
