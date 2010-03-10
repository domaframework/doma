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
package org.seasar.doma.jdbc;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConnection;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;

/**
 * @author taedium
 * 
 */
public class LocalTransactionTest extends TestCase {

    private final MockConnection connection = new MockConnection();

    private final MockDataSource dataSource = new MockDataSource(connection);

    private final ThreadLocal<NeverClosedConnection> connectionHolder = new ThreadLocal<NeverClosedConnection>();

    private final UtilLoggingJdbcLogger jdbcLogger = new UtilLoggingJdbcLogger();

    public void testBegin() throws Exception {
        LocalTransaction transaction = new LocalTransaction(dataSource,
                connectionHolder, jdbcLogger);
        transaction.begin();
        assertTrue(transaction.isActive());
        assertFalse(connection.autoCommit);
    }

    public void testBegin_alreadyBegun() throws Exception {
        LocalTransaction transaction = new LocalTransaction(dataSource,
                connectionHolder, jdbcLogger);
        transaction.begin();
        try {
            transaction.begin();
            fail();
        } catch (LocalTransactionAlreadyBegunException expected) {
            System.out.println(expected.getMessage());
        }
    }

    public void testCommit() {
        LocalTransaction transaction = new LocalTransaction(dataSource,
                connectionHolder, jdbcLogger);
        transaction.begin();
        transaction.commit();
        assertFalse(transaction.isActive());
        assertTrue(connection.autoCommit);
        assertTrue(connection.committed);
    }

    public void testCommit_notYetBegun() throws Exception {
        LocalTransaction transaction = new LocalTransaction(dataSource,
                connectionHolder, jdbcLogger);
        try {
            transaction.commit();
            fail();
        } catch (LocalTransactionNotYetBegunException expected) {
            System.out.println(expected.getMessage());
        }
    }

    public void testRollback() {
        LocalTransaction transaction = new LocalTransaction(dataSource,
                connectionHolder, jdbcLogger);
        transaction.begin();
        transaction.rollback();
        assertFalse(transaction.isActive());
        assertTrue(connection.autoCommit);
        assertTrue(connection.rolledback);
    }

    public void testRollback_notYetBegun() throws Exception {
        LocalTransaction transaction = new LocalTransaction(dataSource,
                connectionHolder, jdbcLogger);
        transaction.rollback();
    }
}
