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

/** Default values for {@link Config} objects. */
public final class ConfigSupport {

  public static final SqlFileRepository defaultSqlFileRepository =
      new GreedyCacheSqlFileRepository();

  public static final ScriptFileLoader defaultScriptFileLoader = new ScriptFileLoader() {};

  public static final JdbcLogger defaultJdbcLogger = new UtilLoggingJdbcLogger();

  public static final RequiresNewController defaultRequiresNewController =
      new RequiresNewController() {};

  public static final ClassHelper defaultClassHelper = new ClassHelper() {};

  public static final CommandImplementors defaultCommandImplementors = new CommandImplementors() {};

  public static final QueryImplementors defaultQueryImplementors = new QueryImplementors() {};

  public static final UnknownColumnHandler defaultUnknownColumnHandler =
      new UnknownColumnHandler() {};

  public static final DuplicateColumnHandler defaultDuplicateColumnHandler =
      new DuplicateColumnHandler() {};

  public static final Naming defaultNaming = Naming.DEFAULT;

  public static final MapKeyNaming defaultMapKeyNaming = new MapKeyNaming() {};

  public static final Commenter defaultCommenter = new Commenter() {};

  public static final EntityListenerProvider defaultEntityListenerProvider =
      new EntityListenerProvider() {};

  public static final SqlBuilderSettings defaultSqlBuilderSettings = new SqlBuilderSettings() {};

  public static final StatisticManager defaultStatisticManager = new DefaultStatisticManager() {};
}
