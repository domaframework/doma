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
package org.seasar.doma.internal.jdbc.command;

import java.math.BigDecimal;
import java.util.List;

import org.seasar.doma.internal.jdbc.command.InsertCommand;
import org.seasar.doma.internal.jdbc.mock.BindValue;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.query.AutoInsertQuery;

import junit.framework.TestCase;
import example.entity.Emp;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
public class InsertCommandTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testExecute() throws Exception {
        Emp emp = new Emp_();
        emp.id().set(1);
        emp.name().set("hoge");
        emp.salary().set(new BigDecimal(1000));
        emp.version().set(10);

        AutoInsertQuery<Emp, Emp_> query = new AutoInsertQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntity(emp);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();
        int rows = new InsertCommand(query).execute();

        assertEquals(1, rows);
        String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
        assertEquals("insert into emp (id, name, salary, version) values (?, ?, ?, ?)", sql);

        List<BindValue> bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
        assertEquals(new Integer(1), bindValues.get(0).getValue());
        assertEquals(new String("hoge"), bindValues.get(1).getValue());
        assertEquals(new BigDecimal(1000), bindValues.get(2).getValue());
        assertEquals(new Integer(10), bindValues.get(3).getValue());
    }

    public void testExecute_defaultVersion() throws Exception {
        Emp emp = new Emp_();
        emp.id().set(1);
        emp.name().set("hoge");
        emp.salary().set(new BigDecimal(1000));
        emp.version().setNull();

        AutoInsertQuery<Emp, Emp_> query = new AutoInsertQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntity(emp);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();
        int rows = new InsertCommand(query).execute();

        assertEquals(1, rows);
        String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
        assertEquals("insert into emp (id, name, salary, version) values (?, ?, ?, ?)", sql);

        List<BindValue> bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
        assertEquals(4, bindValues.size());
        assertEquals(new Integer(1), bindValues.get(0).getValue());
        assertEquals(new String("hoge"), bindValues.get(1).getValue());
        assertEquals(new BigDecimal(1000), bindValues.get(2).getValue());
        assertEquals(new Integer(1), bindValues.get(3).getValue());
    }
}
