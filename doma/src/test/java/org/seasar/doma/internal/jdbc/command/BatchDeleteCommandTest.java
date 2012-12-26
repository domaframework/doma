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
package org.seasar.doma.internal.jdbc.command;

import java.util.Arrays;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.query.AutoBatchDeleteQuery;

import example.entity.Emp;
import example.entity._Emp;

/**
 * @author taedium
 * 
 */
public class BatchDeleteCommandTest extends TestCase {

    private final MockConfig runtimeConfig = new MockConfig();

    public void testExecute() throws Exception {
        Emp emp1 = new Emp();
        emp1.setId(1);
        emp1.setName("hoge");
        emp1.setVersion(10);

        Emp emp2 = new Emp();
        emp2.setId(2);
        emp2.setName("foo");
        emp2.setVersion(20);

        AutoBatchDeleteQuery<Emp> query = new AutoBatchDeleteQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntities(Arrays.asList(emp1, emp2));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();
        int[] rows = new BatchDeleteCommand(query).execute();
        query.complete();

        assertEquals(2, rows.length);
        String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
        assertEquals("delete from EMP where ID = ? and VERSION = ?", sql);
    }

}
