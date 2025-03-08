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

import java.util.Objects;
import javax.sql.DataSource;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.statistic.StatisticManager;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;
import org.seasar.doma.jdbc.tx.TransactionManager;

/**
 * Represents a simplified configuration interface that extends the base {@code Config} interface.
 * It provides methods for accessing specific configuration components and constructing builder
 * instances for creating configuration objects.
 *
 * <pre>
 *     SimpleConfig config = SimpleConfig.builder("jdbc:h2:mem:test")
 *       .naming(Naming.SNAKE_UPPER_CASE)
 *       .queryTimeout(10)
 *       .build();
 * </pre>
 *
 * This configuration uses Doma's local transaction manager. If you're using Doma with Spring
 * Framework or Quarkus, avoid using this class.
 */
public interface SimpleConfig extends Config {

  /**
   * Retrieves the {@code LocalTransactionDataSource} associated with this configuration.
   *
   * @return an instance of {@code LocalTransactionDataSource} for managing local database
   *     connections and transactions
   */
  LocalTransactionDataSource getLocalTransactionDataSource();

  /**
   * Retrieves the {@code LocalTransactionManager} associated with this configuration.
   *
   * @return an instance of {@code LocalTransactionManager} for managing local transactions
   */
  LocalTransactionManager getLocalTransactionManager();

  /**
   * Creates a new instance of {@code SimpleConfigBuilder} with the specified URL.
   *
   * <p>You can use a specially modified JDBC URL for Testcontainers as follows:
   *
   * <pre>
   *     jdbc:tc:postgresql:12.20:///test?TC_DAEMON=true
   * </pre>
   *
   * The {@link Dialect} is inferred from the URL information.
   *
   * @param url the database URL to be used for the configuration; must not be {@code null}
   * @return an instance of {@code SimpleConfigBuilder} initialized with the given URL
   * @throws NullPointerException if the URL is {@code null}
   */
  static SimpleConfigBuilder builder(String url) {
    Objects.requireNonNull(url);
    return SimpleConfigBuilderImpl.of(url);
  }

  /**
   * Creates a new instance of {@code SimpleConfigBuilder} with the specified database URL,
   * username, and password.
   *
   * <p>You can use a specially modified JDBC URL for Testcontainers as follows:
   *
   * <pre>
   *     jdbc:tc:postgresql:12.20:///test?TC_DAEMON=true
   * </pre>
   *
   * The {@link Dialect} is inferred from the URL information.
   *
   * @param url the database URL to be used for the configuration; must not be {@code null}
   * @param user the username to be used for the database connection; can be {@code null}
   * @param password the password to be used for the database connection; can be {@code null}
   * @return an instance of {@code SimpleConfigBuilder} initialized with the specified parameters
   * @throws NullPointerException if the URL is {@code null}
   */
  static SimpleConfigBuilder builder(String url, String user, String password) {
    Objects.requireNonNull(url);
    return SimpleConfigBuilderImpl.of(url, user, password);
  }

  /**
   * Creates a new instance of {@code SimpleConfigBuilder} with the specified {@code DataSource} and
   * {@code Dialect}.
   *
   * @param dataSource the data source to be used for the configuration; must not be {@code null}
   * @param dialect the dialect to be used for the configuration; must not be {@code null}
   * @return an instance of {@code SimpleConfigBuilder} initialized with the provided {@code
   *     DataSource} and {@code Dialect}
   * @throws NullPointerException if the {@code dataSource} or {@code dialect} is {@code null}
   */
  static SimpleConfigBuilder builder(DataSource dataSource, Dialect dialect) {
    Objects.requireNonNull(dataSource);
    Objects.requireNonNull(dialect);
    return SimpleConfigBuilderImpl.of(dataSource, dialect);
  }
}

class SimpleConfigImpl implements Config, SimpleConfig {

  private final LocalTransactionDataSource dataSource;
  private final String dataSourceName;
  private final Dialect dialect;
  private final SqlFileRepository sqlFileRepository;
  private final ScriptFileLoader scriptFileLoader;
  private final JdbcLogger jdbcLogger;
  private final RequiresNewController requiresNewController;
  private final ClassHelper classHelper;
  private final CommandImplementors commandImplementors;
  private final QueryImplementors queryImplementors;
  private final SqlLogType exceptionSqlLogType;
  private final UnknownColumnHandler unknownColumnHandler;
  private final DuplicateColumnHandler duplicateColumnHandler;
  private final Naming naming;
  private final MapKeyNaming mapKeyNaming;
  private final LocalTransactionManager transactionManager;
  private final Commenter commenter;
  private final EntityListenerProvider entityListenerProvider;
  private final SqlBuilderSettings sqlBuilderSettings;
  private final StatisticManager statisticManager;
  private final int maxRows;
  private final int fetchSize;
  private final int queryTimeout;
  private final int batchSize;

  SimpleConfigImpl(
      LocalTransactionDataSource dataSource,
      String dataSourceName,
      Dialect dialect,
      SqlFileRepository sqlFileRepository,
      ScriptFileLoader scriptFileLoader,
      JdbcLogger jdbcLogger,
      RequiresNewController requiresNewController,
      ClassHelper classHelper,
      CommandImplementors commandImplementors,
      QueryImplementors queryImplementors,
      SqlLogType exceptionSqlLogType,
      UnknownColumnHandler unknownColumnHandler,
      DuplicateColumnHandler duplicateColumnHandler,
      Naming naming,
      MapKeyNaming mapKeyNaming,
      LocalTransactionManager transactionManager,
      Commenter commenter,
      EntityListenerProvider entityListenerProvider,
      SqlBuilderSettings sqlBuilderSettings,
      StatisticManager statisticManager,
      int maxRows,
      int fetchSize,
      int queryTimeout,
      int batchSize) {
    this.dataSource = Objects.requireNonNull(dataSource);
    this.dataSourceName = Objects.requireNonNull(dataSourceName);
    this.dialect = Objects.requireNonNull(dialect);
    this.sqlFileRepository = Objects.requireNonNull(sqlFileRepository);
    this.scriptFileLoader = Objects.requireNonNull(scriptFileLoader);
    this.jdbcLogger = Objects.requireNonNull(jdbcLogger);
    this.requiresNewController = Objects.requireNonNull(requiresNewController);
    this.classHelper = Objects.requireNonNull(classHelper);
    this.commandImplementors = Objects.requireNonNull(commandImplementors);
    this.queryImplementors = Objects.requireNonNull(queryImplementors);
    this.exceptionSqlLogType = Objects.requireNonNull(exceptionSqlLogType);
    this.unknownColumnHandler = Objects.requireNonNull(unknownColumnHandler);
    this.duplicateColumnHandler = Objects.requireNonNull(duplicateColumnHandler);
    this.naming = Objects.requireNonNull(naming);
    this.mapKeyNaming = Objects.requireNonNull(mapKeyNaming);
    this.transactionManager = Objects.requireNonNull(transactionManager);
    this.commenter = Objects.requireNonNull(commenter);
    this.entityListenerProvider = Objects.requireNonNull(entityListenerProvider);
    this.sqlBuilderSettings = Objects.requireNonNull(sqlBuilderSettings);
    this.statisticManager = Objects.requireNonNull(statisticManager);
    this.maxRows = maxRows;
    this.fetchSize = fetchSize;
    this.queryTimeout = queryTimeout;
    this.batchSize = batchSize;
  }

  @Override
  public DataSource getDataSource() {
    return dataSource;
  }

  @Override
  public String getDataSourceName() {
    return dataSourceName;
  }

  @Override
  public Dialect getDialect() {
    return dialect;
  }

  @Override
  public SqlFileRepository getSqlFileRepository() {
    return sqlFileRepository;
  }

  @Override
  public ScriptFileLoader getScriptFileLoader() {
    return scriptFileLoader;
  }

  @Override
  public JdbcLogger getJdbcLogger() {
    return jdbcLogger;
  }

  @Override
  public RequiresNewController getRequiresNewController() {
    return requiresNewController;
  }

  @Override
  public ClassHelper getClassHelper() {
    return classHelper;
  }

  @Override
  public CommandImplementors getCommandImplementors() {
    return commandImplementors;
  }

  @Override
  public QueryImplementors getQueryImplementors() {
    return queryImplementors;
  }

  @Override
  public SqlLogType getExceptionSqlLogType() {
    return exceptionSqlLogType;
  }

  @Override
  public UnknownColumnHandler getUnknownColumnHandler() {
    return unknownColumnHandler;
  }

  @Override
  public DuplicateColumnHandler getDuplicateColumnHandler() {
    return duplicateColumnHandler;
  }

  @Override
  public Naming getNaming() {
    return naming;
  }

  @Override
  public MapKeyNaming getMapKeyNaming() {
    return mapKeyNaming;
  }

  @Override
  public TransactionManager getTransactionManager() {
    return transactionManager;
  }

  @Override
  public Commenter getCommenter() {
    return commenter;
  }

  @Override
  public EntityListenerProvider getEntityListenerProvider() {
    return entityListenerProvider;
  }

  @Override
  public SqlBuilderSettings getSqlBuilderSettings() {
    return sqlBuilderSettings;
  }

  @Override
  public StatisticManager getStatisticManager() {
    return statisticManager;
  }

  @Override
  public int getMaxRows() {
    return maxRows;
  }

  @Override
  public int getFetchSize() {
    return fetchSize;
  }

  @Override
  public int getQueryTimeout() {
    return queryTimeout;
  }

  @Override
  public int getBatchSize() {
    return batchSize;
  }

  @Override
  public LocalTransactionDataSource getLocalTransactionDataSource() {
    return dataSource;
  }

  @Override
  public LocalTransactionManager getLocalTransactionManager() {
    return transactionManager;
  }
}
