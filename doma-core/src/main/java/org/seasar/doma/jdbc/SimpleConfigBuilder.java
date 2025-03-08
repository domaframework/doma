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
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import org.seasar.doma.jdbc.dialect.Db2Dialect;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.dialect.HsqldbDialect;
import org.seasar.doma.jdbc.dialect.MssqlDialect;
import org.seasar.doma.jdbc.dialect.MysqlDialect;
import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.SqliteDialect;
import org.seasar.doma.jdbc.statistic.StatisticManager;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;

/**
 * A builder interface for creating a {@link SimpleConfig} instance with custom configuration
 * settings.
 *
 * <p>This builder provides a fluent API for specifying various settings required for database
 * interaction such as dialect, logging behavior, transaction management, SQL behavior, entity
 * listeners, and more. It allows customization of parameters such as fetch size, query timeout, and
 * batch size, among others.
 */
public interface SimpleConfigBuilder {

  SimpleConfigBuilder dialect(Dialect dialect);

  SimpleConfigBuilder jdbcLogger(JdbcLogger jdbcLogger);

  SimpleConfigBuilder sqlFileRepository(SqlFileRepository sqlFileRepository);

  SimpleConfigBuilder scriptFileLoader(ScriptFileLoader scriptFileLoader);

  SimpleConfigBuilder classHelper(ClassHelper classHelper);

  SimpleConfigBuilder commandImplementors(CommandImplementors commandImplementors);

  SimpleConfigBuilder queryImplementors(QueryImplementors queryImplementors);

  SimpleConfigBuilder exceptionSqlLogType(SqlLogType exceptionSqlLogType);

  SimpleConfigBuilder unknownColumnHandler(UnknownColumnHandler unknownColumnHandler);

  SimpleConfigBuilder duplicateColumnHandler(DuplicateColumnHandler duplicateColumnHandler);

  SimpleConfigBuilder naming(Naming naming);

  SimpleConfigBuilder mapKeyNaming(MapKeyNaming mapKeyNaming);

  SimpleConfigBuilder transactionManagerFactory(
      BiFunction<LocalTransactionDataSource, JdbcLogger, LocalTransactionManager>
          transactionManagerFactory);

  SimpleConfigBuilder commenter(Commenter commenter);

  SimpleConfigBuilder entityListenerProvider(EntityListenerProvider entityListenerProvider);

  SimpleConfigBuilder sqlBuilderSettings(SqlBuilderSettings sqlBuilderSettings);

  SimpleConfigBuilder statisticManager(StatisticManager statisticManager);

  SimpleConfigBuilder maxRows(int maxRows);

  SimpleConfigBuilder fetchSize(int fetchSize);

  /**
   * Sets the query timeout duration for SQL queries.
   *
   * @param queryTimeout the query timeout in seconds
   * @return this builder instance for method chaining
   */
  SimpleConfigBuilder queryTimeout(int queryTimeout);

  SimpleConfigBuilder batchSize(int batchSize);

  /**
   * Builds and returns an instance of {@code SimpleConfig} based on the current builder
   * configuration.
   *
   * @return a new {@code SimpleConfig} instance configured with the specified settings
   */
  SimpleConfig build();
}

class SimpleConfigBuilderImpl implements SimpleConfigBuilder {
  private static final Pattern jdbcUrlPattern = Pattern.compile("^jdbc:(tc:)?([^:]*):.*");

  private final LocalTransactionDataSource dataSource;
  private Dialect dialect;
  private BiFunction<LocalTransactionDataSource, JdbcLogger, LocalTransactionManager>
      transactionManagerFactory;
  private SqlFileRepository sqlFileRepository = ConfigSupport.defaultSqlFileRepository;
  private ScriptFileLoader scriptFileLoader = ConfigSupport.defaultScriptFileLoader;
  private JdbcLogger jdbcLogger = ConfigSupport.defaultJdbcLogger;
  private ClassHelper classHelper = ConfigSupport.defaultClassHelper;
  private CommandImplementors commandImplementors = ConfigSupport.defaultCommandImplementors;
  private QueryImplementors queryImplementors = ConfigSupport.defaultQueryImplementors;
  private SqlLogType exceptionSqlLogType = SqlLogType.FORMATTED;
  private UnknownColumnHandler unknownColumnHandler = ConfigSupport.defaultUnknownColumnHandler;
  private DuplicateColumnHandler duplicateColumnHandler =
      ConfigSupport.defaultDuplicateColumnHandler;
  private Naming naming = ConfigSupport.defaultNaming;
  private MapKeyNaming mapKeyNaming = ConfigSupport.defaultMapKeyNaming;
  private Commenter commenter = ConfigSupport.defaultCommenter;
  private EntityListenerProvider entityListenerProvider =
      ConfigSupport.defaultEntityListenerProvider;
  private SqlBuilderSettings sqlBuilderSettings = new SimpleSqlBuilderSettings();
  private StatisticManager statisticManager = ConfigSupport.defaultStatisticManager;
  int maxRows = 0;
  int fetchSize = 0;
  int queryTimeout = 0;
  int batchSize = 0;

  private SimpleConfigBuilderImpl(
      LocalTransactionDataSource dataSource,
      Dialect dialect,
      BiFunction<LocalTransactionDataSource, JdbcLogger, LocalTransactionManager>
          transactionManagerFactory) {
    this.dataSource = Objects.requireNonNull(dataSource);
    this.dialect = Objects.requireNonNull(dialect);
    this.transactionManagerFactory = Objects.requireNonNull(transactionManagerFactory);
  }

  @Override
  public SimpleConfigBuilderImpl dialect(Dialect dialect) {
    this.dialect = Objects.requireNonNull(dialect);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl jdbcLogger(JdbcLogger jdbcLogger) {
    this.jdbcLogger = Objects.requireNonNull(jdbcLogger);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl sqlFileRepository(SqlFileRepository sqlFileRepository) {
    this.sqlFileRepository = Objects.requireNonNull(sqlFileRepository);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl scriptFileLoader(ScriptFileLoader scriptFileLoader) {
    this.scriptFileLoader = Objects.requireNonNull(scriptFileLoader);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl classHelper(ClassHelper classHelper) {
    this.classHelper = Objects.requireNonNull(classHelper);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl commandImplementors(CommandImplementors commandImplementors) {
    this.commandImplementors = Objects.requireNonNull(commandImplementors);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl queryImplementors(QueryImplementors queryImplementors) {
    this.queryImplementors = Objects.requireNonNull(queryImplementors);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl exceptionSqlLogType(SqlLogType exceptionSqlLogType) {
    this.exceptionSqlLogType = Objects.requireNonNull(exceptionSqlLogType);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl unknownColumnHandler(UnknownColumnHandler unknownColumnHandler) {
    this.unknownColumnHandler = Objects.requireNonNull(unknownColumnHandler);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl duplicateColumnHandler(
      DuplicateColumnHandler duplicateColumnHandler) {
    this.duplicateColumnHandler = Objects.requireNonNull(duplicateColumnHandler);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl naming(Naming naming) {
    this.naming = Objects.requireNonNull(naming);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl mapKeyNaming(MapKeyNaming mapKeyNaming) {
    this.mapKeyNaming = Objects.requireNonNull(mapKeyNaming);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl transactionManagerFactory(
      BiFunction<LocalTransactionDataSource, JdbcLogger, LocalTransactionManager>
          transactionManagerFactory) {
    this.transactionManagerFactory = Objects.requireNonNull(transactionManagerFactory);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl commenter(Commenter commenter) {
    this.commenter = Objects.requireNonNull(commenter);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl entityListenerProvider(
      EntityListenerProvider entityListenerProvider) {
    this.entityListenerProvider = Objects.requireNonNull(entityListenerProvider);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl sqlBuilderSettings(SqlBuilderSettings sqlBuilderSettings) {
    this.sqlBuilderSettings = Objects.requireNonNull(sqlBuilderSettings);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl statisticManager(StatisticManager statisticManager) {
    this.statisticManager = Objects.requireNonNull(statisticManager);
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl maxRows(int maxRows) {
    this.maxRows = maxRows;
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl fetchSize(int fetchSize) {
    this.fetchSize = fetchSize;
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl queryTimeout(int queryTimeout) {
    this.queryTimeout = queryTimeout;
    return this;
  }

  @Override
  public SimpleConfigBuilderImpl batchSize(int batchSize) {
    this.batchSize = batchSize;
    return this;
  }

  @Override
  public SimpleConfig build() {
    var transactionManager = transactionManagerFactory.apply(dataSource, jdbcLogger);
    var requiresNewController =
        new RequiresNewController() {
          @Override
          public <R> R requiresNew(Callback<R> callback) throws Throwable {
            return transactionManager.requiresNew(callback::execute);
          }
        };
    var dataSourceName = UUID.randomUUID().toString();
    return new SimpleConfigImpl(
        dataSource,
        dataSourceName,
        dialect,
        sqlFileRepository,
        scriptFileLoader,
        jdbcLogger,
        requiresNewController,
        classHelper,
        commandImplementors,
        queryImplementors,
        exceptionSqlLogType,
        unknownColumnHandler,
        duplicateColumnHandler,
        naming,
        mapKeyNaming,
        transactionManager,
        commenter,
        entityListenerProvider,
        sqlBuilderSettings,
        statisticManager,
        maxRows,
        fetchSize,
        queryTimeout,
        batchSize);
  }

  static SimpleConfigBuilderImpl of(String url) {
    Objects.requireNonNull(url);
    return of(url, null, null);
  }

  static SimpleConfigBuilderImpl of(String url, String user, String password) {
    Objects.requireNonNull(url);
    var driver = extractJdbcDriver(url);
    if (driver == null) {
      throw new IllegalArgumentException(
          "Cannot identify the JDBC driver from the URL. url=" + url);
    }
    var dialect = inferDialect(driver);
    if (dialect == null) {
      throw new IllegalArgumentException(
          "Cannot infer the Dialect from the URL. url=" + url + ", driver=" + driver);
    }
    var dataSource = new LocalTransactionDataSource(url, user, password);
    return of(dataSource, dialect);
  }

  static SimpleConfigBuilderImpl of(DataSource dataSource, Dialect dialect) {
    Objects.requireNonNull(dataSource);
    Objects.requireNonNull(dialect);
    LocalTransactionDataSource localTransactionDataSource;
    if (dataSource instanceof LocalTransactionDataSource ltd) {
      localTransactionDataSource = ltd;
    } else {
      localTransactionDataSource = new LocalTransactionDataSource(dataSource);
    }
    return new SimpleConfigBuilderImpl(
        localTransactionDataSource, dialect, LocalTransactionManager::new);
  }

  static String extractJdbcDriver(String url) {
    var matcher = jdbcUrlPattern.matcher(url);
    if (matcher.matches()) {
      return matcher.group(2).toLowerCase();
    }
    return null;
  }

  static Dialect inferDialect(String driver) {
    return switch (driver) {
      case "db2" -> new Db2Dialect();
      case "h2" -> new H2Dialect();
      case "mysql" -> new MysqlDialect(MysqlDialect.MySqlVersion.V8);
      case "oracle" -> new OracleDialect();
      case "postgresql" -> new PostgresDialect();
      case "sqlite" -> new SqliteDialect();
      case "sqlserver" -> new MssqlDialect();
      case "hsqldb" -> new HsqldbDialect();
      default -> null;
    };
  }
}

class SimpleSqlBuilderSettings implements SqlBuilderSettings {
  @Override
  public boolean shouldRemoveBlankLines() {
    return true;
  }
}
