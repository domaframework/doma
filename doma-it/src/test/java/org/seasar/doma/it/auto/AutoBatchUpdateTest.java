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

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.CompKeyDepartmentDao;
import org.seasar.doma.it.dao.CompKeyDepartmentDaoImpl;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDaoImpl;
import org.seasar.doma.it.dao.NoIdDao;
import org.seasar.doma.it.dao.NoIdDaoImpl;
import org.seasar.doma.it.entity.CompKeyDepartment;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.NoId;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.message.DomaMessageCode;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class AutoBatchUpdateTest {

    public void test() throws Exception {
        DepartmentDao dao = new DepartmentDaoImpl();
        Department department = new Department();
        department.setDepartmentId(1);
        department.setDepartmentNo(1);
        department.setDepartmentName("hoge");
        department.setVersion(1);
        Department department2 = new Department();
        department2.setDepartmentId(2);
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
        assertEquals(new Integer(1), department.getDepartmentId());
        assertEquals(new Integer(1), department.getDepartmentNo());
        assertEquals("hoge", department.getDepartmentName());
        assertNull(department.getLocation());
        assertEquals(new Integer(2), department.getVersion());
        department = dao.selectById(2);
        assertEquals(new Integer(2), department.getDepartmentId());
        assertEquals(new Integer(2), department.getDepartmentNo());
        assertEquals("foo", department.getDepartmentName());
        assertNull(department.getLocation());
        assertEquals(new Integer(2), department.getVersion());
    }

    public void testIncludesVersion() throws Exception {
        DepartmentDao dao = new DepartmentDaoImpl();
        Department department = new Department();
        department.setDepartmentId(1);
        department.setDepartmentNo(1);
        department.setDepartmentName("hoge");
        department.setVersion(100);
        Department department2 = new Department();
        department2.setDepartmentId(2);
        department2.setDepartmentNo(2);
        department2.setDepartmentName("foo");
        department2.setVersion(200);
        int[] result = dao.update_includesVersion(Arrays.asList(department,
                department2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(new Integer(100), department.getVersion());
        assertEquals(new Integer(200), department2.getVersion());

        department = dao.selectById(new Integer(1));
        assertEquals(new Integer(1), department.getDepartmentId());
        assertEquals(new Integer(1), department.getDepartmentNo());
        assertEquals("hoge", department.getDepartmentName());
        assertNull(department.getLocation());
        assertEquals(new Integer(100), department.getVersion());
        department = dao.selectById(new Integer(2));
        assertEquals(new Integer(2), department.getDepartmentId());
        assertEquals(new Integer(2), department.getDepartmentNo());
        assertEquals("foo", department.getDepartmentName());
        assertNull(department.getLocation());
        assertEquals(new Integer(200), department.getVersion());
    }

    public void testCompositeKey() throws Exception {
        CompKeyDepartmentDao dao = new CompKeyDepartmentDaoImpl();
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

    public void testOptimisticLockException() throws Exception {
        DepartmentDao dao = new DepartmentDaoImpl();
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

    public void testSuppressesOptimisticLockException() throws Exception {
        DepartmentDao dao = new DepartmentDaoImpl();
        Department department1 = dao.selectById(1);
        department1.setDepartmentName("hoge");
        Department department2 = dao.selectById(2);
        department2.setDepartmentName("foo");
        Department department3 = dao.selectById(1);
        department3.setDepartmentName("bar");
        dao.update(department1);
        dao.update_suppressesOptimisticLockException(Arrays.asList(department2,
                department3));
    }

    public void testNoId() throws Exception {
        NoIdDao dao = new NoIdDaoImpl();
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
            assertEquals(DomaMessageCode.DOMA2022, expected.getMessageCode());
        }
    }

    public void testSqlExecutionSkip() throws Exception {
        DepartmentDao dao = new DepartmentDaoImpl();
        int[] result = dao.update(new ArrayList<Department>());
        assertEquals(0, result.length);
    }
}
