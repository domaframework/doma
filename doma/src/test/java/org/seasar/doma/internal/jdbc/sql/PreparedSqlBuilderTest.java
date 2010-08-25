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
package org.seasar.doma.internal.jdbc.sql;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.StringWrapper;

/**
 * @author taedium
 * 
 */
public class PreparedSqlBuilderTest extends TestCase {

    private final MockConfig config = new MockConfig();

    public void testAppend() throws Exception {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config,
                SqlKind.SELECT);
        builder.appendSql("select * from aaa where name = ");
        builder.appendWrapper(new StringWrapper("hoge"));
        builder.appendSql(" and salary = ");
        builder.appendWrapper(new BigDecimalWrapper(new BigDecimal(100)));
        PreparedSql sql = builder.build();
        assertEquals("select * from aaa where name = ? and salary = ?",
                sql.toString());
    }

    public void testCutBackSql() {
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config,
                SqlKind.SELECT);
        builder.appendSql("select * from aaa where name = ");
        builder.cutBackSql(14);
        PreparedSql sql = builder.build();
        assertEquals("select * from aaa", sql.toString());
    }
}
