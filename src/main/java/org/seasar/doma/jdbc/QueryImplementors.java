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

import java.lang.reflect.Method;
import org.seasar.doma.SqlProcessor;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.ArrayCreateQuery;
import org.seasar.doma.jdbc.query.AutoBatchDeleteQuery;
import org.seasar.doma.jdbc.query.AutoBatchInsertQuery;
import org.seasar.doma.jdbc.query.AutoBatchUpdateQuery;
import org.seasar.doma.jdbc.query.AutoDeleteQuery;
import org.seasar.doma.jdbc.query.AutoFunctionQuery;
import org.seasar.doma.jdbc.query.AutoInsertQuery;
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
 * {@link Query} の実装クラスのファクトリです。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public interface QueryImplementors {

  default SqlFileSelectQuery createSqlFileSelectQuery(Method method) {
    return new SqlFileSelectQuery();
  }

  default SqlSelectQuery createSqlSelectQuery(Method method) {
    return new SqlSelectQuery();
  }

  default SqlFileScriptQuery createSqlFileScriptQuery(Method method) {
    return new SqlFileScriptQuery();
  }

  default <ENTITY> AutoDeleteQuery<ENTITY> createAutoDeleteQuery(
      Method method, EntityType<ENTITY> entityType) {
    return new AutoDeleteQuery<>(entityType);
  }

  default <ENTITY> AutoInsertQuery<ENTITY> createAutoInsertQuery(
      Method method, EntityType<ENTITY> entityType) {
    return new AutoInsertQuery<>(entityType);
  }

  default <ENTITY> AutoUpdateQuery<ENTITY> createAutoUpdateQuery(
      Method method, EntityType<ENTITY> entityType) {
    return new AutoUpdateQuery<>(entityType);
  }

  default SqlFileDeleteQuery createSqlFileDeleteQuery(Method method) {
    return new SqlFileDeleteQuery();
  }

  default SqlFileInsertQuery createSqlFileInsertQuery(Method method) {
    return new SqlFileInsertQuery();
  }

  default SqlFileUpdateQuery createSqlFileUpdateQuery(Method method) {
    return new SqlFileUpdateQuery();
  }

  default SqlDeleteQuery createSqlDeleteQuery(Method method) {
    return new SqlDeleteQuery();
  }

  default SqlInsertQuery createSqlInsertQuery(Method method) {
    return new SqlInsertQuery();
  }

  default SqlUpdateQuery createSqlUpdateQuery(Method method) {
    return new SqlUpdateQuery();
  }

  default <ENTITY> AutoBatchDeleteQuery<ENTITY> createAutoBatchDeleteQuery(
      Method method, EntityType<ENTITY> entityType) {
    return new AutoBatchDeleteQuery<>(entityType);
  }

  default <ENTITY> AutoBatchInsertQuery<ENTITY> createAutoBatchInsertQuery(
      Method method, EntityType<ENTITY> entityType) {
    return new AutoBatchInsertQuery<>(entityType);
  }

  default <ENTITY> AutoBatchUpdateQuery<ENTITY> createAutoBatchUpdateQuery(
      Method method, EntityType<ENTITY> entityType) {
    return new AutoBatchUpdateQuery<>(entityType);
  }

  default <ELEMENT> SqlFileBatchDeleteQuery<ELEMENT> createSqlFileBatchDeleteQuery(
      Method method, Class<ELEMENT> clazz) {
    return new SqlFileBatchDeleteQuery<>(clazz);
  }

  default <ELEMENT> SqlFileBatchInsertQuery<ELEMENT> createSqlFileBatchInsertQuery(
      Method method, Class<ELEMENT> clazz) {
    return new SqlFileBatchInsertQuery<>(clazz);
  }

  default <ELEMENT> SqlFileBatchUpdateQuery<ELEMENT> createSqlFileBatchUpdateQuery(
      Method method, Class<ELEMENT> clazz) {
    return new SqlFileBatchUpdateQuery<>(clazz);
  }

  default <RESULT> AutoFunctionQuery<RESULT> createAutoFunctionQuery(Method method) {
    return new AutoFunctionQuery<>();
  }

  default AutoProcedureQuery createAutoProcedureQuery(Method method) {
    return new AutoProcedureQuery();
  }

  default ArrayCreateQuery createArrayCreateQuery(Method method) {
    return new ArrayCreateQuery();
  }

  default BlobCreateQuery createBlobCreateQuery(Method method) {
    return new BlobCreateQuery();
  }

  default ClobCreateQuery createClobCreateQuery(Method method) {
    return new ClobCreateQuery();
  }

  default NClobCreateQuery createNClobCreateQuery(Method method) {
    return new NClobCreateQuery();
  }

  default SQLXMLCreateQuery createSQLXMLCreateQuery(Method method) {
    return new SQLXMLCreateQuery();
  }

  /**
   * {@link SqlProcessor} に対応したクエリを作成します。
   *
   * @param method Dao メソッド
   * @return クエリ
   * @since 2.14.0
   */
  default SqlProcessorQuery createSqlProcessorQuery(Method method) {
    return new SqlProcessorQuery();
  }
}
