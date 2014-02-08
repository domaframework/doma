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
package org.seasar.doma.jdbc.dialect;

import java.sql.SQLException;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class SqliteDialectTest extends TestCase {

    public void testIsUniqueConstraintViolated_true() {
        SqliteDialect dialect = new SqliteDialect();
        SQLException e = new SQLException(
                "[SQLITE_CONSTRAINT]  Abort due to constraint violation (PRIMARY KEY must be unique)");
        assertTrue(dialect.isUniqueConstraintViolated(e));
    }

    public void testIsUniqueConstraintViolated_false() {
        SqliteDialect dialect = new SqliteDialect();
        SQLException e = new SQLException(
                "[SQLITE_CONSTRAINT]  Abort due to constraint violation (hoge.foo may not be NULL)");
        assertFalse(dialect.isUniqueConstraintViolated(e));
    }

}
