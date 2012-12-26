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
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;

import example.entity.Emp;

/**
 * @author taedium
 * 
 */
public class SqlFileBatchUpdateQueryTest extends TestCase {

    private final MockConfig runtimeConfig = new MockConfig();

    public void testPrepare() throws Exception {
        Emp emp1 = new Emp();
        emp1.setId(10);
        emp1.setName("aaa");
        emp1.setVersion(100);

        Emp emp2 = new Emp();
        emp2.setId(20);
        emp2.setName("bbb");
        emp2.setVersion(200);

        SqlFileBatchUpdateQuery<Emp> query = new SqlFileBatchUpdateQuery<Emp>(
                Emp.class);
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(),
                getName()));
        query.setParameterName("e");
        query.setElements(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        BatchUpdateQuery batchUpdateQuery = query;
        assertEquals(2, batchUpdateQuery.getSqls().size());
    }

    public void testOption_default() throws Exception {
        Emp emp1 = new Emp();
        emp1.setId(10);
        emp1.setName("aaa");
        emp1.setVersion(100);

        Emp emp2 = new Emp();
        emp2.setId(20);
        emp2.setSalary(new BigDecimal(2000));
        emp2.setVersion(200);

        SqlFileBatchUpdateQuery<Emp> query = new SqlFileBatchUpdateQuery<Emp>(
                Emp.class);
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(),
                getName()));
        query.setParameterName("e");
        query.setElements(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();

        PreparedSql sql = query.getSqls().get(0);
        assertEquals("update emp set name = ?, salary = ? where id = ?",
                sql.getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(3, parameters.size());
        assertEquals("aaa", parameters.get(0).getWrapper().get());
        assertNull(parameters.get(1).getWrapper().get());
        assertEquals(new Integer(10), parameters.get(2).getWrapper().get());

        sql = query.getSqls().get(1);
        assertEquals("update emp set name = ?, salary = ? where id = ?",
                sql.getRawSql());
        parameters = sql.getParameters();
        assertEquals(3, parameters.size());
        assertNull(parameters.get(0).getWrapper().get());
        assertEquals(new BigDecimal(2000), parameters.get(1).getWrapper().get());
        assertEquals(new Integer(20), parameters.get(2).getWrapper().get());
    }

    public void testIsExecutable() throws Exception {
        SqlFileBatchUpdateQuery<Emp> query = new SqlFileBatchUpdateQuery<Emp>(
                Emp.class);
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFileUtil.buildPath(getClass().getName(),
                getName()));
        query.setParameterName("e");
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setElements(Collections.<Emp> emptyList());
        query.prepare();
        assertFalse(query.isExecutable());
    }
}
