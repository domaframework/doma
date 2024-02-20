package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import org.seasar.doma.jdbc.command.*;
import org.seasar.doma.jdbc.query.*;

/** A factory for the {@link Command} implementation classes. */
public interface CommandImplementors {

  /**
   * Creates a {@link SelectCommand} object.
   *
   * @param <RESULT> the result type of the command
   * @param method the DAO method
   * @param query the query
   * @param resultSetHandler the result set handler
   * @return the command
   */
  default <RESULT> SelectCommand<RESULT> createSelectCommand(
      Method method, SelectQuery query, ResultSetHandler<RESULT> resultSetHandler) {
    return new SelectCommand<>(query, resultSetHandler);
  }

  /**
   * Creates a {@link DeleteCommand} object.
   *
   * @param method the DAO method
   * @param query the query
   * @return the command
   */
  default DeleteCommand createDeleteCommand(Method method, DeleteQuery query) {
    return new DeleteCommand(query);
  }

  /**
   * Creates an {@link InsertCommand} object.
   *
   * @param method the DAO method
   * @param query the query
   * @return the command
   */
  default InsertCommand createInsertCommand(Method method, InsertQuery query) {
    return new InsertCommand(query);
  }

  /**
   * Creates an {@link UpdateCommand} object.
   *
   * @param method the DAO method
   * @param query the query
   * @return the command
   */
  default UpdateCommand createUpdateCommand(Method method, UpdateQuery query) {
    return new UpdateCommand(query);
  }

  /**
   * Creates a {@link BatchDeleteCommand} object.
   *
   * @param method the DAO method
   * @param query the query
   * @return the command
   */
  default BatchDeleteCommand createBatchDeleteCommand(Method method, BatchDeleteQuery query) {
    return new BatchDeleteCommand(query);
  }

  /**
   * Creates a {@link BatchInsertCommand} object.
   *
   * @param method the DAO method
   * @param query the query
   * @return the command
   */
  default BatchInsertCommand createBatchInsertCommand(Method method, BatchInsertQuery query) {
    return new BatchInsertCommand(query);
  }

  /**
   * Creates a {@link BatchUpdateCommand} object.
   *
   * @param method the DAO method
   * @param query the query
   * @return the command
   */
  default BatchUpdateCommand createBatchUpdateCommand(Method method, BatchUpdateQuery query) {
    return new BatchUpdateCommand(query);
  }

  /**
   * Creates a {@link FunctionCommand} object.
   *
   * @param <RESULT> the result type of the command
   * @param method the DAO method
   * @param query the query
   * @return the command
   */
  default <RESULT> FunctionCommand<RESULT> createFunctionCommand(
      Method method, FunctionQuery<RESULT> query) {
    return new FunctionCommand<>(query);
  }

  /**
   * Creates a {@link ProcedureCommand} object.
   *
   * @param method the DAO method
   * @param query the query
   * @return the command
   */
  default ProcedureCommand createProcedureCommand(Method method, ProcedureQuery query) {
    return new ProcedureCommand(query);
  }

  /**
   * Creates a {@link CreateCommand} object.
   *
   * @param <RESULT> the result type of the command
   * @param method the DAO method
   * @param query the query
   * @return the command
   */
  default <RESULT> CreateCommand<RESULT> createCreateCommand(
      Method method, CreateQuery<RESULT> query) {
    return new CreateCommand<>(query);
  }

  /**
   * Creates a {@link ScriptCommand} object.
   *
   * @param method the DAO method
   * @param query the query
   * @return the command
   */
  default ScriptCommand createScriptCommand(Method method, ScriptQuery query) {
    return new ScriptCommand(query);
  }

  /**
   * Creates a {@link SqlProcessorCommand} object
   *
   * @param <RESULT> the result type of the command
   * @param method the DAO method
   * @param query the query
   * @param handler the SQL handler
   * @return the command
   */
  default <RESULT> SqlProcessorCommand<RESULT> createSqlProcessorCommand(
      Method method, SqlProcessorQuery query, BiFunction<Config, PreparedSql, RESULT> handler) {
    return new SqlProcessorCommand<>(query, handler);
  }
}
