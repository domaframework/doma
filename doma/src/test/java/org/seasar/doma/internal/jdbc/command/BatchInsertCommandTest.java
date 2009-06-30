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

import java.util.Arrays;

import org.seasar.doma.internal.jdbc.command.BatchInsertCommand;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.query.AutoBatchInsertQuery;

import junit.framework.TestCase;
import example.entity.Emp;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
public class BatchInsertCommandTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testExecute() throws Exception {
        Emp emp1 = new Emp_();
        emp1.id().set(1);
        emp1.name().set("hoge");
        emp1.version().set(10);

        Emp emp2 = new Emp_();
        emp2.id().set(2);
        emp2.name().set("foo");
        emp2.version().set(20);

        AutoBatchInsertQuery<Emp, Emp_> query = new AutoBatchInsertQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();
        int[] rows = new BatchInsertCommand(query).execute();

        assertEquals(2, rows.length);
        String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
        assertEquals("insert into emp (id, name, salary, version) values (?, ?, ?, ?)", sql);
    }

}
