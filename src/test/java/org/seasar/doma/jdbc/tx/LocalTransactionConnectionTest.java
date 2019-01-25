package org.seasar.doma.jdbc.tx;

import java.sql.Connection;
import java.sql.SQLException;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConnection;

public class LocalTransactionConnectionTest extends TestCase {

  public void testIsWrapperFor() throws Exception {
    try (Connection connection =
        new LocalTransactionConnection(
            new MockConnection(), Connection.TRANSACTION_READ_COMMITTED, true)) {
      assertTrue(connection.isWrapperFor(LocalTransactionConnection.class));
      assertTrue(connection.isWrapperFor(MockConnection.class));
      assertFalse(connection.isWrapperFor(Runnable.class));
    }
  }

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
