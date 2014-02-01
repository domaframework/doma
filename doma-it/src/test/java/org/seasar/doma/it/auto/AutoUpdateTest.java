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
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.CompKeyDepartmentDao;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DeptDao;
import org.seasar.doma.it.dao.NoIdDao;
import org.seasar.doma.it.dao.WorkerDao;
import org.seasar.doma.it.entity.CompKeyDepartment;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Dept;
import org.seasar.doma.it.entity.NoId;
import org.seasar.doma.it.entity.Worker;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.message.Message;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class AutoUpdateTest {

    @Test
    public void test() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        Department department = dao.selectById(1);
        department.setDepartmentNo(1);
        department.setDepartmentName("hoge");
        int result = dao.update(department);
        assertEquals(1, result);
        assertEquals(new Integer(2), department.getVersion());

        department = dao.selectById(1);
        assertEquals(new Integer(1), department.getDepartmentId().getValue());
        assertEquals(new Integer(1), department.getDepartmentNo());
        assertEquals("hoge", department.getDepartmentName());
        assertEquals("NEW YORK", department.getLocation().getValue());
        assertEquals(new Integer(2), department.getVersion());
    }

    @Test
    public void testImmutable() throws Exception {
        DeptDao dao = DeptDao.get();
        Dept dept = dao.selectById(1);
        dept = new Dept(dept.getDepartmentId(), 1, "hoge", dept.getLocation(),
                dept.getVersion());
        Result<Dept> result = dao.update(dept);
        assertEquals(1, result.getCount());
        dept = result.getEntity();
        assertEquals(new Integer(2), dept.getVersion());
        assertEquals("hoge_preU_postU", dept.getDepartmentName());

        dept = dao.selectById(1);
        assertEquals(new Integer(1), dept.getDepartmentId().getValue());
        assertEquals(new Integer(1), dept.getDepartmentNo());
        assertEquals("hoge_preU", dept.getDepartmentName());
        assertEquals("NEW YORK", dept.getLocation().getValue());
        assertEquals(new Integer(2), dept.getVersion());
    }

    @Test
    public void testIgnoreVersion() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        Department department = dao.selectById(1);
        department.setDepartmentNo(1);
        department.setDepartmentName("hoge");
        department.setVersion(100);
        int result = dao.update_ignoreVersion(department);
        assertEquals(1, result);
        assertEquals(new Integer(100), department.getVersion());

        department = dao.selectById(1);
        assertEquals(new Integer(1), department.getDepartmentId().getValue());
        assertEquals(new Integer(1), department.getDepartmentNo());
        assertEquals("hoge", department.getDepartmentName());
        assertEquals("NEW YORK", department.getLocation().getValue());
        assertEquals(new Integer(100), department.getVersion());
    }

    @Test
    public void testExcludeNull() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        Department department = dao.selectById(1);
        department.setDepartmentNo(1);
        department.setDepartmentName(null);
        int result = dao.update_excludeNull(department);
        assertEquals(1, result);

        department = dao.selectById(1);
        assertEquals(new Integer(1), department.getDepartmentId().getValue());
        assertEquals(new Integer(1), department.getDepartmentNo());
        assertEquals("ACCOUNTING", department.getDepartmentName());
        assertEquals("NEW YORK", department.getLocation().getValue());
        assertEquals(new Integer(2), department.getVersion());
    }

    @Test
    public void testCompositeKey() throws Exception {
        CompKeyDepartmentDao dao = CompKeyDepartmentDao.get();
        CompKeyDepartment department = dao.selectById(1, 1);
        department.setDepartmentNo(1);
        department.setDepartmentName("hoge");
        department.setVersion(1);
        int result = dao.update(department);
        assertEquals(1, result);
        assertEquals(new Integer(2), department.getVersion());

        department = dao.selectById(1, 1);
        assertEquals(new Integer(1), department.getDepartmentId1());
        assertEquals(new Integer(1), department.getDepartmentId2());
        assertEquals(new Integer(1), department.getDepartmentNo());
        assertEquals("hoge", department.getDepartmentName());
        assertEquals("NEW YORK", department.getLocation());
        assertEquals(new Integer(2), department.getVersion());
    }

    @Test
    public void testOptimisticLockException() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        Department department1 = dao.selectById(1);
        department1.setDepartmentName("hoge");
        Department department2 = dao.selectById(1);
        department2.setDepartmentName("foo");
        dao.update(department1);
        try {
            dao.update(department2);
            fail();
        } catch (OptimisticLockException expected) {
        }
    }

    @Test
    public void testSuppressOptimisticLockException() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        Department department1 = dao.selectById(1);
        department1.setDepartmentName("hoge");
        Department department2 = dao.selectById(1);
        department2.setDepartmentName("foo");
        dao.update(department1);
        int rows = dao.update_suppressOptimisticLockException(department2);
        assertEquals(0, rows);
    }

    @Test
    public void testNoId() throws Exception {
        NoIdDao dao = NoIdDao.get();
        NoId entity = new NoId();
        entity.setValue1(1);
        entity.setValue2(2);
        try {
            dao.update(entity);
            fail();
        } catch (JdbcException expected) {
            assertEquals(Message.DOMA2022, expected.getMessageResource());
        }
    }

    @Test
    public void testSqlExecutionSkip() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        Department department = dao.selectById(1);
        int result = dao.update(department);
        assertEquals(0, result);
    }

    @Test
    public void testOptional() throws Exception {
        WorkerDao dao = WorkerDao.get();
        Worker worker = dao.selectById(Optional.of(1));
        worker.employeeName = Optional.of("hoge");
        int result = dao.update(worker);
        assertEquals(1, result);
        assertEquals(new Integer(2), worker.version.get());

        worker = dao.selectById(Optional.of(1));
        assertEquals(new Integer(7369), worker.employeeNo.get());
        assertEquals(new Integer(2), worker.version.get());
        assertEquals("hoge", worker.employeeName.get());
        assertEquals(0,
                worker.salary.get().getValue().compareTo(new BigDecimal("800")));
        assertEquals(java.sql.Date.valueOf("1980-12-17"), worker.hiredate.get());
        assertEquals(Integer.valueOf(13), worker.managerId.get());
        assertEquals(Integer.valueOf(2), worker.departmentId.get().getValue());
        assertEquals(Integer.valueOf(1), worker.addressId.get());
    }

}
