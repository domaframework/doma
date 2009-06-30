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
package org.seasar.doma.it.auto;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.CompKeyEmployeeDao;
import org.seasar.doma.it.dao.CompKeyEmployeeDao_;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDao_;
import org.seasar.doma.it.dao.NoIdDao;
import org.seasar.doma.it.dao.NoIdDao_;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.entity.CompKeyEmployee;
import org.seasar.doma.it.entity.CompKeyEmployee_;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.it.entity.Employee_;
import org.seasar.doma.it.entity.NoId;
import org.seasar.doma.it.entity.NoId_;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.message.MessageCode;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class AutoDeleteTest {

    public void test() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = new Employee_();
        employee.employee_id().set(1);
        employee.version().set(1);
        int result = dao.delete(employee);
        assertEquals(1, result);

        employee = dao.selectById(new IdDomain(1));
        assertNull(employee);
    }

    public void testIgnoresVersion() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = new Employee_();
        employee.employee_id().set(1);
        employee.version().set(99);
        int result = dao.delete_ignoresVersion(employee);
        assertEquals(1, result);

        employee = dao.selectById(new IdDomain(1));
        assertNull(employee);
    }

    public void testCompositeKey() throws Exception {
        CompKeyEmployeeDao dao = new CompKeyEmployeeDao_();
        CompKeyEmployee employee = new CompKeyEmployee_();
        employee.employee_id1().set(1);
        employee.employee_id2().set(1);
        employee.version().set(1);
        int result = dao.delete(employee);
        assertEquals(1, result);

        employee = dao.selectById(new IdDomain(1), new IdDomain(1));
        assertNull(employee);
    }

    public void testOptimisticLockException() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee1 = dao.selectById(new IdDomain(1));
        employee1.employee_name().set("hoge");
        Employee employee2 = dao.selectById(new IdDomain(1));
        employee2.employee_name().set("foo");
        dao.delete(employee1);
        try {
            dao.delete(employee2);
            fail();
        } catch (OptimisticLockException expected) {
        }
    }

    public void testSuppressesOptimisticLockException() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee1 = dao.selectById(new IdDomain(1));
        employee1.employee_name().set("hoge");
        Employee employee2 = dao.selectById(new IdDomain(1));
        employee2.employee_name().set("foo");
        dao.delete(employee1);
        dao.delete_suppressesOptimisticLockException(employee2);
    }

    public void testNoId() throws Exception {
        NoIdDao dao = new NoIdDao_();
        NoId entity = new NoId_();
        entity.value1().set(1);
        entity.value2().set(2);
        try {
            dao.delete(entity);
            fail();
        } catch (JdbcException expected) {
            assertEquals(MessageCode.DOMA2022, expected.getMessageCode());
        }
    }
}
