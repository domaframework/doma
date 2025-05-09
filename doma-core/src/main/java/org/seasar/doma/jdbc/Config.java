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

import java.sql.PreparedStatement;
import java.sql.Statement;
import javax.sql.DataSource;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.statistic.StatisticManager;
import org.seasar.doma.jdbc.tx.TransactionManager;
import org.seasar.doma.message.Message;

/**
 * A runtime configuration for DAOs (Data Access Objects).
 *
 * <p>The implementation must be thread safe.
 *
 * <p>This interface provides configuration settings for database connections, SQL dialects, and
 * various other components used by the Doma framework. It serves as a central point for configuring
 * the behavior of Doma's JDBC operations, entity management, and SQL execution.
 *
 * <p>Implementations of this interface are typically provided by the application and injected into
 * DAO instances. The configuration can be customized by overriding the default methods in this
 * interface.
 *
 * <p>Most methods in this interface provide default implementations that return sensible defaults,
 * allowing applications to override only the settings they need to customize.
 */
public interface Config {

  /**
   * Returns the data source for database connections.
   *
   * <p>The data source is used to obtain database connections for executing SQL statements. It is a
   * fundamental component required for all database operations in Doma.
   *
   * @return the data source that provides database connections
   */
  DataSource getDataSource();

  /**
   * Returns the SQL dialect.
   *
   * <p>The SQL dialect provides database-specific functionality and SQL syntax adaptations for
   * different database management systems (such as MySQL, PostgreSQL, Oracle, etc.). It allows Doma
   * to generate appropriate SQL statements that are compatible with the target database.
   *
   * @return the SQL dialect implementation for the target database
   */
  Dialect getDialect();

  /**
   * Returns the name of the data source.
   *
   * <p>Each data source must have a unique name when multiple data sources are used in an
   * application. This name is used to identify the data source for distinguishing between different
   * database connections.
   *
   * <p>By default, this method returns the fully qualified class name of the Config implementation.
   * Override this method to provide a more descriptive name for the data source.
   *
   * @return the name of the data source that uniquely identifies it within the application
   */
  default String getDataSourceName() {
    return getClass().getName();
  }

  /**
   * Returns the SQL file repository.
   *
   * <p>The SQL file repository is responsible for loading and caching SQL files that contain SQL
   * statements used by DAOs. It manages the lifecycle of SQL templates and ensures efficient access
   * to SQL resources.
   *
   * <p>By default, this method returns a shared instance from {@link
   * ConfigSupport#defaultSqlFileRepository}. Override this method to provide a custom
   * implementation with different caching or loading strategies.
   *
   * @return the SQL file repository that manages SQL templates
   */
  default SqlFileRepository getSqlFileRepository() {
    return ConfigSupport.defaultSqlFileRepository;
  }

  /**
   * Returns the Script file loader.
   *
   * <p>The Script file loader is responsible for loading SQL script files that contain multiple SQL
   * statements for batch execution. These scripts are typically used for database initialization,
   * schema creation, or test data setup.
   *
   * <p>By default, this method returns a shared instance from {@link
   * ConfigSupport#defaultScriptFileLoader}. Override this method to provide a custom implementation
   * with different loading strategies or resource resolution mechanisms.
   *
   * @return the Script file loader that handles SQL script files
   */
  default ScriptFileLoader getScriptFileLoader() {
    return ConfigSupport.defaultScriptFileLoader;
  }

  /**
   * Returns the JDBC logger.
   *
   * <p>The JDBC logger is responsible for logging SQL statements, parameters, execution times, and
   * other JDBC-related operations. It provides visibility into database interactions performed by
   * Doma, which is essential for debugging and performance monitoring.
   *
   * <p>By default, this method returns a shared instance from {@link
   * ConfigSupport#defaultJdbcLogger}. Override this method to provide a custom implementation that
   * integrates with your application's logging framework or has different logging behavior.
   *
   * @return the JDBC logger that handles logging of SQL and JDBC operations
   * @see UtilLoggingJdbcLogger
   */
  default JdbcLogger getJdbcLogger() {
    return ConfigSupport.defaultJdbcLogger;
  }

  /**
   * Returns the transaction controller whose transaction attribute is {@code REQUIRES_NEW}.
   *
   * <p>The RequiresNewController is responsible for managing transactions with the REQUIRES_NEW
   * attribute, which means it always creates a new transaction regardless of whether a transaction
   * already exists. This is useful for operations that must be committed or rolled back
   * independently of the surrounding transaction context.
   *
   * <p>By default, this method returns a shared instance from {@link
   * ConfigSupport#defaultRequiresNewController}. Override this method to provide a custom
   * implementation that integrates with your application's transaction management system.
   *
   * @return the transaction controller that manages transactions with the REQUIRES_NEW attribute
   */
  default RequiresNewController getRequiresNewController() {
    return ConfigSupport.defaultRequiresNewController;
  }

  /**
   * Returns the class helper.
   *
   * <p>The ClassHelper is responsible for loading classes and creating instances of those classes.
   * It provides an abstraction layer over Java's reflection and class loading mechanisms, allowing
   * for customization of how classes are loaded and instantiated in different environments (such as
   * OSGi containers, application servers, or custom classloaders).
   *
   * <p>By default, this method returns a shared instance from {@link
   * ConfigSupport#defaultClassHelper}. Override this method to provide a custom implementation that
   * integrates with your application's class loading infrastructure.
   *
   * @return the class helper that handles class loading and instantiation
   */
  default ClassHelper getClassHelper() {
    return ConfigSupport.defaultClassHelper;
  }

  /**
   * Returns the factory for {@link Command} implementation classes.
   *
   * <p>The CommandImplementors factory creates implementations of the {@link Command} interface,
   * which are responsible for executing various database operations. These commands encapsulate the
   * logic for executing SQL statements, handling results, and managing resources.
   *
   * <p>By default, this method returns a shared instance from {@link
   * ConfigSupport#defaultCommandImplementors}. Override this method to provide custom
   * implementations of commands that modify the standard behavior of database operations or add
   * additional functionality such as metrics collection, custom error handling, or specialized
   * resource management.
   *
   * @return the factory that creates implementations of the {@link Command} interface
   */
  default CommandImplementors getCommandImplementors() {
    return ConfigSupport.defaultCommandImplementors;
  }

  /**
   * Returns the factory for {@link Query} implementation classes.
   *
   * <p>The QueryImplementors factory creates implementations of the {@link Query} interface, which
   * are responsible for building and preparing SQL statements for execution. These queries handle
   * parameter binding, SQL transformation, and preparation of execution context.
   *
   * <p>By default, this method returns a shared instance from {@link
   * ConfigSupport#defaultQueryImplementors}. Override this method to provide custom implementations
   * of queries that modify how SQL statements are built, transformed, or prepared. This can be
   * useful for implementing features like query rewriting, additional validation, or specialized
   * parameter handling.
   *
   * @return the factory that creates implementations of the {@link Query} interface
   */
  default QueryImplementors getQueryImplementors() {
    return ConfigSupport.defaultQueryImplementors;
  }

  /**
   * Returns the SQL log type that determines the SQL log format in exceptions.
   *
   * <p>The SQL log type controls how SQL statements are formatted and displayed in exception
   * messages when database errors occur. This affects the readability and detail level of
   * SQL-related information in error logs and stack traces.
   *
   * <p>By default, this method returns {@link SqlLogType#FORMATTED}, which provides a
   * well-formatted SQL representation with parameter values. Other options include {@link
   * SqlLogType#RAW} for raw SQL with bind variables and {@link SqlLogType#NONE} to omit SQL from
   * exception messages.
   *
   * <p>Override this method to change the default SQL logging behavior for exceptions throughout
   * your application.
   *
   * @return the SQL log type that controls SQL formatting in exception messages
   */
  default SqlLogType getExceptionSqlLogType() {
    return SqlLogType.FORMATTED;
  }

  /**
   * Returns the unknown column handler.
   *
   * <p>The UnknownColumnHandler determines how Doma responds when it encounters columns in query
   * results that don't match any fields in the corresponding entity class. This can happen when the
   * database schema changes or when using queries that return columns not mapped to entity fields.
   *
   * <p>By default, this method returns a shared instance from {@link
   * ConfigSupport#defaultUnknownColumnHandler}, which throws an exception when unknown columns are
   * encountered. Alternative implementations might ignore unknown columns or log warnings about
   * unmapped columns instead of throwing exceptions.
   *
   * <p>Override this method to provide a custom implementation with different behavior for handling
   * unknown columns in query results.
   *
   * @return the handler that determines the behavior when unknown columns are encountered
   */
  default UnknownColumnHandler getUnknownColumnHandler() {
    return ConfigSupport.defaultUnknownColumnHandler;
  }

  /**
   * Returns the duplicate column handler.
   *
   * <p>The DuplicateColumnHandler determines how Doma responds when it encounters multiple columns
   * with the same name in query results. This can happen with SQL queries that join multiple tables
   * without using column aliases, or when using database views with ambiguous column names.
   *
   * <p>By default, this method returns a shared instance from {@link
   * ConfigSupport#defaultDuplicateColumnHandler}, which typically uses the last occurrence of a
   * duplicate column and ignores previous ones. Alternative implementations might throw exceptions
   * to alert developers about potential query issues or handle duplicates in a specific way based
   * on application requirements.
   *
   * <p>Override this method to provide a custom implementation with different behavior for handling
   * duplicate columns in query results.
   *
   * @return the handler that determines the behavior when duplicate columns are encountered
   */
  default DuplicateColumnHandler getDuplicateColumnHandler() {
    return ConfigSupport.defaultDuplicateColumnHandler;
  }

  /**
   * Returns the naming convention controller.
   *
   * <p>The Naming controller determines the strategy for converting between Java entity property
   * names and database column names. It provides a consistent way to handle different naming
   * conventions between Java (typically camelCase) and databases (often snake_case or other
   * conventions).
   *
   * <p>By default, this method returns a shared instance from {@link ConfigSupport#defaultNaming},
   * which is set to {@link Naming#DEFAULT} (equivalent to {@link Naming#NONE}). This implementation
   * performs no name conversion, preserving the original names exactly as they are defined in Java.
   * Other options include SNAKE_LOWER_CASE, SNAKE_UPPER_CASE, or others defined in the {@link
   * Naming} enum.
   *
   * <p>Override this method to provide a different naming convention that matches your database
   * schema design or organizational standards.
   *
   * @return the naming convention controller that handles name conversions between Java and the
   *     database
   */
  default Naming getNaming() {
    return ConfigSupport.defaultNaming;
  }

  /**
   * Returns a naming convention controller for keys contained in a {@code Map<String, Object>}
   * object.
   *
   * <p>The MapKeyNaming controller determines the strategy for converting between database column
   * names and keys in Map objects when query results are mapped to {@code Map<String, Object>}
   * instances. This is particularly useful when working with dynamic queries where the result
   * structure is not known at compile time.
   *
   * <p>By default, this method returns a shared instance from {@link
   * ConfigSupport#defaultMapKeyNaming}, which typically applies a specific naming convention to map
   * database column names to map keys. For example, it might convert database column names like
   * "USER_ID" to map keys like "userId" or keep them as-is depending on the configuration.
   *
   * <p>Override this method to provide a different naming convention for map keys that aligns with
   * your application's conventions for handling dynamic query results.
   *
   * @return a naming convention controller that handles conversions between database column names
   *     and keys in {@code Map<String, Object>} objects
   */
  default MapKeyNaming getMapKeyNaming() {
    return ConfigSupport.defaultMapKeyNaming;
  }

  /**
   * Returns the transaction manager.
   *
   * <p>The TransactionManager is responsible for managing database transactions, including
   * beginning, committing, and rolling back transactions. It provides a consistent API for
   * transaction management across different environments and transaction implementations.
   *
   * <p>By default, this method throws an {@link UnsupportedOperationException}, indicating that
   * transaction management is not supported by the default configuration. Implementations of this
   * interface must override this method to provide transaction support.
   *
   * <p>When implementing this method, consider integrating with your application's existing
   * transaction management system, such as Spring's transaction manager, Jakarta Transactions
   * (JTA), or a custom transaction management solution.
   *
   * @return the transaction manager that handles database transactions
   * @throws UnsupportedOperationException if this configuration does not support transactions by
   *     the transaction manager
   */
  default TransactionManager getTransactionManager() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the commenter for SQL strings.
   *
   * <p>The Commenter is responsible for adding comments to SQL statements before they are executed.
   * These comments can include information such as the origin of the SQL (e.g., method name, file
   * name), execution context, or application-specific information that helps with debugging and
   * monitoring database operations.
   *
   * <p>By default, this method returns a shared instance from {@link
   * ConfigSupport#defaultCommenter}, which typically adds no comments to SQL statements. Custom
   * implementations can add more detailed comments based on application requirements.
   *
   * <p>SQL comments are particularly useful when analyzing database performance, auditing SQL
   * execution, or troubleshooting issues in production environments where the source of a query
   * might not be immediately obvious.
   *
   * @return the commenter that adds comments to SQL statements before execution
   */
  default Commenter getCommenter() {
    return ConfigSupport.defaultCommenter;
  }

  /**
   * Returns the maximum number of rows for a {@code ResultSet} object.
   *
   * <p>This setting limits the maximum number of rows that can be returned by a query, which can be
   * useful for preventing excessive memory usage or improving performance when only a subset of
   * results is needed. When set, this value is passed to {@link Statement#setMaxRows(int)} for all
   * statements created by Doma.
   *
   * <p>If the value is greater than or equal to 1, it is applied as a limit. If the value is 0 (the
   * default), no limit is applied, and all rows matching the query criteria will be returned.
   *
   * <p>Note that this is a global setting that affects all queries executed through this
   * configuration. For query-specific limits, consider using SQL-level pagination or limit clauses
   * instead.
   *
   * @return the maximum number of rows to return in query results (0 means no limit)
   * @see Statement#setMaxRows(int)
   */
  default int getMaxRows() {
    return 0;
  }

  /**
   * Returns the fetch size.
   *
   * <p>The fetch size is a hint to the JDBC driver about how many rows should be fetched from the
   * database when more rows are needed for a result set. It affects the network traffic between the
   * application and the database by controlling how many rows are retrieved in each database
   * round-trip.
   *
   * <p>If the value is greater than or equal to 1, it is passed to {@link
   * Statement#setFetchSize(int)} for all statements created by Doma. If the value is 0 (the
   * default), the JDBC driver's default fetch size is used.
   *
   * <p>Setting an appropriate fetch size can significantly improve performance for queries that
   * return large result sets. A larger fetch size reduces the number of round-trips but requires
   * more memory. A smaller fetch size uses less memory but may require more round-trips to the
   * database.
   *
   * @return the fetch size hint for result set retrieval (0 means use the driver's default)
   * @see Statement#setFetchSize(int)
   */
  default int getFetchSize() {
    return 0;
  }

  /**
   * Returns the query timeout limit in seconds.
   *
   * <p>The query timeout specifies the maximum time in seconds that a JDBC driver will wait for a
   * statement to execute before canceling it. This helps prevent long-running queries from
   * consuming excessive resources or blocking application threads indefinitely.
   *
   * <p>If the value is greater than or equal to 1, it is passed to {@link
   * Statement#setQueryTimeout(int)} for all statements created by Doma. If the value is 0 (the
   * default), no timeout is set, and queries can run for an unlimited amount of time.
   *
   * <p>Setting an appropriate timeout is particularly important for applications that need to
   * maintain responsiveness even when database operations take longer than expected. When a timeout
   * occurs, the statement is canceled and an exception is thrown.
   *
   * <p>Note that not all JDBC drivers or database systems support query timeouts. Check your
   * specific database documentation for compatibility information.
   *
   * @return the query timeout limit in seconds (0 means no timeout)
   * @see Statement#setQueryTimeout(int)
   */
  default int getQueryTimeout() {
    return 0;
  }

  /**
   * Returns the batch size.
   *
   * <p>The batch size determines how many SQL statements are grouped together and sent to the
   * database in a single batch execution. Batch processing can significantly improve performance
   * for operations that need to execute the same SQL statement multiple times with different
   * parameter values (such as bulk inserts, updates, or deletes).
   *
   * <p>If the value is greater than or equal to 1, Doma will accumulate that many statements before
   * calling {@link PreparedStatement#executeBatch()}. If the value is less than 1, it is treated as
   * 1, meaning each statement will be executed individually without batching. A value of 0 (the
   * default) also disables batching.
   *
   * <p>Larger batch sizes generally improve performance by reducing the number of round-trips to
   * the database, but they also increase memory usage and may delay error detection until the batch
   * is executed.
   *
   * @return the batch size for grouping SQL statements (0 or negative values disable batching)
   * @see PreparedStatement#executeBatch()
   * @see PreparedStatement#addBatch()
   */
  default int getBatchSize() {
    return 0;
  }

  /**
   * Returns the provider for {@link EntityListener}.
   *
   * <p>The EntityListenerProvider is responsible for creating and managing instances of {@link
   * EntityListener} implementations. Entity listeners provide callback methods that are invoked
   * during entity lifecycle events, such as before/after insert, update, or delete operations. This
   * allows for implementing cross-cutting concerns like validation, auditing, or event
   * notification.
   *
   * <p>By default, this method returns a shared instance from {@link
   * ConfigSupport#defaultEntityListenerProvider}, which creates one instance of each listener class
   * per entity class. Custom implementations might integrate with dependency injection frameworks
   * or provide pooling of listener instances.
   *
   * <p>Override this method to provide a custom implementation that integrates with your
   * application's dependency injection or component management system.
   *
   * @return the provider that creates and manages {@link EntityListener} instances
   */
  default EntityListenerProvider getEntityListenerProvider() {
    return ConfigSupport.defaultEntityListenerProvider;
  }

  /**
   * Returns the context for SQL builder settings.
   *
   * <p>The SqlBuilderSettings provides configuration options for the SQL builders used by Doma to
   * generate SQL statements. These settings control aspects such as SQL formatting and other SQL
   * generation behaviors.
   *
   * <p>By default, this method returns a shared instance from {@link
   * ConfigSupport#defaultSqlBuilderSettings}, which applies standard SQL building rules. Custom
   * implementations might modify these rules to accommodate specific database requirements or
   * organizational SQL standards.
   *
   * @return the context that provides configuration options for SQL generation
   */
  default SqlBuilderSettings getSqlBuilderSettings() {
    return ConfigSupport.defaultSqlBuilderSettings;
  }

  /**
   * Returns the statistic manager instance.
   *
   * <p>The StatisticManager collects and manages statistics about SQL execution, such as execution
   * counts and execution times. These statistics can be valuable for monitoring application
   * performance, identifying bottlenecks, and diagnosing database-related issues.
   *
   * <p>By default, this method returns a shared instance from {@link
   * ConfigSupport#defaultStatisticManager}, which may or may not collect statistics depending on
   * its configuration. Custom implementations might integrate with application monitoring systems,
   * performance tracking tools, or provide more detailed statistics collection.
   *
   * <p>Statistics collection can have a small performance impact, so it's typically configurable to
   * enable or disable collection based on the environment (e.g., enabled in development and
   * testing, but disabled or sampling-based in production).
   *
   * @return the {@link StatisticManager} instance that collects and manages SQL execution
   *     statistics
   */
  default StatisticManager getStatisticManager() {
    return ConfigSupport.defaultStatisticManager;
  }

  /**
   * Retrieves a {@link Config} object from the {@code provider} parameter.
   *
   * <p>This static utility method is used to obtain a Config instance from an object that
   * implements the {@link ConfigProvider} interface. It's commonly used to extract configuration
   * settings from DAO instances.
   *
   * <p>The method performs a type check to ensure that the provided object implements {@link
   * ConfigProvider}, and throws an exception if it doesn't.
   *
   * @param provider the instance of {@link ConfigProvider} from which to retrieve the configuration
   * @return the configuration instance associated with the provider
   * @throws DomaIllegalArgumentException if {@code provider} does not implement {@link
   *     ConfigProvider}
   */
  static Config get(Object provider) {
    if (provider instanceof ConfigProvider) {
      ConfigProvider p = (ConfigProvider) provider;
      return p.getConfig();
    }
    throw new DomaIllegalArgumentException(
        "provider", Message.DOMA2218.getMessage("provider", ConfigProvider.class.getName()));
  }
}
