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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SqlFileSelectCountTest {

    @Test
    public void test() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        SelectOptions options = SelectOptions.get().count();
        List<Employee> employees = dao.selectAll(options);
        assertEquals(14, employees.size());
        assertEquals(14, options.getCount());
    }

    @Test
    public void testCountUnspecified() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        SelectOptions options = SelectOptions.get();
        List<Employee> employees = dao.selectAll(options);
        assertEquals(14, employees.size());
        assertEquals(-1, options.getCount());
    }

    @Test
    public void testWhere() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        SelectOptions options = SelectOptions.get().count();
        Employee employee = dao.selectById(1, options);
        assertNotNull(employee);
        assertEquals(1, options.getCount());
    }

    @Test
    public void testLimitOffset() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        SelectOptions options = SelectOptions.get().limit(5).offset(3).count();
        List<Employee> employees = dao.selectAll(options);
        assertEquals(5, employees.size());
        assertEquals(14, options.getCount());
        assertEquals(new Integer(4), employees.get(0).getEmployeeId());
        assertEquals(new Integer(5), employees.get(1).getEmployeeId());
        assertEquals(new Integer(6), employees.get(2).getEmployeeId());
        assertEquals(new Integer(7), employees.get(3).getEmployeeId());
        assertEquals(new Integer(8), employees.get(4).getEmployeeId());
    }

}
