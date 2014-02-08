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

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;

/**
 * @author taedium
 * 
 */
public class LocalTransactionalDataSourceTest extends TestCase {

    public void testGetConnection() throws Exception {
        UtilLoggingJdbcLogger jdbcLogger = new UtilLoggingJdbcLogger();
        LocalTransactionalDataSource dataSource = new LocalTransactionalDataSource(
                new MockDataSource());
        dataSource.getLocalTransaction(jdbcLogger).begin();
        dataSource.getConnection();
        dataSource.getLocalTransaction(jdbcLogger).commit();
    }

    public void testGetConnection_notYetBegun() throws Exception {
        LocalTransactionalDataSource dataSource = new LocalTransactionalDataSource(
                new MockDataSource());
        try {
            dataSource.getConnection();
            fail();
        } catch (LocalTransactionNotYetBegunException expected) {
            System.out.println(expected.getMessage());
        }
    }
}
