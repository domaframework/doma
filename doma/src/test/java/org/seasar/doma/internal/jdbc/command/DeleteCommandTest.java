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

import java.math.BigDecimal;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.BindValue;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockConnection;
import org.seasar.doma.internal.jdbc.mock.MockPreparedStatement;
import org.seasar.doma.internal.jdbc.query.AutoDeleteQuery;
import org.seasar.doma.jdbc.OptimisticLockException;

import example.entity.Emp;
import example.entity._Emp;

/**
 * @author taedium
 * 
 */
public class DeleteCommandTest extends TestCase {

    private final MockConfig runtimeConfig = new MockConfig();

    public void testExecute() throws Exception {
        Emp emp = new Emp();
        emp.setId(1);
        emp.setName("hoge");
        emp.setSalary(new BigDecimal(1000));
        emp.setVersion(10);

        AutoDeleteQuery<Emp> query = new AutoDeleteQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntity(emp);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();
        int rows = new DeleteCommand(query).execute();
        query.complete();

        assertEquals(1, rows);
        String sql = runtimeConfig.dataSource.connection.preparedStatement.sql;
        assertEquals("delete from EMP where ID = ? and VERSION = ?", sql);

        List<BindValue> bindValues = runtimeConfig.dataSource.connection.preparedStatement.bindValues;
        assertEquals(2, bindValues.size());
        assertEquals(new Integer(1), bindValues.get(0).getValue());
        assertEquals(new Integer(10), bindValues.get(1).getValue());
    }

    public void testExecute_throwsOptimisticLockException() throws Exception {
        Emp emp = new Emp();
        emp.setId(10);
        emp.setName("aaa");
        emp.setVersion(100);

        MockPreparedStatement ps = new MockPreparedStatement();
        ps.updatedRows = 0;
        runtimeConfig.dataSource.connection = new MockConnection(ps);
        AutoDeleteQuery<Emp> query = new AutoDeleteQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setMethod(getClass().getDeclaredMethod(getName()));
        query.setConfig(runtimeConfig);
        query.setEntity(emp);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();
        DeleteCommand command = new DeleteCommand(query);
        try {
            command.execute();
            fail();
        } catch (OptimisticLockException expected) {
        }
    }

    public void testExecute_suppressesOptimisticLockException()
            throws Exception {
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
        query.setOptimisticLockExceptionSuppressed(true);
        query.prepare();
        new DeleteCommand(query).execute();
        query.complete();
    }
}
