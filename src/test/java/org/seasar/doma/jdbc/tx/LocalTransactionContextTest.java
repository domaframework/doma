package org.seasar.doma.jdbc.tx;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.seasar.doma.internal.jdbc.mock.MockConnection;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class LocalTransactionContextTest extends TestCase {

    public void testReleaseAndGetSavepoint() throws Exception {
        try (LocalTransactionConnection connection = new LocalTransactionConnection(
                new MockConnection(), Connection.TRANSACTION_READ_COMMITTED)) {
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

    class MySavepoint implements Savepoint {

        String name;

        public MySavepoint(String name) {
            this.name = name;
        }

        @Override
        public int getSavepointId() throws SQLException {
            return 0;
        }

        @Override
        public String getSavepointName() throws SQLException {
            return name;
        }

    }
}
