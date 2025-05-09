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
import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.seasar.doma.jdbc.aggregate.AggregateCommand;
import org.seasar.doma.jdbc.aggregate.AggregateStrategyType;
import org.seasar.doma.jdbc.aggregate.StreamReducer;
import org.seasar.doma.jdbc.command.BatchDeleteCommand;
import org.seasar.doma.jdbc.command.BatchInsertCommand;
import org.seasar.doma.jdbc.command.BatchUpdateCommand;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.CreateCommand;
import org.seasar.doma.jdbc.command.DeleteCommand;
import org.seasar.doma.jdbc.command.DeleteReturningCommand;
import org.seasar.doma.jdbc.command.FunctionCommand;
import org.seasar.doma.jdbc.command.InsertCommand;
import org.seasar.doma.jdbc.command.InsertReturningCommand;
import org.seasar.doma.jdbc.command.ProcedureCommand;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.ScriptCommand;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.command.SqlProcessorCommand;
import org.seasar.doma.jdbc.command.UpdateCommand;
import org.seasar.doma.jdbc.command.UpdateReturningCommand;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.BatchDeleteQuery;
import org.seasar.doma.jdbc.query.BatchInsertQuery;
import org.seasar.doma.jdbc.query.BatchUpdateQuery;
import org.seasar.doma.jdbc.query.CreateQuery;
import org.seasar.doma.jdbc.query.DeleteQuery;
import org.seasar.doma.jdbc.query.FunctionQuery;
import org.seasar.doma.jdbc.query.InsertQuery;
import org.seasar.doma.jdbc.query.ProcedureQuery;
import org.seasar.doma.jdbc.query.ScriptQuery;
import org.seasar.doma.jdbc.query.SelectQuery;
import org.seasar.doma.jdbc.query.SqlProcessorQuery;
import org.seasar.doma.jdbc.query.UpdateQuery;

/**
 * A factory interface for creating {@link Command} implementation objects.
 *
 * <p>This interface provides factory methods for instantiating various types of database commands
 * used in the Doma framework. Each method creates a specific type of command object that is
 * responsible for executing a corresponding {@link org.seasar.doma.jdbc.query.Query} object.
 *
 * <p>Implementations of this interface are responsible for creating the appropriate command objects
 * based on the DAO method being executed. These command objects handle the execution of database
 * operations (select, insert, update, delete, batch operations, etc.) and process the results.
 *
 * <p>The default implementations of these methods create standard command objects, but custom
 * implementations can override these methods to provide specialized behavior or extended
 * functionality for specific command types, such as custom transaction handling, logging, or
 * performance monitoring.
 */
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
   * Creates an {@link AggregateCommand} object for aggregating entities based on a specified query
   * and strategy.
   *
   * @param <RESULT> the result type of the aggregation
   * @param <ENTITY> the entity type used in the aggregation
   * @param method the DAO method associated with the command
   * @param query the select query to be executed
   * @param entityType the type of the root entity for aggregation
   * @param resultReducer the reducer used to process the stream of aggregated entities into a final
   *     result
   * @param aggregateStrategyType the strategy type used for aggregation logic
   * @return a new {@link AggregateCommand} instance configured with the provided parameters
   */
  default <RESULT, ENTITY> AggregateCommand<RESULT, ENTITY> createAggregateCommand(
      Method method,
      SelectQuery query,
      EntityType<ENTITY> entityType,
      StreamReducer<RESULT, ENTITY> resultReducer,
      AggregateStrategyType aggregateStrategyType) {
    return new AggregateCommand<>(query, entityType, resultReducer, aggregateStrategyType);
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
   * Creates a {@link DeleteReturningCommand} object.
   *
   * @param <RESULT> the result type of the command
   * @param method the DAO method
   * @param query the delete query
   * @param resultSetHandler the handler for processing the result set
   * @param emptyResultSupplier the supplier for providing an empty result
   * @return the created {@link DeleteReturningCommand} object
   */
  default <RESULT> DeleteReturningCommand<RESULT> createDeleteReturningCommand(
      Method method,
      DeleteQuery query,
      ResultSetHandler<RESULT> resultSetHandler,
      Supplier<RESULT> emptyResultSupplier) {
    return new DeleteReturningCommand<>(query, resultSetHandler, emptyResultSupplier);
  }

  /**
   * Creates an {@link InsertReturningCommand} object.
   *
   * @param method the DAO method
   * @param query the query
   * @param resultSetHandler the result set handler
   * @return the command
   */
  default <RESULT> InsertReturningCommand<RESULT> createInsertReturningCommand(
      Method method,
      InsertQuery query,
      ResultSetHandler<RESULT> resultSetHandler,
      Supplier<RESULT> emptyResultSupplier) {
    return new InsertReturningCommand<>(query, resultSetHandler, emptyResultSupplier);
  }

  /**
   * Creates an {@link UpdateReturningCommand} object.
   *
   * @param <RESULT> the result type of the command
   * @param method the DAO method
   * @param query the update query
   * @param resultSetHandler the handler for processing the result set
   * @param emptyResultSupplier the supplier for providing an empty result
   * @return the created {@link UpdateReturningCommand} object
   */
  default <RESULT> UpdateReturningCommand<RESULT> createUpdateReturningCommand(
      Method method,
      UpdateQuery query,
      ResultSetHandler<RESULT> resultSetHandler,
      Supplier<RESULT> emptyResultSupplier) {
    return new UpdateReturningCommand<>(query, resultSetHandler, emptyResultSupplier);
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
