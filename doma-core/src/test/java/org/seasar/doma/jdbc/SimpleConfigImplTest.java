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

import static org.junit.jupiter.api.Assertions.assertSame;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;

public class SimpleConfigImplTest {

  @Test
  void testGetLocalTransactionManager() {
    DataSource dataSource = new MockDataSource();
    Dialect dialect = new H2Dialect();
    JdbcLogger jdbcLogger = ConfigSupport.defaultJdbcLogger;
    LocalTransactionDataSource localTransactionDataSource =
        new LocalTransactionDataSource(dataSource);
    LocalTransactionManager transactionManager =
        new LocalTransactionManager(localTransactionDataSource, jdbcLogger);

    SimpleConfig config =
        new SimpleConfigImpl(
            localTransactionDataSource,
            dialect,
            ConfigSupport.defaultSqlFileRepository,
            ConfigSupport.defaultScriptFileLoader,
            jdbcLogger,
            ConfigSupport.defaultRequiresNewController,
            ConfigSupport.defaultClassHelper,
            ConfigSupport.defaultCommandImplementors,
            ConfigSupport.defaultQueryImplementors,
            SqlLogType.FORMATTED,
            ConfigSupport.defaultUnknownColumnHandler,
            ConfigSupport.defaultDuplicateColumnHandler,
            ConfigSupport.defaultNaming,
            ConfigSupport.defaultMapKeyNaming,
            transactionManager,
            ConfigSupport.defaultCommenter,
            ConfigSupport.defaultEntityListenerProvider,
            ConfigSupport.defaultSqlBuilderSettings,
            ConfigSupport.defaultStatisticManager,
            0,
            0,
            0,
            0);

    assertSame(localTransactionDataSource, config.getDataSource());
    assertSame(localTransactionDataSource, config.getLocalTransactionDataSource());
    assertSame(transactionManager, config.getTransactionManager());
    assertSame(transactionManager, config.getLocalTransactionManager());
  }
}
