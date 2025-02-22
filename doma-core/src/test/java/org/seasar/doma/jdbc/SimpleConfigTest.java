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

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;

class SimpleConfigTest {

  @Test
  void build_with_url() {
    SimpleConfig config = SimpleConfig.builder("jdbc:h2:mem:test").build();

    assertNotNull(config);
    assertNotNull(config.getDataSource());
    assertNotNull(config.getDialect());
    assertNotNull(config.getLocalTransactionDataSource());
    assertNotNull(config.getLocalTransactionManager());
  }

  @Test
  void build_with_url_user_password() {
    SimpleConfig config = SimpleConfig.builder("jdbc:h2:mem:test", "sa", "sa").build();

    assertNotNull(config);
    assertNotNull(config.getDataSource());
    assertNotNull(config.getDialect());
    assertNotNull(config.getLocalTransactionDataSource());
    assertNotNull(config.getLocalTransactionManager());
  }

  @Test
  void build_with_dataSource_dialect() {
    DataSource dataSource = new MockDataSource();
    Dialect dialect = new H2Dialect();
    SimpleConfig config = SimpleConfig.builder(dataSource, dialect).build();

    assertNotNull(config);
    assertNotNull(config.getDataSource());
    assertNotEquals(dataSource, config.getDataSource());
    assertNotNull(config.getLocalTransactionManager());
    assertNotEquals(dataSource, config.getLocalTransactionDataSource());
    assertEquals(dialect, config.getDialect());
    assertNotNull(config.getLocalTransactionManager());
  }

  @Test
  void build_with_localTransactionDataSource_dialect() {
    DataSource dataSource = new MockDataSource();
    LocalTransactionDataSource localTransactionDataSource =
        new LocalTransactionDataSource(dataSource);
    Dialect dialect = new H2Dialect();

    SimpleConfig config = SimpleConfig.builder(localTransactionDataSource, dialect).build();

    assertNotNull(config);
    assertEquals(localTransactionDataSource, config.getDataSource());
    assertEquals(localTransactionDataSource, config.getLocalTransactionDataSource());
    assertEquals(dialect, config.getDialect());
    assertNotNull(config.getLocalTransactionManager());
  }
}
