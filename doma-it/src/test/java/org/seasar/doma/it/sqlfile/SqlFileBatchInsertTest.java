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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DeptDao;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Dept;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SqlFileBatchInsertTest {

    @Test
    public void test() throws Exception {
        DepartmentDao dao = DepartmentDao.get();
        Department department = new Department();
        department.setDepartmentId(new Identity<Department>(99));
        department.setDepartmentNo(99);
        department.setDepartmentName("hoge");
        Department department2 = new Department();
        department2.setDepartmentId(new Identity<Department>(98));
        department2.setDepartmentNo(98);
        department2.setDepartmentName("foo");
        int[] result = dao.insertBySqlFile(Arrays.asList(department,
                department2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);

        department = dao.selectById(99);
        assertEquals(new Integer(99), department.getDepartmentId().getValue());
        assertEquals(new Integer(99), department.getDepartmentNo());
        department = dao.selectById(98);
        assertEquals(new Integer(98), department.getDepartmentId().getValue());
        assertEquals(new Integer(98), department.getDepartmentNo());
    }

    @Test
    public void testImmutable() throws Exception {
        DeptDao dao = DeptDao.get();
        Dept dept = new Dept(new Identity<Dept>(99), 99, "hoge", null, null);
        Dept dept2 = new Dept(new Identity<Dept>(98), 98, "foo", null, null);
        BatchResult<Dept> result = dao.insertBySqlFile(Arrays.asList(dept,
                dept2));
        int[] counts = result.getCounts();
        assertEquals(2, counts.length);
        assertEquals(1, counts[0]);
        assertEquals(1, counts[1]);
        dept = result.getEntities().get(0);
        assertEquals("hoge_preI_postI", dept.getDepartmentName());
        dept2 = result.getEntities().get(1);
        assertEquals("foo_preI_postI", dept2.getDepartmentName());

        dept = dao.selectById(99);
        assertEquals(new Integer(99), dept.getDepartmentId().getValue());
        assertEquals(new Integer(99), dept.getDepartmentNo());
        assertEquals("hoge_preI", dept.getDepartmentName());
        dept2 = dao.selectById(98);
        assertEquals(new Integer(98), dept2.getDepartmentId().getValue());
        assertEquals(new Integer(98), dept2.getDepartmentNo());
        assertEquals("foo_preI", dept2.getDepartmentName());
    }
}
