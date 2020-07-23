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

/** A factory for the {@link Query} implementation classes. */
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
