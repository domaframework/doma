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
package org.seasar.doma.it.sqlfile;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.seasar.doma.internal.message.Message;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDaoImpl;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

@RunWith(Seasar2.class)
public class SqlFileSelectForUpdateTest {

    @Prerequisite("#ENV not in {'h2', 'postgres', 'oracle', 'mysql'}")
    public void testUnsupported() throws Exception {
        EmployeeDao dao = new EmployeeDaoImpl();
        try {
            dao.selectById(1, SelectOptions.get().forUpdate());
            fail();
        } catch (JdbcException expected) {
            assertEquals(Message.DOMA2023, expected.getMessageResource());
        }
    }

    @Prerequisite("#ENV not in {'hsqldb', 'mysql'}")
    public void testForUpdate() throws Exception {
        EmployeeDao dao = new EmployeeDaoImpl();
        Employee employee = dao.selectById(1, SelectOptions.get().forUpdate());
        assertNotNull(employee);
    }

    @Prerequisite("#ENV not in {'hsqldb', 'postgres', 'mysql'}")
    public void testForUpdateWithColumns() throws Exception {
        EmployeeDao dao = new EmployeeDaoImpl();
        Employee employee = dao.selectById(1, SelectOptions.get().forUpdate(
                "employee_name", "address_id"));
        assertNotNull(employee);
    }

    @Prerequisite("#ENV not in {'hsqldb', 'oracle', 'mysql'}")
    public void testForUpdateWithTables() throws Exception {
        EmployeeDao dao = new EmployeeDaoImpl();
        Employee employee = dao.selectById(1, SelectOptions.get().forUpdate(
                "employee"));
        assertNotNull(employee);
    }

    @Prerequisite("#ENV not in {'hsqldb', 'postgres', 'mysql'}")
    public void testForUpdateNowait() throws Exception {
        EmployeeDao dao = new EmployeeDaoImpl();
        Employee employee = dao.selectById(1, SelectOptions.get()
                .forUpdateNowait());
        assertNotNull(employee);
    }

    @Prerequisite("#ENV not in {'hsqldb', 'postgres', 'mysql'}")
    public void testForUpdateNowaitWithColumns() throws Exception {
        EmployeeDao dao = new EmployeeDaoImpl();
        Employee employee = dao.selectById(1, SelectOptions.get()
                .forUpdateNowait("employee_name", "address_id"));
        assertNotNull(employee);
    }

    @Prerequisite("#ENV not in {'hsqldb', 'postgres', 'mysql'}")
    public void testForUpdateWait() throws Exception {
        EmployeeDao dao = new EmployeeDaoImpl();
        Employee employee = dao.selectById(1, SelectOptions.get()
                .forUpdateWait(10));
        assertNotNull(employee);
    }

    @Prerequisite("#ENV not in {'hsqldb', 'postgres', 'mysql'}")
    public void testForUpdateWaitWithColumns() throws Exception {
        EmployeeDao dao = new EmployeeDaoImpl();
        Employee employee = dao.selectById(1, SelectOptions.get()
                .forUpdateWait(10, "employee_name", "address_id"));
        assertNotNull(employee);
    }
}
