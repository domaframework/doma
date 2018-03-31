package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.query.*;

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

  /**
   * Creates an {@link AutoDeleteQuery} object.
   *
   * @param method the DAO method
   * @param entityDesc the entity description
   * @return the query
   */
  default <ENTITY> AutoDeleteQuery<ENTITY> createAutoDeleteQuery(
      Method method, EntityDesc<ENTITY> entityDesc) {
    return new AutoDeleteQuery<>(entityDesc);
  }

  /**
   * Creates an {@link AutoInsertQuery} object.
   *
   * @param method the DAO method
   * @param entityDesc the entity description
   * @return the query
   */
  default <ENTITY> AutoInsertQuery<ENTITY> createAutoInsertQuery(
      Method method, EntityDesc<ENTITY> entityDesc) {
    return new AutoInsertQuery<>(entityDesc);
  }

  /**
   * Creates an {@link AutoUpdateQuery} object.
   *
   * @param method the DAO method
   * @param entityDesc the entity description
   * @return the query
   */
  default <ENTITY> AutoUpdateQuery<ENTITY> createAutoUpdateQuery(
      Method method, EntityDesc<ENTITY> entityDesc) {
    return new AutoUpdateQuery<>(entityDesc);
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
   * @param method the DAO method
   * @param entityDesc the entity description
   * @return the query
   */
  default <ENTITY> AutoBatchDeleteQuery<ENTITY> createAutoBatchDeleteQuery(
      Method method, EntityDesc<ENTITY> entityDesc) {
    return new AutoBatchDeleteQuery<>(entityDesc);
  }

  /**
   * Creates an {@link AutoBatchInsertQuery} object.
   *
   * @param method the DAO method
   * @param entityDesc the entity description
   * @return the query
   */
  default <ENTITY> AutoBatchInsertQuery<ENTITY> createAutoBatchInsertQuery(
      Method method, EntityDesc<ENTITY> entityDesc) {
    return new AutoBatchInsertQuery<>(entityDesc);
  }

  /**
   * Creates an {@link AutoBatchUpdateQuery} object.
   *
   * @param method the DAO method
   * @param entityDesc the entity description
   * @return the query
   */
  default <ENTITY> AutoBatchUpdateQuery<ENTITY> createAutoBatchUpdateQuery(
      Method method, EntityDesc<ENTITY> entityDesc) {
    return new AutoBatchUpdateQuery<>(entityDesc);
  }

  /**
   * Creates an {@link SqlFileBatchDeleteQuery} object.
   *
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
