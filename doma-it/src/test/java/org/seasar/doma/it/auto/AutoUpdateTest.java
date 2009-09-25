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
import org.seasar.doma.it.dao.CompKeyDepartmentDao;
import org.seasar.doma.it.dao.CompKeyDepartmentDao_;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDao_;
import org.seasar.doma.it.dao.NoIdDao;
import org.seasar.doma.it.dao.NoIdDao_;
import org.seasar.doma.it.entity.CompKeyDepartment;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.NoId;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.message.DomaMessageCode;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class AutoUpdateTest {

    public void test() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department = new Department();
        department.setDepartment_id(1);
        department.setDepartment_no(1);
        department.setDepartment_name("hoge");
        department.setVersion(1);
        int result = dao.update(department);
        assertEquals(1, result);
        assertEquals(new Integer(2), department.getVersion());

        department = dao.selectById(new Integer(1));
        assertEquals(new Integer(1), department.getDepartment_id());
        assertEquals(new Integer(1), department.getDepartment_no());
        assertEquals("hoge", department.getDepartment_name());
        assertEquals("NEW YORK", department.getLocation());
        assertEquals(new Integer(2), department.getVersion());
    }

    public void testIncludesVersion() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department = new Department();
        department.setDepartment_id(1);
        department.setDepartment_no(1);
        department.setDepartment_name("hoge");
        department.setVersion(100);
        int result = dao.update_includesVersion(department);
        assertEquals(1, result);
        assertEquals(new Integer(100), department.getVersion());

        department = dao.selectById(1);
        assertEquals(new Integer(1), department.getDepartment_id());
        assertEquals(new Integer(1), department.getDepartment_no());
        assertEquals("hoge", department.getDepartment_name());
        assertEquals("NEW YORK", department.getLocation());
        assertEquals(new Integer(100), department.getVersion());
    }

    public void testExcludesNull() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department = new Department();
        department.setDepartment_id(1);
        department.setDepartment_no(1);
        department.setDepartment_name(null);
        department.setVersion(1);
        int result = dao.update_excludesNull(department);
        assertEquals(1, result);

        department = dao.selectById(new Integer(1));
        assertEquals(new Integer(1), department.getDepartment_id());
        assertEquals(new Integer(1), department.getDepartment_no());
        assertEquals("ACCOUNTING", department.getDepartment_name());
        assertEquals("NEW YORK", department.getLocation());
        assertEquals(new Integer(2), department.getVersion());
    }

    public void testCompositeKey() throws Exception {
        CompKeyDepartmentDao dao = new CompKeyDepartmentDao_();
        CompKeyDepartment department = new CompKeyDepartment();
        department.setDepartment_id1(1);
        department.setDepartment_id2(1);
        department.setDepartment_no(1);
        department.setDepartment_name("hoge");
        department.setVersion(1);
        int result = dao.update(department);
        assertEquals(1, result);
        assertEquals(new Integer(2), department.getVersion());

        department = dao.selectById(new Integer(1), new Integer(1));
        assertEquals(new Integer(1), department.getDepartment_id1());
        assertEquals(new Integer(1), department.getDepartment_id2());
        assertEquals(new Integer(1), department.getDepartment_no());
        assertEquals("hoge", department.getDepartment_name());
        assertEquals("NEW YORK", department.getLocation());
        assertEquals(new Integer(2), department.getVersion());
    }

    public void testOptimisticLockException() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department1 = dao.selectById(new Integer(1));
        department1.setDepartment_name("hoge");
        Department department2 = dao.selectById(new Integer(1));
        department2.setDepartment_name("foo");
        dao.update(department1);
        try {
            dao.update(department2);
            fail();
        } catch (OptimisticLockException expected) {
        }
    }

    public void testSuppressesOptimisticLockException() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department1 = dao.selectById(new Integer(1));
        department1.setDepartment_name("hoge");
        Department department2 = dao.selectById(new Integer(1));
        department2.setDepartment_name("foo");
        dao.update(department1);
        dao.update_suppressesOptimisticLockException(department2);
    }

    public void testNoId() throws Exception {
        NoIdDao dao = new NoIdDao_();
        NoId entity = new NoId();
        entity.setValue1(1);
        entity.setValue2(2);
        try {
            dao.update(entity);
            fail();
        } catch (JdbcException expected) {
            assertEquals(DomaMessageCode.DOMA2022, expected.getMessageCode());
        }
    }

    public void testSqlExecutionSkip() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department = dao.selectById(new Integer(1));
        int result = dao.update(department);
        assertEquals(0, result);
    }
}
