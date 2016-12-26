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
package org.seasar.doma.jdbc.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlParameter;

/**
 * @author bakenezumi
 *
 */
public class BatchInsertExecutorTest extends TestCase {

    public void testBuilder() throws Exception {
        BatchInsertExecutor.BatchInsertBuilder builder = BatchInsertExecutor.BatchInsertBuilder.newInstance(new MockConfig());
        builder.sql("insert into Emp");
        builder.sql("(name, salary)");
        builder.sql("values (");
        builder.param(String.class, "SMITH").sql(", ");
        builder.param(int.class, 100).sql(")");
        builder = builder.fixSql();
        builder.sql("insert into Emp");
        builder.sql("(name, salary)");
        builder.sql("values (");
        builder.param(String.class, "ALLEN").sql(", ");
        builder.param(int.class, 200).sql(")");
        builder = builder.fixSql();

        builder.execute();
    }

    public void testGetSql() throws Exception {
        BatchInsertExecutor.BatchInsertBuilder builder = BatchInsertExecutor.BatchInsertBuilder.newInstance(new MockConfig());
        builder.sql("insert into Emp");
        builder.sql("(name, salary)");
        builder.sql("values (");
        builder.param(String.class, "SMITH").sql(", ");
        builder.param(int.class, 100).sql(")");
        builder = builder.fixSql();
        builder.sql("insert into Emp");
        builder.sql("(name, salary)");
        builder.sql("values (");
        builder.param(String.class, "ALLEN").sql(", ");
        builder.param(int.class, 200).sql(")");
        builder = builder.fixSql();
        String sql = String.format("insert into Emp%n" + "(name, salary)%n"
                                   + "values (?, ?)");

        List<? extends Sql<?>> sqls = builder.getSqls();
        assertEquals(2, sqls.size());
        Sql<?> sql0 = sqls.get(0);
        assertEquals(sql, sql0.getRawSql());
        List<? extends SqlParameter> parameters0 = sql0.getParameters();
        assertEquals(2, parameters0.size());
        assertEquals("SMITH", parameters0.get(0).getValue());
        assertEquals(100, parameters0.get(1).getValue());

        Sql<?> sql1 = sqls.get(1);
        assertEquals(sql, sql1.getRawSql());
        List<? extends SqlParameter> parameters1 = sql1.getParameters();
        assertEquals(2, parameters1.size());
        assertEquals("ALLEN", parameters1.get(0).getValue());
        assertEquals(200, parameters1.get(1).getValue());
    }

    public void testLiteral() throws Exception {
        BatchInsertExecutor.BatchInsertBuilder builder = BatchInsertExecutor.BatchInsertBuilder.newInstance(new MockConfig());
        builder.sql("insert into Emp");
        builder.sql("(name, salary)");
        builder.sql("values (");
        builder.literal(String.class, "SMITH").sql(", ");
        builder.param(int.class, 100).sql(")");
        builder = builder.fixSql();
        builder.sql("insert into Emp");
        builder.sql("(name, salary)");
        builder.sql("values (");
        builder.literal(String.class, "ALLEN").sql(", ");
        builder.param(int.class, 200).sql(")");
        builder = builder.fixSql();

        List<? extends Sql<?>> sqls = builder.getSqls();
        assertEquals(2, sqls.size());
        Sql<?> sql0 = sqls.get(0);
        assertEquals(String.format("insert into Emp%n" + "(name, salary)%n"
                                   + "values ('SMITH', ?)"), sql0.getRawSql());
        List<? extends SqlParameter> parameters0 = sql0.getParameters();
        assertEquals(1, parameters0.size());
        assertEquals(100, parameters0.get(0).getValue());

        Sql<?> sql1 = sqls.get(1);
        assertEquals(String.format("insert into Emp%n" + "(name, salary)%n"
                                   + "values ('ALLEN', ?)"), sql1.getRawSql());
        List<? extends SqlParameter> parameters1 = sql1.getParameters();
        assertEquals(1, parameters1.size());
        assertEquals(200, parameters1.get(0).getValue());
    }

    public void testNotEqualParamCall() throws Exception {
        BatchInsertExecutor.BatchInsertBuilder builder = BatchInsertExecutor.BatchInsertBuilder.newInstance(new MockConfig());
        builder.sql("insert into Emp");
        builder.sql("(name, salary)");
        builder.sql("values (");
        builder.param(String.class, "SMITH").sql(", ");
        builder.param(int.class, 100).sql(")");
        builder = builder.fixSql();
        builder.sql("insert into Emp");
        builder.sql("(name, salary)");
        builder.sql("values (");
        builder.param(String.class, "ALLEN").sql(")");
        builder = builder.fixSql();

        try {
            builder.execute();
        } catch (AssertionError e) {
            return;
        }

        fail();
    }

    public void testChangeType() throws Exception {
        BatchInsertExecutor.BatchInsertBuilder builder = BatchInsertExecutor.BatchInsertBuilder.newInstance(new MockConfig());
        builder.sql("insert into Emp");
        builder.sql("(name, salary)");
        builder.sql("values (");
        builder.param(String.class, "SMITH").sql(", ");
        builder.param(int.class, 100).sql(")");
        builder = builder.fixSql();
        builder.sql("insert into Emp");
        builder.sql("(name, salary)");
        builder.sql("values (");

        try {
            builder.literal(int.class, 10).sql(", ");
        } catch (IllegalStateException e) {
            return;
        }

        fail();
    }

    public void testParamToLiteral() throws Exception {
        BatchInsertExecutor.BatchInsertBuilder builder = BatchInsertExecutor.BatchInsertBuilder.newInstance(new MockConfig());
        builder.sql("insert into Emp");
        builder.sql("(name, salary)");
        builder.sql("values (");
        builder.param(String.class, "SMITH").sql(", ");
        builder.param(int.class, 100).sql(")");
        builder = builder.fixSql();
        builder.sql("insert into Emp");
        builder.sql("(name, salary)");
        builder.sql("values (");

        try {
            builder.literal(String.class, "ALLEN").sql(", ");
        } catch (IllegalStateException e) {
            return;
        }

        fail();
    }

    private static class Employee {
        final String name;
        final int salary;
        Employee(String name, int salary) {
            this.name = name;
            this.salary = salary;
        }
    }

    public void testExecutor() throws Exception {
        List<Employee> employees = Arrays.asList(new Employee[] {
                                       new Employee("SMITH", 100),
                                       new Employee("ALLEN", 200)
                                   });
        BatchInsertExecutor executor = BatchInsertExecutor.newInstance(new MockConfig());
        executor.execute(employees, (emp, builder) -> {
            builder.sql("insert into Emp");
            builder.sql("(name, salary)");
            builder.sql("values (");
            builder.param(String.class, emp.name).sql(", ");
            builder.param(int.class, emp.salary).sql(")");
        });
    }

    public void testExecutorByMap() throws Exception {
        List<LinkedHashMap<String, Object>> employees = new ArrayList<LinkedHashMap<String, Object>>() {{
            add(new LinkedHashMap<String, Object>(){{
                put("name", "SMITH");
                put("salary", 500);
            }});
            add(new LinkedHashMap<String, Object>(){{
                put("name", "ALLEN");
                put("salary", null);
            }});
        }};
        BatchInsertExecutor executor = BatchInsertExecutor.newInstance(new MockConfig());
        executor.execute(employees, (emp, builder) -> {
            builder.sql("insert into Emp");
            builder.sql("(name, salary)");
            builder.sql("values (");
            builder.param(String.class, (String) emp.get("name")).sql(", ");
            builder.param(Integer.class, (Integer) emp.get("salary")).sql(")");
        });
    }    
}
