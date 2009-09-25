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
import org.seasar.doma.it.dao.CompKeyEmployeeDao;
import org.seasar.doma.it.dao.CompKeyEmployeeDao_;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDao_;
import org.seasar.doma.it.dao.NoIdDao;
import org.seasar.doma.it.dao.NoIdDao_;
import org.seasar.doma.it.entity.CompKeyEmployee;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.it.entity.NoId;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.message.DomaMessageCode;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class AutoBatchDeleteTest {

    public void test() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setVersion(1);
        Employee employee2 = new Employee();
        employee2.setEmployeeId(2);
        employee2.setVersion(1);
        int[] result = dao.delete(Arrays.asList(employee, employee2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);

        employee = dao.selectById(1);
        assertNull(employee);
        employee = dao.selectById(2);
        assertNull(employee);
    }

    public void testIgnoresVersion() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setVersion(99);
        Employee employee2 = new Employee();
        employee2.setEmployeeId(2);
        employee2.setVersion(99);
        ;
        int[] result = dao.delete_ignoresVersion(Arrays.asList(employee,
                employee2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);

        employee = dao.selectById(1);
        assertNull(employee);
        employee = dao.selectById(2);
        assertNull(employee);
    }

    public void testCompositeKey() throws Exception {
        CompKeyEmployeeDao dao = new CompKeyEmployeeDao_();
        CompKeyEmployee employee = new CompKeyEmployee();
        employee.setEmployeeId1(1);
        employee.setEmployeeId2(1);
        employee.setVersion(1);
        CompKeyEmployee employee2 = new CompKeyEmployee();
        employee2.setEmployeeId1(2);
        employee2.setEmployeeId2(2);
        employee2.setVersion(1);

        int[] result = dao.delete(Arrays.asList(employee, employee2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);

        employee = dao.selectById(1, 1);
        assertNull(employee);
        employee = dao.selectById(2, 2);
        assertNull(employee);
    }

    public void testOptimisticLockException() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee1 = dao.selectById(1);
        employee1.setEmployeeName("hoge");
        Employee employee2 = dao.selectById(2);
        employee2.setEmployeeName("foo");
        Employee employee3 = dao.selectById(1);
        employee2.setEmployeeName("bar");
        dao.delete(employee1);
        try {
            dao.delete(Arrays.asList(employee2, employee3));
            fail();
        } catch (OptimisticLockException expected) {
        }
    }

    public void testSuppressesOptimisticLockException() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee1 = dao.selectById(1);
        employee1.setEmployeeName("hoge");
        Employee employee2 = dao.selectById(2);
        employee2.setEmployeeName("foo");
        Employee employee3 = dao.selectById(1);
        employee2.setEmployeeName("bar");
        dao.delete(employee1);
        dao.delete_suppressesOptimisticLockException(Arrays.asList(employee2,
                employee3));
    }

    public void testNoId() throws Exception {
        NoIdDao dao = new NoIdDao_();
        NoId entity = new NoId();
        entity.setValue1(1);
        entity.setValue2(2);
        NoId entity2 = new NoId();
        entity2.setValue1(1);
        entity2.setValue2(2);
        try {
            dao.delete(Arrays.asList(entity, entity2));
            fail();
        } catch (JdbcException expected) {
            assertEquals(DomaMessageCode.DOMA2022, expected.getMessageCode());
        }
    }
}
