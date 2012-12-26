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
package org.seasar.doma.internal.jdbc.query;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;

import example.entity.Emp;
import example.entity._Emp;

/**
 * @author taedium
 * 
 */
public class AutoBatchDeleteQueryTest extends TestCase {

    private final MockConfig runtimeConfig = new MockConfig();

    public void testPrepare() throws Exception {
        Emp emp1 = new Emp();
        emp1.setId(10);
        emp1.setName("aaa");

        Emp emp2 = new Emp();
        emp2.setId(20);
        emp2.setName("bbb");

        AutoBatchDeleteQuery<Emp> query = new AutoBatchDeleteQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        BatchDeleteQuery batchDeleteQuery = query;
        assertEquals(2, batchDeleteQuery.getSqls().size());
    }

    public void testOption_default() throws Exception {
        Emp emp1 = new Emp();
        emp1.setId(10);
        emp1.setName("aaa");

        Emp emp2 = new Emp();
        emp2.setId(20);
        emp2.setSalary(new BigDecimal(2000));
        emp2.setVersion(new Integer(10));

        AutoBatchDeleteQuery<Emp> query = new AutoBatchDeleteQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        PreparedSql sql = query.getSqls().get(0);
        assertEquals("delete from EMP where ID = ? and VERSION = ?",
                sql.getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(2, parameters.size());
        assertEquals(new Integer(10), parameters.get(0).getWrapper().get());
        assertTrue(parameters.get(1).getWrapper().get() == null);

        sql = query.getSqls().get(1);
        assertEquals("delete from EMP where ID = ? and VERSION = ?",
                sql.getRawSql());
        parameters = sql.getParameters();
        assertEquals(2, parameters.size());
        assertEquals(new Integer(20), parameters.get(0).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(1).getWrapper().get());
    }

    public void testOption_ignoreVersion() throws Exception {
        Emp emp1 = new Emp();
        emp1.setId(10);
        emp1.setName("aaa");

        Emp emp2 = new Emp();
        emp2.setId(20);
        emp2.setSalary(new BigDecimal(2000));
        emp2.setVersion(new Integer(10));

        AutoBatchDeleteQuery<Emp> query = new AutoBatchDeleteQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setVersionIgnored(true);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        PreparedSql sql = query.getSqls().get(0);
        assertEquals("delete from EMP where ID = ?", sql.getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(1, parameters.size());
        assertEquals(new Integer(10), parameters.get(0).getWrapper().get());

        sql = query.getSqls().get(1);
        assertEquals("delete from EMP where ID = ?", sql.getRawSql());
        parameters = sql.getParameters();
        assertEquals(1, parameters.size());
        assertEquals(new Integer(20), parameters.get(0).getWrapper().get());
    }
}
