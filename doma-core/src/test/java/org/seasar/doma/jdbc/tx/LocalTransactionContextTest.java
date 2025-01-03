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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Connection;
import java.sql.Savepoint;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConnection;

public class LocalTransactionContextTest {

  @Test
  public void testReleaseAndGetSavepoint() throws Exception {
    try (LocalTransactionConnection connection =
        new LocalTransactionConnection(
            new MockConnection(), Connection.TRANSACTION_READ_COMMITTED, true)) {
      LocalTransactionContext context = new LocalTransactionContext();
      context.addSavepoint("1", new MySavepoint("1"));
      context.addSavepoint("2", new MySavepoint("2"));
      context.addSavepoint("3", new MySavepoint("3"));
      context.addSavepoint("4", new MySavepoint("4"));
      context.addSavepoint("5", new MySavepoint("5"));

      Savepoint savepoint = context.releaseAndGetSavepoint("3");
      assertEquals("3", savepoint.getSavepointName());

      assertNotNull(context.getSavepoint("1"));
      assertNotNull(context.getSavepoint("2"));
      assertNull(context.getSavepoint("3"));
      assertNull(context.getSavepoint("4"));
      assertNull(context.getSavepoint("5"));
    }
  }

  static class MySavepoint implements Savepoint {

    final String name;

    public MySavepoint(String name) {
      this.name = name;
    }

    @Override
    public int getSavepointId() {
      return 0;
    }

    @Override
    public String getSavepointName() {
      return name;
    }
  }
}
