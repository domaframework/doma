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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.message.Message;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

@RunWith(Seasar2.class)
public class SqlFileSelectForUpdateTest {

    @Test
    @Prerequisite("#ENV not in {'h2', 'postgres', 'oracle', 'mysql', 'db2', 'mssql2008'}")
    public void testUnsupported() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        try {
            dao.selectById(1, SelectOptions.get().forUpdate());
            fail();
        } catch (JdbcException expected) {
            assertEquals(Message.DOMA2023, expected.getMessageResource());
        }
    }

    @Test
    @Prerequisite("#ENV not in {'hsqldb', 'sqlite'}")
    public void testForUpdate() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        Employee employee = dao.selectById(1, SelectOptions.get().forUpdate());
        assertNotNull(employee);
    }

    @Test
    @Prerequisite("#ENV not in {'hsqldb', 'h2', 'postgres', 'mysql', 'db2', 'mssql2008', 'sqlite'}")
    public void testForUpdateWithColumns() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        Employee employee = dao.selectById(1,
                SelectOptions.get().forUpdate("employee_name", "address_id"));
        assertNotNull(employee);
    }

    @Test
    @Prerequisite("#ENV not in {'hsqldb', 'h2', 'oracle', 'mysql', 'db2', 'mssql2008', 'sqlite'}")
    public void testForUpdateWithTables() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        Employee employee = dao.selectById(1,
                SelectOptions.get().forUpdate("employee"));
        assertNotNull(employee);
    }

    @Test
    @Prerequisite("#ENV not in {'hsqldb', 'h2', 'postgres', 'mysql', 'db2', 'sqlite'}")
    public void testForUpdateNowait() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        Employee employee = dao.selectById(1, SelectOptions.get()
                .forUpdateNowait());
        assertNotNull(employee);
    }

    @Test
    @Prerequisite("#ENV not in {'hsqldb', 'h2', 'postgres', 'mysql', 'db2', 'mssql2008', 'sqlite'}")
    public void testForUpdateNowaitWithColumns() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        Employee employee = dao.selectById(1, SelectOptions.get()
                .forUpdateNowait("employee_name", "address_id"));
        assertNotNull(employee);
    }

    @Test
    @Prerequisite("#ENV not in {'hsqldb', 'h2', 'postgres', 'mysql', 'db2', 'mssql2008', 'sqlite'}")
    public void testForUpdateWait() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        Employee employee = dao.selectById(1, SelectOptions.get()
                .forUpdateWait(10));
        assertNotNull(employee);
    }

    @Prerequisite("#ENV not in {'hsqldb', 'h2', 'postgres', 'mysql', 'db2', 'mssql2008', 'sqlite'}")
    public void testForUpdateWaitWithColumns() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        Employee employee = dao.selectById(1, SelectOptions.get()
                .forUpdateWait(10, "employee_name", "address_id"));
        assertNotNull(employee);
    }
}
