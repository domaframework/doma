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

import java.util.Arrays;
import java.util.List;

import org.seasar.doma.domain.BuiltinStringDomain;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.query.BatchDeleteQuery;
import org.seasar.doma.internal.jdbc.query.SqlFileBatchDeleteQuery;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;
import org.seasar.doma.internal.jdbc.sql.SqlFileUtil;

import junit.framework.TestCase;
import example.entity.Emp;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
public class SqlFileBatchDeleteQueryTest extends TestCase {

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

        SqlFileBatchDeleteQuery<Emp, Emp_> query = new SqlFileBatchDeleteQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFileUtil
                .buildPath(getClass().getName(), getName()));
        query.setParameterName("e");
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        BatchDeleteQuery batchDeleteQuery = query;
        assertEquals(2, batchDeleteQuery.getSqls().size());
    }

    public void testOption_default() throws Exception {
        Emp emp1 = new Emp_();
        emp1.name().set("aaa");

        Emp emp2 = new Emp_();
        emp2.name().set("bbb");

        SqlFileBatchDeleteQuery<Emp, Emp_> query = new SqlFileBatchDeleteQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFileUtil
                .buildPath(getClass().getName(), getName()));
        query.setParameterName("e");
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        PreparedSql sql = query.getSqls().get(0);
        assertEquals("delete from emp where name = ?", sql.getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(1, parameters.size());
        assertEquals(new BuiltinStringDomain("aaa"), parameters.get(0).getDomain());

        sql = query.getSqls().get(1);
        assertEquals("delete from emp where name = ?", sql.getRawSql());
        parameters = sql.getParameters();
        assertEquals(1, parameters.size());
        assertEquals(new BuiltinStringDomain("bbb"), parameters.get(0).getDomain());
    }

}
