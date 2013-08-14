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

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDaoImpl;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.jdbc.BatchOptimisticLockException;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SqlFileBatchUpdateTest {

    public void test() throws Exception {
        DepartmentDao dao = new DepartmentDaoImpl();
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
        int[] result = dao.updateBySqlFile(Arrays.asList(department,
                department2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);

        department = dao.selectById(1);
        assertEquals(new Integer(1), department.getDepartmentId().getValue());
        assertEquals("hoge", department.getDepartmentName());
        assertEquals(new Integer(2), department.getVersion());
        department = dao.selectById(2);
        assertEquals(new Integer(2), department.getDepartmentId().getValue());
        assertEquals("foo", department.getDepartmentName());
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
        dao.updateBySqlFile(department1);
        try {
            dao.updateBySqlFile(Arrays.asList(department2, department3));
            fail();
        } catch (BatchOptimisticLockException expected) {
        }
    }

    public void testSuppressOptimisticLockException() throws Exception {
        DepartmentDao dao = new DepartmentDaoImpl();
        Department department1 = dao.selectById(1);
        department1.setDepartmentName("hoge");
        Department department2 = dao.selectById(2);
        department2.setDepartmentName("foo");
        Department department3 = dao.selectById(1);
        department3.setDepartmentName("bar");
        dao.update(department1);
        dao.updateBySqlFile_suppressOptimisticLockException(Arrays.asList(
                department2, department3));
    }

}
