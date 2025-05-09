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

import org.seasar.doma.jdbc.statistic.DefaultStatisticManager;
import org.seasar.doma.jdbc.statistic.StatisticManager;

/**
 * Provides default implementations for the {@link Config} interface.
 *
 * <p>This class serves as a central repository of default implementations for various components
 * used by the Doma framework. These default implementations are used when a custom {@link Config}
 * implementation does not override the corresponding methods.
 *
 * <p>Each field in this class represents a default implementation of a specific interface or
 * component that can be used by {@link Config} implementations. The default implementations provide
 * reasonable behavior for most applications, but can be replaced with custom implementations when
 * needed.
 *
 * <p>For example, the {@link Config#getSqlFileRepository()} method returns {@link
 * #defaultSqlFileRepository} by default, but a custom {@link Config} implementation can override
 * this method to provide a different {@link SqlFileRepository} implementation.
 *
 * <p>This design allows applications to customize only the components they need while using the
 * default implementations for everything else, promoting a flexible and modular architecture.
 *
 * <p>All fields in this class are public, static, and final, making them effectively constants that
 * can be referenced directly if needed, though they are typically accessed through the default
 * methods of the {@link Config} interface.
 */
public final class ConfigSupport {

  /**
   * Default implementation of {@link SqlFileRepository} that caches SQL files aggressively.
   *
   * <p>This implementation uses a {@link GreedyCacheSqlFileRepository} which loads and caches SQL
   * files when they are first accessed. The cached SQL files are kept in memory for the lifetime of
   * the application, which improves performance but increases memory usage.
   *
   * <p>Used by {@link Config#getSqlFileRepository()} as the default return value.
   */
  public static final SqlFileRepository defaultSqlFileRepository =
      new GreedyCacheSqlFileRepository();

  /**
   * Default implementation of {@link ScriptFileLoader} that provides basic script loading
   * capabilities.
   *
   * <p>This implementation uses an anonymous inner class of {@link ScriptFileLoader} with default
   * behavior for loading SQL script files.
   *
   * <p>Used by {@link Config#getScriptFileLoader()} as the default return value.
   */
  public static final ScriptFileLoader defaultScriptFileLoader = new ScriptFileLoader() {};

  /**
   * Default implementation of {@link JdbcLogger} that logs using java.util.logging.
   *
   * <p>This implementation uses {@link UtilLoggingJdbcLogger} which delegates to the standard Java
   * logging framework (java.util.logging) for logging SQL statements, parameters, and execution
   * times.
   *
   * <p>Used by {@link Config#getJdbcLogger()} as the default return value.
   */
  public static final JdbcLogger defaultJdbcLogger = new UtilLoggingJdbcLogger();

  /**
   * Default implementation of {@link RequiresNewController} for transaction control.
   *
   * <p>This implementation uses an anonymous inner class of {@link RequiresNewController} with
   * default behavior for managing transactions with the REQUIRES_NEW attribute.
   *
   * <p>Used by {@link Config#getRequiresNewController()} as the default return value.
   */
  public static final RequiresNewController defaultRequiresNewController =
      new RequiresNewController() {};

  /**
   * Default implementation of {@link ClassHelper} for class loading and instantiation.
   *
   * <p>This implementation uses an anonymous inner class of {@link ClassHelper} with default
   * behavior for loading classes and creating instances.
   *
   * <p>Used by {@link Config#getClassHelper()} as the default return value.
   */
  public static final ClassHelper defaultClassHelper = new ClassHelper() {};

  /**
   * Default implementation of {@link CommandImplementors} for creating command objects.
   *
   * <p>This implementation uses an anonymous inner class of {@link CommandImplementors} with
   * default behavior for creating implementations of the {@link
   * org.seasar.doma.jdbc.command.Command} interface.
   *
   * <p>Used by {@link Config#getCommandImplementors()} as the default return value.
   */
  public static final CommandImplementors defaultCommandImplementors = new CommandImplementors() {};

  /**
   * Default implementation of {@link QueryImplementors} for creating query objects.
   *
   * <p>This implementation uses an anonymous inner class of {@link QueryImplementors} with default
   * behavior for creating implementations of the {@link org.seasar.doma.jdbc.query.Query}
   * interface.
   *
   * <p>Used by {@link Config#getQueryImplementors()} as the default return value.
   */
  public static final QueryImplementors defaultQueryImplementors = new QueryImplementors() {};

  /**
   * Default implementation of {@link UnknownColumnHandler} that throws an exception.
   *
   * <p>This implementation uses an anonymous inner class of {@link UnknownColumnHandler} which
   * throws an {@link UnknownColumnException} when it encounters a column in a result set that
   * doesn't match any property in the corresponding entity class.
   *
   * <p>Used by {@link Config#getUnknownColumnHandler()} as the default return value.
   */
  public static final UnknownColumnHandler defaultUnknownColumnHandler =
      new UnknownColumnHandler() {};

  /**
   * Default implementation of {@link DuplicateColumnHandler} that uses the last occurrence.
   *
   * <p>This implementation uses an anonymous inner class of {@link DuplicateColumnHandler} which
   * silently accepts duplicate columns in a result set and uses the last occurrence of each
   * duplicate column when mapping to entity properties.
   *
   * <p>Used by {@link Config#getDuplicateColumnHandler()} as the default return value.
   */
  public static final DuplicateColumnHandler defaultDuplicateColumnHandler =
      new DuplicateColumnHandler() {};

  /**
   * Default implementation of {@link Naming} for name conversion between Java and databases.
   *
   * <p>This implementation uses {@link Naming#DEFAULT} which provides the default naming convention
   * for converting between Java property names and database column names.
   *
   * <p>Used by {@link Config#getNaming()} as the default return value.
   */
  public static final Naming defaultNaming = Naming.DEFAULT;

  /**
   * Default implementation of {@link MapKeyNaming} for map key naming conventions.
   *
   * <p>This implementation uses an anonymous inner class of {@link MapKeyNaming} with default
   * behavior for converting between database column names and keys in Map objects.
   *
   * <p>Used by {@link Config#getMapKeyNaming()} as the default return value.
   */
  public static final MapKeyNaming defaultMapKeyNaming = new MapKeyNaming() {};

  /**
   * Default implementation of {@link Commenter} for adding comments to SQL statements.
   *
   * <p>This implementation uses an anonymous inner class of {@link Commenter} which adds no
   * comments to SQL statements by default.
   *
   * <p>Used by {@link Config#getCommenter()} as the default return value.
   */
  public static final Commenter defaultCommenter = new Commenter() {};

  /**
   * Default implementation of {@link EntityListenerProvider} for entity listeners.
   *
   * <p>This implementation uses an anonymous inner class of {@link EntityListenerProvider} which
   * creates one instance of each listener class per entity class. This means that the same listener
   * instance is reused for all operations on entities of the same class.
   *
   * <p>Used by {@link Config#getEntityListenerProvider()} as the default return value.
   */
  public static final EntityListenerProvider defaultEntityListenerProvider =
      new EntityListenerProvider() {};

  /**
   * Default implementation of {@link SqlBuilderSettings} for SQL generation.
   *
   * <p>This implementation uses a new instance of {@link SqlBuilderSettings} with default settings
   * for SQL generation.
   *
   * <p>Used by {@link Config#getSqlBuilderSettings()} as the default return value.
   */
  public static final SqlBuilderSettings defaultSqlBuilderSettings = new SqlBuilderSettings() {};

  /**
   * Default implementation of {@link StatisticManager} for collecting SQL execution statistics.
   *
   * <p>This implementation uses {@link DefaultStatisticManager} which provides basic functionality
   * for collecting and managing statistics about SQL execution.
   *
   * <p>Used by {@link Config#getStatisticManager()} as the default return value.
   */
  public static final StatisticManager defaultStatisticManager = new DefaultStatisticManager() {};
}
