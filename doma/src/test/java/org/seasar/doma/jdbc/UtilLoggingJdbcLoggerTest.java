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

import java.util.Collections;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;

/**
 * @author taedium
 * 
 */
public class UtilLoggingJdbcLoggerTest extends TestCase {

    public void testLogSql() throws Exception {
        PreparedSql sql = new PreparedSql(SqlKind.SELECT, "aaa", "bbb", "ccc",
                Collections.<PreparedSqlParameter> emptyList());
        UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
        logger.logSql("ddd", "eee", sql);
    }

    public void testLogLocalTransactionBegun() throws Exception {
        UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
        logger.logLocalTransactionBegun("ddd", "eee", "fff");
    }

    public void testLogLocalTransactionCommitted() throws Exception {
        UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
        logger.logLocalTransactionCommitted("ddd", "eee", "fff");
    }

    public void testLogLocalTransactionRolledback() throws Exception {
        UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
        logger.logLocalTransactionRolledback("ddd", "eee", "fff");
    }
}
