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

import org.seasar.doma.domain.BuiltinBigDecimalDomain;
import org.seasar.doma.domain.BuiltinIntegerDomain;
import org.seasar.doma.domain.BuiltinStringDomain;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.query.BatchInsertQuery;
import org.seasar.doma.internal.jdbc.query.SqlFileBatchInsertQuery;
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
public class SqlFileBatchInsertQueryTest extends TestCase {

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

        SqlFileBatchInsertQuery<Emp, Emp_> query = new SqlFileBatchInsertQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setSqlFilePath(SqlFileUtil
                .buildPath(getClass().getName(), getName()));
        query.setParameterName("e");
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        BatchInsertQuery batchInsertQuery = query;
        assertEquals(2, batchInsertQuery.getSqls().size());
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

        SqlFileBatchInsertQuery<Emp, Emp_> query = new SqlFileBatchInsertQuery<Emp, Emp_>(
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
        assertEquals("insert into emp (id, name, salary) values (?, ?, ?)", sql
                .getRawSql());
        List<PreparedSqlParameter> parameters = sql.getParameters();
        assertEquals(3, parameters.size());
        assertEquals(new BuiltinIntegerDomain(10), parameters.get(0).getDomain());
        assertEquals(new BuiltinStringDomain("aaa"), parameters.get(1).getDomain());
        assertTrue(parameters.get(2).getDomain().isNull());

        sql = query.getSqls().get(1);
        assertEquals("insert into emp (id, name, salary) values (?, ?, ?)", sql
                .getRawSql());
        parameters = sql.getParameters();
        assertEquals(3, parameters.size());
        assertEquals(new BuiltinIntegerDomain(20), parameters.get(0).getDomain());
        assertTrue(parameters.get(1).getDomain().isNull());
        assertEquals(new BuiltinBigDecimalDomain(new BigDecimal(2000)), parameters
                .get(2).getDomain());
    }

}
