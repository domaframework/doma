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
package org.seasar.doma.jdbc.query;

import java.math.BigDecimal;
import java.util.List;
import java.util.LinkedHashMap;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.InParameter;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author bakenezumi
 * 
 */
public class AutoMapInsertQueryTest extends TestCase {

    private final MockConfig runtimeConfig = new MockConfig();

    public void testPrepare() throws Exception {

        AutoMapInsertQuery query = new AutoMapInsertQuery(
                "EMP",
                new LinkedHashMap<String, Object>(){{
                    put("ID", 10);
                    put("NAME", "aaa");
                }});
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setSqlLogType(SqlLogType.FORMATTED);
        query.prepare();

        InsertQuery insertQuery = query;
        assertNotNull(insertQuery.getSql());
    }

    public void testOption_default() throws Exception {

        AutoMapInsertQuery query = new AutoMapInsertQuery(
                "EMP",
                new LinkedHashMap<String, Object>(){{
                    put("ID", 10);
                    put("NAME", "aaa");
                }});
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setSqlLogType(SqlLogType.FORMATTED);
        query.prepare();

        PreparedSql sql = query.getSql();
        assertEquals(
                "insert into EMP (ID, NAME) values (?, ?)",
                sql.getRawSql());
        List<InParameter<?>> parameters = sql.getParameters();
        assertEquals(2, parameters.size());
        assertEquals(new Integer(10), parameters.get(0).getWrapper().get());
        assertEquals("aaa", parameters.get(1).getWrapper().get());
    }
}
