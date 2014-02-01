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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.CompKeyDepartmentDao;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DeptDao;
import org.seasar.doma.it.dao.IdentityStrategyDao;
import org.seasar.doma.it.dao.NoIdDao;
import org.seasar.doma.it.dao.SequenceStrategyDao;
import org.seasar.doma.it.dao.TableStrategyDao;
import org.seasar.doma.it.dao.WorkerDao;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.domain.Location;
import org.seasar.doma.it.entity.CompKeyDepartment;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Dept;
import org.seasar.doma.it.entity.IdentityStrategy;
import org.seasar.doma.it.entity.NoId;
import org.seasar.doma.it.entity.SequenceStrategy;
import org.seasar.doma.it.entity.TableStrategy;
import org.seasar.doma.it.entity.Worker;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.UniqueConstraintException;
import org.seasar.doma.message.Message;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

@RunWith(Seasar2.class)
public class AutoInsertTest {

    @Test
    public void test() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        Department department = new Department();
        department.setDepartmentId(new Identity<Department>(99));
        department.setDepartmentNo(99);
        department.setDepartmentName("hoge");
        department.setLocation(new Location<Department>("foo"));
        int result = dao.insert(department);
        assertEquals(1, result);
        assertEquals(new Integer(1), department.getVersion());

        department = dao.selectById(new Integer(99));
        assertEquals(new Integer(99), department.getDepartmentId().getValue());
        assertEquals(new Integer(99), department.getDepartmentNo());
        assertEquals("hoge", department.getDepartmentName());
        assertEquals("foo", department.getLocation().getValue());
        assertEquals(new Integer(1), department.getVersion());
    }

    @Test
    public void testImmutable() throws Exception {
        DeptDao dao = DeptDao.get();
        Dept dept = new Dept(new Identity<Dept>(99), 99, "hoge",
                new Location<Dept>("foo"), null);
        Result<Dept> result = dao.insert(dept);
        assertEquals(1, result.getCount());
        dept = result.getEntity();
        assertEquals(new Integer(1), dept.getVersion());
        assertEquals("hoge_preI_postI", dept.getDepartmentName());

        dept = dao.selectById(new Integer(99));
        assertEquals(new Integer(99), dept.getDepartmentId().getValue());
        assertEquals(new Integer(99), dept.getDepartmentNo());
        assertEquals("hoge_preI", dept.getDepartmentName());
        assertEquals("foo", dept.getLocation().getValue());
        assertEquals(new Integer(1), dept.getVersion());
    }

    @Test
    public void test_UniqueConstraintException() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        Department department = new Department();
        department.setDepartmentId(new Identity<Department>(99));
        department.setDepartmentNo(99);
        department.setDepartmentName("hoge");
        int result = dao.insert(department);
        assertEquals(1, result);
        assertEquals(new Integer(1), department.getVersion());
        try {
            dao.insert(department);
            fail();
        } catch (UniqueConstraintException e) {
        }
    }

    @Test
    public void testExcludeNull() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        Department department = new Department();
        department.setDepartmentId(new Identity<Department>(99));
        department.setDepartmentNo(99);
        department.setDepartmentName("hoge");
        int result = dao.insert_excludeNull(department);
        assertEquals(1, result);
        assertEquals(new Integer(1), department.getVersion());

        department = dao.selectById(new Integer(99));
        assertEquals(new Integer(99), department.getDepartmentId().getValue());
        assertEquals(new Integer(99), department.getDepartmentNo());
        assertEquals("hoge", department.getDepartmentName());
        assertEquals("TOKYO", department.getLocation().getValue());
        assertEquals(new Integer(1), department.getVersion());
    }

    @Test
    public void testCompositeKey() throws Exception {
        CompKeyDepartmentDao dao = CompKeyDepartmentDao.get();
        CompKeyDepartment department = new CompKeyDepartment();
        department.setDepartmentId1(99);
        department.setDepartmentId2(99);
        department.setDepartmentNo(99);
        department.setDepartmentName("hoge");
        int result = dao.insert(department);
        assertEquals(1, result);
        assertEquals(new Integer(1), department.getVersion());

        department = dao.selectById(99, 99);
        assertEquals(new Integer(99), department.getDepartmentId1());
        assertEquals(new Integer(99), department.getDepartmentId2());
        assertEquals(new Integer(99), department.getDepartmentNo());
        assertEquals("hoge", department.getDepartmentName());
        assertNull(department.getLocation());
        assertEquals(new Integer(1), department.getVersion());
    }

    @Test
    public void testIdNotAssigned() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        Department department = new Department();
        department.setDepartmentNo(99);
        department.setDepartmentName("hoge");
        try {
            dao.insert(department);
            fail();
        } catch (JdbcException expected) {
            assertEquals(Message.DOMA2020, expected.getMessageResource());
        }
    }

    @Test
    @Prerequisite("#ENV not in {'oracle'}")
    public void testId_Identity() throws Exception {
        IdentityStrategyDao dao = IdentityStrategyDao.get();
        for (int i = 0; i < 110; i++) {
            IdentityStrategy entity = new IdentityStrategy();
            dao.insert(entity);
            assertNotNull(entity.getId());
        }
    }

    @Test
    @Prerequisite("#ENV not in {'mysql', 'mssql2008', 'sqlite'}")
    public void testId_sequence() throws Exception {
        SequenceStrategyDao dao = SequenceStrategyDao.get();
        for (int i = 0; i < 110; i++) {
            SequenceStrategy entity = new SequenceStrategy();
            dao.insert(entity);
            assertNotNull(entity.getId());
        }
    }

    // it seems that sqlite doesn't support requiresNew transaction
    // so ignore this test case
    @Test
    @Prerequisite("#ENV not in {'sqlite'}")
    public void testId_table() throws Exception {
        TableStrategyDao dao = TableStrategyDao.get();
        for (int i = 0; i < 110; i++) {
            TableStrategy entity = new TableStrategy();
            dao.insert(entity);
            assertNotNull(entity.getId());
        }
    }

    @Test
    public void testNoId() throws Exception {
        NoIdDao dao = NoIdDao.get();
        NoId entity = new NoId();
        entity.setValue1(1);
        entity.setValue2(2);
        int result = dao.insert(entity);
        assertEquals(1, result);
    }

    @Test
    public void testOptional() throws Exception {
        WorkerDao dao = WorkerDao.get();
        Worker worker = new Worker();
        worker.employeeId = Optional.of(9999);
        worker.employeeNo = Optional.of(9999);
        int result = dao.insert(worker);
        assertEquals(1, result);
        assertEquals(new Integer(1), worker.version.get());

        worker = dao.selectById(Optional.of(9999));
        assertEquals(new Integer(9999), worker.employeeNo.get());
        assertEquals(new Integer(1), worker.version.get());
        assertFalse(worker.employeeName.isPresent());
        assertFalse(worker.salary.isPresent());
        assertFalse(worker.hiredate.isPresent());
        assertFalse(worker.managerId.isPresent());
        assertFalse(worker.departmentId.isPresent());
        assertFalse(worker.addressId.isPresent());
    }

}
