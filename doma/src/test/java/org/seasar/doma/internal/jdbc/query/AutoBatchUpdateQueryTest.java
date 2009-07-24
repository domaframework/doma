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
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.doma.DomaMessageCode;
import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;
import org.seasar.doma.jdbc.JdbcException;

import example.entity.Emp;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
public class AutoBatchUpdateQueryTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testCompile() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(10);
        emp1.name().set("aaa");
        emp1.version().set(100);

        Emp emp2 = new Emp_();
        emp2.id().set(20);
        emp2.name().set("bbb");
        emp2.version().set(200);

        AutoBatchUpdateQuery<Emp, Emp_> query = new AutoBatchUpdateQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        BatchUpdateQuery batchUpdateQuery = query;
        assertEquals(2, batchUpdateQuery.getSqls().size());
    }

    public void testOption_default() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(10);
        emp1.name().set("aaa");
        emp1.version().set(100);

        Emp emp2 = new Emp_();
        emp2.id().set(20);
        emp2.salary().set(new BigDecimal(2000));
        emp2.version().set(200);

        AutoBatchUpdateQuery<Emp, Emp_> query = new AutoBatchUpdateQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        PreparedSql sql = query.getSqls().get(0);
        assertEquals("update EMP set NAME = ?, SALARY = ?, VERSION = ? + 1 where ID = ? and VERSION = ?", sql
                .getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(5, parameters.size());
        assertEquals(new StringDomain("aaa"), parameters.get(0).getDomain());
        assertTrue(parameters.get(1).getDomain().isNull());
        assertEquals(new IntegerDomain(100), parameters.get(2).getDomain());
        assertEquals(new IntegerDomain(10), parameters.get(3).getDomain());
        assertEquals(new IntegerDomain(100), parameters.get(4).getDomain());

        sql = query.getSqls().get(1);
        assertEquals("update EMP set NAME = ?, SALARY = ?, VERSION = ? + 1 where ID = ? and VERSION = ?", sql
                .getRawSql());
        parameters = sql.getParameters();
        assertEquals(5, parameters.size());
        assertTrue(parameters.get(0).getDomain().isNull());
        assertEquals(new BigDecimalDomain(new BigDecimal(2000)), parameters
                .get(1).getDomain());
        assertEquals(new IntegerDomain(200), parameters.get(2).getDomain());
        assertEquals(new IntegerDomain(20), parameters.get(3).getDomain());
        assertEquals(new IntegerDomain(200), parameters.get(4).getDomain());
    }

    public void testOption_includesVersion() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(10);
        emp1.name().set("aaa");
        emp1.version().set(100);

        Emp emp2 = new Emp_();
        emp2.id().set(20);
        emp2.salary().set(new BigDecimal(2000));
        emp2.version().set(200);

        AutoBatchUpdateQuery<Emp, Emp_> query = new AutoBatchUpdateQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setVersionIncluded(true);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        PreparedSql sql = query.getSqls().get(0);
        assertEquals("update EMP set NAME = ?, SALARY = ?, VERSION = ? where ID = ?", sql
                .getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(4, parameters.size());
        assertEquals(new StringDomain("aaa"), parameters.get(0).getDomain());
        assertTrue(parameters.get(1).getDomain().isNull());
        assertEquals(new IntegerDomain(100), parameters.get(2).getDomain());
        assertEquals(new IntegerDomain(10), parameters.get(3).getDomain());

        sql = query.getSqls().get(1);
        assertEquals("update EMP set NAME = ?, SALARY = ?, VERSION = ? where ID = ?", sql
                .getRawSql());
        parameters = sql.getParameters();
        assertEquals(4, parameters.size());
        assertTrue(parameters.get(0).getDomain().isNull());
        assertEquals(new BigDecimalDomain(new BigDecimal(2000)), parameters
                .get(1).getDomain());
        assertEquals(new IntegerDomain(200), parameters.get(2).getDomain());
        assertEquals(new IntegerDomain(20), parameters.get(3).getDomain());
    }

    public void testIsExecutable() throws Exception {
        AutoBatchUpdateQuery<Emp, Emp_> query = new AutoBatchUpdateQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Collections.<Emp> emptyList());
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();
        assertFalse(query.isExecutable());
    }

    public void testIllegalEntityInstance() throws Exception {
        AutoBatchUpdateQuery<Emp, Emp_> query = new AutoBatchUpdateQuery<Emp, Emp_>(
                Emp_.class);
        try {
            query.setEntities(Arrays.<Emp> asList(new MyEmp()));
            fail();
        } catch (JdbcException expected) {
            assertEquals(DomaMessageCode.DOMA2026, expected.getMessageCode());
        }
    }

    private static class MyEmp implements Emp {

        @Override
        public IntegerDomain version() {
            return null;
        }

        @Override
        public BigDecimalDomain salary() {
            return null;
        }

        @Override
        public StringDomain name() {
            return null;
        }

        @Override
        public IntegerDomain id() {
            return null;
        }
    }
}
