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

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.seasar.doma.DomaMessageCode;
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
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class AutoBatchDeleteTest {

    public void test() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = new Employee_();
        employee.employee_id().set(1);
        employee.version().set(1);
        Employee employee2 = new Employee_();
        employee2.employee_id().set(2);
        employee2.version().set(1);
        int[] result = dao.delete(Arrays.asList(employee, employee2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);

        employee = dao.selectById(new IdDomain(1));
        assertNull(employee);
        employee = dao.selectById(new IdDomain(2));
        assertNull(employee);
    }

    public void testIgnoresVersion() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = new Employee_();
        employee.employee_id().set(1);
        employee.version().set(99);
        Employee employee2 = new Employee_();
        employee2.employee_id().set(2);
        employee2.version().set(99);
        int[] result = dao.delete_ignoresVersion(Arrays.asList(employee,
                employee2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);

        employee = dao.selectById(new IdDomain(1));
        assertNull(employee);
        employee = dao.selectById(new IdDomain(2));
        assertNull(employee);
    }

    public void testCompositeKey() throws Exception {
        CompKeyEmployeeDao dao = new CompKeyEmployeeDao_();
        CompKeyEmployee employee = new CompKeyEmployee_();
        employee.employee_id1().set(1);
        employee.employee_id2().set(1);
        employee.version().set(1);
        CompKeyEmployee employee2 = new CompKeyEmployee_();
        employee2.employee_id1().set(2);
        employee2.employee_id2().set(2);
        employee2.version().set(1);

        int[] result = dao.delete(Arrays.asList(employee, employee2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);

        employee = dao.selectById(new IdDomain(1), new IdDomain(1));
        assertNull(employee);
        employee = dao.selectById(new IdDomain(2), new IdDomain(2));
        assertNull(employee);
    }

    public void testOptimisticLockException() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee1 = dao.selectById(new IdDomain(1));
        employee1.employee_name().set("hoge");
        Employee employee2 = dao.selectById(new IdDomain(2));
        employee2.employee_name().set("foo");
        Employee employee3 = dao.selectById(new IdDomain(1));
        employee2.employee_name().set("bar");
        dao.delete(employee1);
        try {
            dao.delete(Arrays.asList(employee2, employee3));
            fail();
        } catch (OptimisticLockException expected) {
        }
    }

    public void testSuppressesOptimisticLockException() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee1 = dao.selectById(new IdDomain(1));
        employee1.employee_name().set("hoge");
        Employee employee2 = dao.selectById(new IdDomain(2));
        employee2.employee_name().set("foo");
        Employee employee3 = dao.selectById(new IdDomain(1));
        employee2.employee_name().set("bar");
        dao.delete(employee1);
        dao.delete_suppressesOptimisticLockException(Arrays.asList(employee2,
                employee3));
    }

    public void testNoId() throws Exception {
        NoIdDao dao = new NoIdDao_();
        NoId entity = new NoId_();
        entity.value1().set(1);
        entity.value2().set(2);
        NoId entity2 = new NoId_();
        entity2.value1().set(1);
        entity2.value2().set(2);
        try {
            dao.delete(Arrays.asList(entity, entity2));
            fail();
        } catch (JdbcException expected) {
            assertEquals(DomaMessageCode.DOMA2022, expected.getMessageCode());
        }
    }
}
