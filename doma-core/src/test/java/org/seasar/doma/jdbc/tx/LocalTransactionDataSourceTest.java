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
package org.seasar.doma.jdbc.tx;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;

public class LocalTransactionDataSourceTest {

  @Test
  public void testGetConnection() throws Exception {
    UtilLoggingJdbcLogger jdbcLogger = new UtilLoggingJdbcLogger();
    LocalTransactionDataSource dataSource = new LocalTransactionDataSource(new MockDataSource());
    dataSource.getLocalTransaction(jdbcLogger).begin();
    Connection connection = dataSource.getConnection();
    assertFalse(connection.getAutoCommit());
    dataSource.getLocalTransaction(jdbcLogger).commit();
  }

  @Test
  public void testGetConnection_notYetBegun() throws Exception {
    LocalTransactionDataSource dataSource = new LocalTransactionDataSource(new MockDataSource());
    Connection connection = dataSource.getConnection();
    assertTrue(connection.getAutoCommit());
  }

  @Test
  public void testIsWrapperFor() throws SQLException {
    DataSource dataSource = new LocalTransactionDataSource(new MockDataSource());
    assertTrue(dataSource.isWrapperFor(LocalTransactionDataSource.class));
    assertTrue(dataSource.isWrapperFor(MockDataSource.class));
    assertFalse(dataSource.isWrapperFor(Runnable.class));
  }

  @Test
  public void testUnwrap() throws SQLException {
    DataSource dataSource = new LocalTransactionDataSource(new MockDataSource());
    assertNotNull(dataSource.unwrap(LocalTransactionDataSource.class));
    assertNotNull(dataSource.unwrap(MockDataSource.class));
    try {
      dataSource.unwrap(Runnable.class);
      fail();
    } catch (SQLException ignored) {
    }
  }
}
