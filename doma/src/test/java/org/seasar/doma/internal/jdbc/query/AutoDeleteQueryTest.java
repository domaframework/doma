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
public class AutoDeleteQueryTest extends TestCase {

    private final MockConfig runtimeConfig = new MockConfig();

    public void testPrepare() throws Exception {
        Emp emp = new Emp();

        AutoDeleteQuery<Emp> query = new AutoDeleteQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntity(emp);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        DeleteQuery deleteQuery = query;
        assertNotNull(deleteQuery.getSql());
    }

    public void testOption_default() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setName("aaa");
        emp.setVersion(100);

        AutoDeleteQuery<Emp> query = new AutoDeleteQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntity(emp);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        PreparedSql sql = query.getSql();
        assertEquals("delete from EMP where ID = ? and VERSION = ?",
                sql.getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(2, parameters.size());
        assertEquals(new Integer(10), parameters.get(0).getWrapper().get());
        assertEquals(new Integer(100), parameters.get(1).getWrapper().get());
    }

    public void testOption_ignoreVersion() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setName("aaa");
        emp.setVersion(100);

        AutoDeleteQuery<Emp> query = new AutoDeleteQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntity(emp);
        query.setVersionIgnored(true);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        PreparedSql sql = query.getSql();
        assertEquals("delete from EMP where ID = ?", sql.getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(1, parameters.size());
        assertEquals(new Integer(10), parameters.get(0).getWrapper().get());
    }
}
