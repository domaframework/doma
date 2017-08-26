/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.jdbc.tx;

import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConnection;

/**
 * @author nakamura-to
 *
 */
public class LocalTransactionConnectionTest extends TestCase {

    public void testIsWrapperFor() throws Exception {
        try (Connection connection = new LocalTransactionConnection(new MockConnection(),
                Connection.TRANSACTION_READ_COMMITTED)) {
            assertTrue(connection.isWrapperFor(LocalTransactionConnection.class));
            assertTrue(connection.isWrapperFor(MockConnection.class));
            assertFalse(connection.isWrapperFor(Runnable.class));
        }
    }

    public void testUnwrap() throws Exception {
        try (Connection connection = new LocalTransactionConnection(new MockConnection(),
                Connection.TRANSACTION_READ_COMMITTED)) {
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
