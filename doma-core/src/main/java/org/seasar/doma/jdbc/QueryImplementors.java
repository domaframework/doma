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

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.ArrayCreateQuery;
import org.seasar.doma.jdbc.query.AutoBatchDeleteQuery;
import org.seasar.doma.jdbc.query.AutoBatchInsertQuery;
import org.seasar.doma.jdbc.query.AutoBatchUpdateQuery;
import org.seasar.doma.jdbc.query.AutoDeleteQuery;
import org.seasar.doma.jdbc.query.AutoFunctionQuery;
import org.seasar.doma.jdbc.query.AutoInsertQuery;
import org.seasar.doma.jdbc.query.AutoMultiInsertQuery;
import org.seasar.doma.jdbc.query.AutoProcedureQuery;
import org.seasar.doma.jdbc.query.AutoUpdateQuery;
import org.seasar.doma.jdbc.query.BlobCreateQuery;
import org.seasar.doma.jdbc.query.ClobCreateQuery;
import org.seasar.doma.jdbc.query.NClobCreateQuery;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.query.SQLXMLCreateQuery;
import org.seasar.doma.jdbc.query.SqlDeleteQuery;
import org.seasar.doma.jdbc.query.SqlFileBatchDeleteQuery;
import org.seasar.doma.jdbc.query.SqlFileBatchInsertQuery;
import org.seasar.doma.jdbc.query.SqlFileBatchUpdateQuery;
import org.seasar.doma.jdbc.query.SqlFileDeleteQuery;
import org.seasar.doma.jdbc.query.SqlFileInsertQuery;
import org.seasar.doma.jdbc.query.SqlFileScriptQuery;
import org.seasar.doma.jdbc.query.SqlFileSelectQuery;
import org.seasar.doma.jdbc.query.SqlFileUpdateQuery;
import org.seasar.doma.jdbc.query.SqlInsertQuery;
import org.seasar.doma.jdbc.query.SqlProcessorQuery;
import org.seasar.doma.jdbc.query.SqlSelectQuery;
import org.seasar.doma.jdbc.query.SqlUpdateQuery;

/**
 * A factory interface for creating {@link Query} implementation objects.
 *
 * <p>This interface provides factory methods for instantiating various types of database queries
 * used in the Doma framework. Each method creates a specific type of query object that encapsulates
 * the details of a database operation.
 *
 * <p>Implementations of this interface are responsible for creating the appropriate query objects
 * based on the DAO method being executed. These query objects define how SQL is generated and
 * executed for different database operations (select, insert, update, delete, batch operations,
 * etc.).
 *
 * <p>The default implementations of these methods create standard query objects, but custom
 * implementations can override these methods to provide specialized behavior or extended
 * functionality for specific query types.
 */
public interface QueryImplementors {

  /**
   * Creates an {@link SqlFileSelectQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlFileSelectQuery createSqlFileSelectQuery(Method method) {
    return new SqlFileSelectQuery();
  }

  /**
   * Creates an {@link SqlSelectQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlSelectQuery createSqlSelectQuery(Method method) {
    return new SqlSelectQuery();
  }

  /**
   * Creates an {@link SqlFileScriptQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlFileScriptQuery createSqlFileScriptQuery(Method method) {
    return new SqlFileScriptQuery();
  }

  /**
   * Creates an {@link AutoDeleteQuery} object.
   *
   * @param <ENTITY> the entity type
   * @param method the DAO method
   * @param entityType the entity type
   * @return the query
   */
  default <ENTITY> AutoDeleteQuery<ENTITY> createAutoDeleteQuery(
      Method method, EntityType<ENTITY> entityType) {
    return new AutoDeleteQuery<>(entityType);
  }

  /**
   * Creates an {@link AutoInsertQuery} object.
   *
   * @param <ENTITY> the entity type
   * @param method the DAO method
   * @param entityType the entity type
   * @return the query
   */
  default <ENTITY> AutoInsertQuery<ENTITY> createAutoInsertQuery(
      Method method, EntityType<ENTITY> entityType) {
    return new AutoInsertQuery<>(entityType);
  }

  /**
   * Creates an {@link AutoMultiInsertQuery} object.
   *
   * @param <ENTITY> the entity type
   * @param method the DAO method
   * @param entityType the entity type
   * @return the query
   */
  default <ENTITY> AutoMultiInsertQuery<ENTITY> createAutoMultiInsertQuery(
      Method method, EntityType<ENTITY> entityType) {
    return new AutoMultiInsertQuery<>(entityType);
  }

  /**
   * Creates an {@link AutoUpdateQuery} object.
   *
   * @param <ENTITY> the entity type
   * @param method the DAO method
   * @param entityType the entity type
   * @return the query
   */
  default <ENTITY> AutoUpdateQuery<ENTITY> createAutoUpdateQuery(
      Method method, EntityType<ENTITY> entityType) {
    return new AutoUpdateQuery<>(entityType);
  }

  /**
   * Creates an {@link SqlFileDeleteQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlFileDeleteQuery createSqlFileDeleteQuery(Method method) {
    return new SqlFileDeleteQuery();
  }

  /**
   * Creates an {@link SqlFileInsertQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlFileInsertQuery createSqlFileInsertQuery(Method method) {
    return new SqlFileInsertQuery();
  }

  /**
   * Creates an {@link SqlFileUpdateQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlFileUpdateQuery createSqlFileUpdateQuery(Method method) {
    return new SqlFileUpdateQuery();
  }

  /**
   * Creates an {@link SqlDeleteQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlDeleteQuery createSqlDeleteQuery(Method method) {
    return new SqlDeleteQuery();
  }

  /**
   * Creates an {@link SqlInsertQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlInsertQuery createSqlInsertQuery(Method method) {
    return new SqlInsertQuery();
  }

  /**
   * Creates an {@link SqlUpdateQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlUpdateQuery createSqlUpdateQuery(Method method) {
    return new SqlUpdateQuery();
  }

  /**
   * Creates an {@link AutoBatchDeleteQuery} object.
   *
   * @param <ENTITY> the entity type
   * @param method the DAO method
   * @param entityType the entity type
   * @return the query
   */
  default <ENTITY> AutoBatchDeleteQuery<ENTITY> createAutoBatchDeleteQuery(
      Method method, EntityType<ENTITY> entityType) {
    return new AutoBatchDeleteQuery<>(entityType);
  }

  /**
   * Creates an {@link AutoBatchInsertQuery} object.
   *
   * @param <ENTITY> the entity type
   * @param method the DAO method
   * @param entityType the entity type
   * @return the query
   */
  default <ENTITY> AutoBatchInsertQuery<ENTITY> createAutoBatchInsertQuery(
      Method method, EntityType<ENTITY> entityType) {
    return new AutoBatchInsertQuery<>(entityType);
  }

  /**
   * Creates an {@link AutoBatchUpdateQuery} object.
   *
   * @param <ENTITY> the entity type
   * @param method the DAO method
   * @param entityType the entity type
   * @return the query
   */
  default <ENTITY> AutoBatchUpdateQuery<ENTITY> createAutoBatchUpdateQuery(
      Method method, EntityType<ENTITY> entityType) {
    return new AutoBatchUpdateQuery<>(entityType);
  }

  /**
   * Creates an {@link SqlFileBatchDeleteQuery} object.
   *
   * @param <ELEMENT> the element type of the batch list
   * @param method the DAO method
   * @param clazz the element class of the batch list
   * @return the query
   */
  default <ELEMENT> SqlFileBatchDeleteQuery<ELEMENT> createSqlFileBatchDeleteQuery(
      Method method, Class<ELEMENT> clazz) {
    return new SqlFileBatchDeleteQuery<>(clazz);
  }

  /**
   * Creates an {@link SqlFileBatchInsertQuery} object.
   *
   * @param <ELEMENT> the element type of the batch list
   * @param method the DAO method
   * @param clazz the element class of the batch list
   * @return the query
   */
  default <ELEMENT> SqlFileBatchInsertQuery<ELEMENT> createSqlFileBatchInsertQuery(
      Method method, Class<ELEMENT> clazz) {
    return new SqlFileBatchInsertQuery<>(clazz);
  }

  /**
   * Creates a {@link SqlFileBatchUpdateQuery} object.
   *
   * @param <ELEMENT> the element type of the batch list
   * @param method the DAO method
   * @param clazz the element class of the batch list
   * @return the query
   */
  default <ELEMENT> SqlFileBatchUpdateQuery<ELEMENT> createSqlFileBatchUpdateQuery(
      Method method, Class<ELEMENT> clazz) {
    return new SqlFileBatchUpdateQuery<>(clazz);
  }

  /**
   * Creates an {@link AutoFunctionQuery} object.
   *
   * @param <RESULT> the result type of the function query
   * @param method the DAO method
   * @return the query
   */
  default <RESULT> AutoFunctionQuery<RESULT> createAutoFunctionQuery(Method method) {
    return new AutoFunctionQuery<>();
  }

  /**
   * Creates an {@link AutoProcedureQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default AutoProcedureQuery createAutoProcedureQuery(Method method) {
    return new AutoProcedureQuery();
  }

  /**
   * Creates an {@link ArrayCreateQuery } object.
   *
   * @param method the DAO method
   * @return the query
   */
  default ArrayCreateQuery createArrayCreateQuery(Method method) {
    return new ArrayCreateQuery();
  }

  /**
   * Creates a {@link BlobCreateQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default BlobCreateQuery createBlobCreateQuery(Method method) {
    return new BlobCreateQuery();
  }

  /**
   * Creates a {@link ClobCreateQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default ClobCreateQuery createClobCreateQuery(Method method) {
    return new ClobCreateQuery();
  }

  /**
   * Creates a {@link NClobCreateQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default NClobCreateQuery createNClobCreateQuery(Method method) {
    return new NClobCreateQuery();
  }

  /**
   * Creates an {@link SQLXMLCreateQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SQLXMLCreateQuery createSQLXMLCreateQuery(Method method) {
    return new SQLXMLCreateQuery();
  }

  /**
   * Creates an {@link SqlProcessorQuery} object.
   *
   * @param method the DAO method
   * @return the query
   */
  default SqlProcessorQuery createSqlProcessorQuery(Method method) {
    return new SqlProcessorQuery();
  }
}
