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
public class AutoUpdateQueryTest extends TestCase {

    private final MockConfig runtimeConfig = new MockConfig();

    public void testPrepare() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setName("aaa");
        emp.setVersion(100);

        AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntity(emp);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        UpdateQuery updateQuery = query;
        assertNotNull(updateQuery.getSql());
    }

    public void testOption_default() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setName("aaa");
        emp.setVersion(100);
        emp.originalStates = new Emp();

        AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntity(emp);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        PreparedSql sql = query.getSql();
        assertEquals(
                "update EMP set NAME = ?, VERSION = ? + 1 where ID = ? and VERSION = ?",
                sql.getRawSql());

        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(4, parameters.size());
        assertEquals("aaa", parameters.get(0).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(2).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(3).getWrapper().get());
    }

    public void testOption_excludeNull() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setVersion(100);

        AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntity(emp);
        query.setNullExcluded(true);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        PreparedSql sql = query.getSql();
        assertEquals(
                "update EMP set VERSION = ? + 1 where ID = ? and VERSION = ?",
                sql.getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(3, parameters.size());
        assertEquals(new Integer(100), parameters.get(0).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(1).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(2).getWrapper().get());
    }

    public void testOption_includeVersion() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setName("aaa");
        emp.setVersion(100);
        emp.originalStates = new Emp();

        AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntity(emp);
        query.setVersionIncluded(true);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        PreparedSql sql = query.getSql();
        assertEquals("update EMP set NAME = ?, VERSION = ? where ID = ?",
                sql.getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(3, parameters.size());
        assertEquals("aaa", parameters.get(0).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(2).getWrapper().get());
    }

    public void testOption_include() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setName("aaa");
        emp.setSalary(new BigDecimal(200));
        emp.setVersion(100);

        AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntity(emp);
        query.setIncludedPropertyNames("name");
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        PreparedSql sql = query.getSql();
        assertEquals(
                "update EMP set NAME = ?, VERSION = ? + 1 where ID = ? and VERSION = ?",
                sql.getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(4, parameters.size());
        assertEquals("aaa", parameters.get(0).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(2).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(3).getWrapper().get());
    }

    public void testOption_exclude() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setName("aaa");
        emp.setSalary(new BigDecimal(200));
        emp.setVersion(100);

        AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntity(emp);
        query.setExcludedPropertyNames("name");
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        PreparedSql sql = query.getSql();
        assertEquals(
                "update EMP set SALARY = ?, VERSION = ? + 1 where ID = ? and VERSION = ?",
                sql.getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(4, parameters.size());
        assertEquals(new BigDecimal(200), parameters.get(0).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(2).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
    }

    public void testIsExecutable() throws Exception {
        Emp emp = new Emp();
        emp.originalStates = new Emp();

        AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntity(emp);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        assertFalse(query.isExecutable());
    }

}
