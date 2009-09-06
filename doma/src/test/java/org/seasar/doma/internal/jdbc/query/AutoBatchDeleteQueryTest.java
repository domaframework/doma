/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.doma.internal.jdbc.query;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.doma.domain.BuiltinIntegerDomain;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;

import example.entity.Emp;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
public class AutoBatchDeleteQueryTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testCompile() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(10);
        emp1.name().set("aaa");

        Emp emp2 = new Emp_();
        emp2.id().set(20);
        emp2.name().set("bbb");

        AutoBatchDeleteQuery<Emp, Emp_> query = new AutoBatchDeleteQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        BatchDeleteQuery batchDeleteQuery = query;
        assertEquals(2, batchDeleteQuery.getSqls().size());
    }

    public void testOption_default() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(10);
        emp1.name().set("aaa");

        Emp emp2 = new Emp_();
        emp2.id().set(20);
        emp2.salary().set(new BigDecimal(2000));
        emp2.version().set(new Integer(10));

        AutoBatchDeleteQuery<Emp, Emp_> query = new AutoBatchDeleteQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        PreparedSql sql = query.getSqls().get(0);
        assertEquals("delete from EMP where ID = ? and VERSION = ?", sql
                .getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(2, parameters.size());
        assertEquals(new BuiltinIntegerDomain(10), parameters.get(0).getDomain());
        assertTrue(parameters.get(1).getDomain().isNull());

        sql = query.getSqls().get(1);
        assertEquals("delete from EMP where ID = ? and VERSION = ?", sql
                .getRawSql());
        parameters = sql.getParameters();
        assertEquals(2, parameters.size());
        assertEquals(new BuiltinIntegerDomain(20), parameters.get(0).getDomain());
        assertEquals(new BuiltinIntegerDomain(10), parameters.get(1).getDomain());
    }

    public void testOption_ignoreVersion() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(10);
        emp1.name().set("aaa");

        Emp emp2 = new Emp_();
        emp2.id().set(20);
        emp2.salary().set(new BigDecimal(2000));
        emp2.version().set(new Integer(10));

        AutoBatchDeleteQuery<Emp, Emp_> query = new AutoBatchDeleteQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setVersionIgnored(true);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        PreparedSql sql = query.getSqls().get(0);
        assertEquals("delete from EMP where ID = ?", sql.getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(1, parameters.size());
        assertEquals(new BuiltinIntegerDomain(10), parameters.get(0).getDomain());

        sql = query.getSqls().get(1);
        assertEquals("delete from EMP where ID = ?", sql.getRawSql());
        parameters = sql.getParameters();
        assertEquals(1, parameters.size());
        assertEquals(new BuiltinIntegerDomain(20), parameters.get(0).getDomain());
    }
}
