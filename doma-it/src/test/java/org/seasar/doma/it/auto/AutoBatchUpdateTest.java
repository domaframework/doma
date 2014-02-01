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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.CompKeyDepartmentDao;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DeptDao;
import org.seasar.doma.it.dao.NoIdDao;
import org.seasar.doma.it.dao.WorkerDao;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.entity.CompKeyDepartment;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Dept;
import org.seasar.doma.it.entity.NoId;
import org.seasar.doma.it.entity.Worker;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.message.Message;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class AutoBatchUpdateTest {

    @Test
    public void test() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        Department department = new Department();
        department.setDepartmentId(new Identity<Department>(1));
        department.setDepartmentNo(1);
        department.setDepartmentName("hoge");
        department.setVersion(1);
        Department department2 = new Department();
        department2.setDepartmentId(new Identity<Department>(2));
        department2.setDepartmentNo(2);
        department2.setDepartmentName("foo");
        department2.setVersion(1);
        int[] result = dao.update(Arrays.asList(department, department2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(new Integer(2), department.getVersion());
        assertEquals(new Integer(2), department2.getVersion());

        department = dao.selectById(1);
        assertEquals(new Integer(1), department.getDepartmentId().getValue());
        assertEquals(new Integer(1), department.getDepartmentNo());
        assertEquals("hoge", department.getDepartmentName());
        assertNull(department.getLocation().getValue());
        assertEquals(new Integer(2), department.getVersion());
        department = dao.selectById(2);
        assertEquals(new Integer(2), department.getDepartmentId().getValue());
        assertEquals(new Integer(2), department.getDepartmentNo());
        assertEquals("foo", department.getDepartmentName());
        assertNull(department.getLocation().getValue());
        assertEquals(new Integer(2), department.getVersion());
    }

    @Test
    public void testImmutable() throws Exception {
        DeptDao dao = DeptDao.get();
        Dept dept = new Dept(new Identity<Dept>(1), 1, "hoge", null, 1);
        Dept dept2 = new Dept(new Identity<Dept>(2), 2, "foo", null, 1);
        BatchResult<Dept> result = dao.update(Arrays.asList(dept, dept2));
        int[] counts = result.getCounts();
        assertEquals(2, counts.length);
        assertEquals(1, counts[0]);
        assertEquals(1, counts[1]);
        dept = result.getEntities().get(0);
        dept2 = result.getEntities().get(1);
        assertEquals(new Integer(2), dept.getVersion());
        assertEquals("hoge_preU_postU", dept.getDepartmentName());
        assertEquals(new Integer(2), dept2.getVersion());
        assertEquals("foo_preU_postU", dept2.getDepartmentName());

        dept = dao.selectById(1);
        assertEquals(new Integer(1), dept.getDepartmentId().getValue());
        assertEquals(new Integer(1), dept.getDepartmentNo());
        assertEquals("hoge_preU", dept.getDepartmentName());
        assertNull(dept.getLocation().getValue());
        assertEquals(new Integer(2), dept.getVersion());
        dept = dao.selectById(2);
        assertEquals(new Integer(2), dept.getDepartmentId().getValue());
        assertEquals(new Integer(2), dept.getDepartmentNo());
        assertEquals("foo_preU", dept.getDepartmentName());
        assertNull(dept.getLocation().getValue());
        assertEquals(new Integer(2), dept.getVersion());
    }

    @Test
    public void testIncludeVersion() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        Department department = new Department();
        department.setDepartmentId(new Identity<Department>(1));
        department.setDepartmentNo(1);
        department.setDepartmentName("hoge");
        department.setVersion(100);
        Department department2 = new Department();
        department2.setDepartmentId(new Identity<Department>(2));
        department2.setDepartmentNo(2);
        department2.setDepartmentName("foo");
        department2.setVersion(200);
        int[] result = dao.update_ignoreVersion(Arrays.asList(department,
                department2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(new Integer(100), department.getVersion());
        assertEquals(new Integer(200), department2.getVersion());

        department = dao.selectById(new Integer(1));
        assertEquals(new Integer(1), department.getDepartmentId().getValue());
        assertEquals(new Integer(1), department.getDepartmentNo());
        assertEquals("hoge", department.getDepartmentName());
        assertNull(department.getLocation().getValue());
        assertEquals(new Integer(100), department.getVersion());
        department = dao.selectById(new Integer(2));
        assertEquals(new Integer(2), department.getDepartmentId().getValue());
        assertEquals(new Integer(2), department.getDepartmentNo());
        assertEquals("foo", department.getDepartmentName());
        assertNull(department.getLocation().getValue());
        assertEquals(new Integer(200), department.getVersion());
    }

    public void testCompositeKey() throws Exception {
        CompKeyDepartmentDao dao = CompKeyDepartmentDao.get();
        CompKeyDepartment department = new CompKeyDepartment();
        department.setDepartmentId1(1);
        department.setDepartmentId2(1);
        department.setDepartmentNo(1);
        department.setDepartmentName("hoge");
        department.setVersion(1);
        CompKeyDepartment department2 = new CompKeyDepartment();
        department2.setDepartmentId1(2);
        department2.setDepartmentId2(2);
        department2.setDepartmentNo(2);
        department2.setDepartmentName("foo");
        department2.setVersion(1);
        int[] result = dao.update(Arrays.asList(department, department2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(new Integer(2), department.getVersion());

        department = dao.selectById(1, 1);
        assertEquals(new Integer(1), department.getDepartmentId1());
        assertEquals(new Integer(1), department.getDepartmentId2());
        assertEquals(new Integer(1), department.getDepartmentNo());
        assertEquals("hoge", department.getDepartmentName());
        assertNull(department.getLocation());
        assertEquals(new Integer(2), department.getVersion());
        department = dao.selectById(2, 2);
        assertEquals(new Integer(2), department.getDepartmentId1());
        assertEquals(new Integer(2), department.getDepartmentId2());
        assertEquals(new Integer(2), department.getDepartmentNo());
        assertEquals("foo", department.getDepartmentName());
        assertNull(department.getLocation());
        assertEquals(new Integer(2), department.getVersion());
    }

    @Test
    public void testOptimisticLockException() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        Department department1 = dao.selectById(1);
        department1.setDepartmentName("hoge");
        Department department2 = dao.selectById(2);
        department2.setDepartmentName("foo");
        Department department3 = dao.selectById(1);
        department3.setDepartmentName("bar");
        dao.update(department1);
        try {
            dao.update(Arrays.asList(department2, department3));
            fail();
        } catch (OptimisticLockException expected) {
        }
    }

    @Test
    public void testSuppressOptimisticLockException() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        Department department1 = dao.selectById(1);
        department1.setDepartmentName("hoge");
        Department department2 = dao.selectById(2);
        department2.setDepartmentName("foo");
        Department department3 = dao.selectById(1);
        department3.setDepartmentName("bar");
        dao.update(department1);
        dao.update_suppressOptimisticLockException(Arrays.asList(department2,
                department3));
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
            dao.update(Arrays.asList(entity, entity2));
            fail();
        } catch (JdbcException expected) {
            assertEquals(Message.DOMA2022, expected.getMessageResource());
        }
    }

    @Test
    public void testSqlExecutionSkip() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        int[] result = dao.update(new ArrayList<Department>());
        assertEquals(0, result.length);
    }

    @Test
    public void testOptional() throws Exception {
        WorkerDao dao = WorkerDao.get();
        Worker worker = new Worker();
        worker.employeeId = Optional.of(1);
        worker.employeeNo = Optional.of(5555);
        worker.version = Optional.of(1);
        Worker worker2 = new Worker();
        worker2.employeeId = Optional.of(2);
        worker2.employeeNo = Optional.of(6666);
        worker2.version = Optional.of(1);
        int[] result = dao.update(Arrays.asList(worker, worker2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(new Integer(2), worker.version.get());
        assertEquals(new Integer(2), worker2.version.get());

        worker = dao.selectById(Optional.of(1));
        assertEquals(new Integer(1), worker.employeeId.get());
        assertEquals(new Integer(5555), worker.employeeNo.get());
        assertEquals(new Integer(2), worker.version.get());
        worker = dao.selectById(Optional.of(2));
        assertEquals(new Integer(2), worker.employeeId.get());
        assertEquals(new Integer(6666), worker.employeeNo.get());
        assertEquals(new Integer(2), worker.version.get());
    }

}
