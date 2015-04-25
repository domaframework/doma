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

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.sql.PreparedSql;

/**
 * 
 * @author nakamura-to
 *
 */
public class PostgresDialectTest extends TestCase {

    public void testGetIdentitySelectSql_quoteNotRequired() throws Exception {
        PostgresDialect dialect = new PostgresDialect();
        PreparedSql sql = dialect.getIdentitySelectSql("aaa", "bbb", "ccc",
                "ddd", false);
        assertEquals("select currval('aaa.bbb.ccc_ddd_seq')", sql.getRawSql());
    }

    public void testGetIdentitySelectSql_quoteRequired() throws Exception {
        PostgresDialect dialect = new PostgresDialect();
        PreparedSql sql = dialect.getIdentitySelectSql("aaa", "bbb", "ccc",
                "ddd", true);
        assertEquals("select currval('\"aaa\".\"bbb\".\"ccc_ddd_seq\"')",
                sql.getRawSql());
    }

}
