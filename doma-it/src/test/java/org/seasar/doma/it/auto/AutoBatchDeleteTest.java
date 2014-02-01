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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.CompKeyEmployeeDao;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.NoIdDao;
import org.seasar.doma.it.dao.PersonDao;
import org.seasar.doma.it.dao.WorkerDao;
import org.seasar.doma.it.entity.CompKeyEmployee;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.it.entity.NoId;
import org.seasar.doma.it.entity.Person;
import org.seasar.doma.it.entity.Worker;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.message.Message;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class AutoBatchDeleteTest {

    @Test
    public void test() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
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

    @Test
    public void testImmutable() throws Exception {
        PersonDao dao = PersonDao.get();
        Person person = new Person(1, null, null, null, null, null, null, null,
                1);
        Person person2 = new Person(2, null, null, null, null, null, null,
                null, 1);
        BatchResult<Person> result = dao.delete(Arrays.asList(person, person2));
        int[] counts = result.getCounts();
        assertEquals(2, counts.length);
        assertEquals(1, counts[0]);
        assertEquals(1, counts[1]);
        person = result.getEntities().get(0);
        assertEquals("null_preD_postD", person.getEmployeeName());
        person2 = result.getEntities().get(0);
        assertEquals("null_preD_postD", person2.getEmployeeName());

        person = dao.selectById(1);
        assertNull(person);
        person2 = dao.selectById(2);
        assertNull(person2);
    }

    @Test
    public void testIgnoreVersion() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setVersion(99);
        Employee employee2 = new Employee();
        employee2.setEmployeeId(2);
        employee2.setVersion(99);
        ;
        int[] result = dao.delete_ignoreVersion(Arrays.asList(employee,
                employee2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);

        employee = dao.selectById(1);
        assertNull(employee);
        employee = dao.selectById(2);
        assertNull(employee);
    }

    @Test
    public void testCompositeKey() throws Exception {
        CompKeyEmployeeDao dao = CompKeyEmployeeDao.get();
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

    @Test
    public void testOptimisticLockException() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
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

    @Test
    public void testSuppressOptimisticLockException() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        Employee employee1 = dao.selectById(1);
        employee1.setEmployeeName("hoge");
        Employee employee2 = dao.selectById(2);
        employee2.setEmployeeName("foo");
        Employee employee3 = dao.selectById(1);
        employee2.setEmployeeName("bar");
        dao.delete(employee1);
        dao.delete_suppressOptimisticLockException(Arrays.asList(employee2,
                employee3));
    }

    @Test
    public void testNoId() throws Exception {
        NoIdDao dao = NoIdDao.get();
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
            assertEquals(Message.DOMA2022, expected.getMessageResource());
        }
    }

    @Test
    public void testOptional() throws Exception {
        WorkerDao dao = WorkerDao.get();
        Worker employee = new Worker();
        employee.employeeId = Optional.of(1);
        employee.version = Optional.of(1);
        Worker employee2 = new Worker();
        employee2.employeeId = Optional.of(2);
        employee2.version = Optional.of(1);
        int[] result = dao.delete(Arrays.asList(employee, employee2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);

        employee = dao.selectById(Optional.of(1));
        assertNull(employee);
        employee = dao.selectById(Optional.of(2));
        assertNull(employee);
    }

}
