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
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConnection;

public class LocalTransactionConnectionTest {

  @Test
  public void testIsWrapperFor() throws Exception {
    try (Connection connection =
        new LocalTransactionConnection(
            new MockConnection(), Connection.TRANSACTION_READ_COMMITTED, true)) {
      assertTrue(connection.isWrapperFor(LocalTransactionConnection.class));
      assertTrue(connection.isWrapperFor(MockConnection.class));
      assertFalse(connection.isWrapperFor(Runnable.class));
    }
  }

  @Test
  public void testUnwrap() throws Exception {
    try (Connection connection =
        new LocalTransactionConnection(
            new MockConnection(), Connection.TRANSACTION_READ_COMMITTED, true)) {
      assertNotNull(connection.unwrap(LocalTransactionConnection.class));
      assertNotNull(connection.unwrap(LocalTransactionConnection.class));
      try {
        connection.unwrap(Runnable.class);
        fail();
      } catch (SQLException ignored) {
      }
    }
  }
}
