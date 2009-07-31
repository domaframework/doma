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
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.LocationDomain;
import org.seasar.doma.it.domain.NameDomain;
import org.seasar.doma.it.domain.NoDomain;
import org.seasar.doma.it.domain.VersionDomain;
import org.seasar.doma.it.entity.CompKeyDepartment;
import org.seasar.doma.it.entity.CompKeyDepartment_;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Department_;
import org.seasar.doma.it.entity.NoId;
import org.seasar.doma.it.entity.NoId_;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.message.DomaMessageCode;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class AutoUpdateTest {

    public void test() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department = new Department_();
        department.department_id().set(1);
        department.department_no().set(1);
        department.department_name().set("hoge");
        department.version().set(1);
        int result = dao.update(department);
        assertEquals(1, result);
        assertEquals(new VersionDomain(2), department.version());

        department = dao.selectById(new IdDomain(1));
        assertEquals(new IdDomain(1), department.department_id());
        assertEquals(new NoDomain(1), department.department_no());
        assertEquals(new NameDomain("hoge"), department.department_name());
        assertEquals(new LocationDomain("NEW YORK"), department.location());
        assertEquals(new VersionDomain(2), department.version());
    }

    public void testIncludesVersion() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department = new Department_();
        department.department_id().set(1);
        department.department_no().set(1);
        department.department_name().set("hoge");
        department.version().set(100);
        int result = dao.update_includesVersion(department);
        assertEquals(1, result);
        assertEquals(new VersionDomain(100), department.version());

        department = dao.selectById(new IdDomain(1));
        assertEquals(new IdDomain(1), department.department_id());
        assertEquals(new NoDomain(1), department.department_no());
        assertEquals(new NameDomain("hoge"), department.department_name());
        assertEquals(new LocationDomain("NEW YORK"), department.location());
        assertEquals(new VersionDomain(100), department.version());
    }

    public void testExcludesNull() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department = new Department_();
        department.department_id().set(1);
        department.department_no().set(1);
        department.department_name().setNull();
        department.version().set(1);
        int result = dao.update_excludesNull(department);
        assertEquals(1, result);

        department = dao.selectById(new IdDomain(1));
        assertEquals(new IdDomain(1), department.department_id());
        assertEquals(new NoDomain(1), department.department_no());
        assertEquals(new NameDomain("ACCOUNTING"), department.department_name());
        assertEquals(new LocationDomain("NEW YORK"), department.location());
        assertEquals(new VersionDomain(2), department.version());
    }

    public void testCompositeKey() throws Exception {
        CompKeyDepartmentDao dao = new CompKeyDepartmentDao_();
        CompKeyDepartment department = new CompKeyDepartment_();
        department.department_id1().set(1);
        department.department_id2().set(1);
        department.department_no().set(1);
        department.department_name().set("hoge");
        department.version().set(1);
        int result = dao.update(department);
        assertEquals(1, result);
        assertEquals(new VersionDomain(2), department.version());

        department = dao.selectById(new IdDomain(1), new IdDomain(1));
        assertEquals(new IdDomain(1), department.department_id1());
        assertEquals(new IdDomain(1), department.department_id2());
        assertEquals(new NoDomain(1), department.department_no());
        assertEquals(new NameDomain("hoge"), department.department_name());
        assertEquals(new LocationDomain("NEW YORK"), department.location());
        assertEquals(new VersionDomain(2), department.version());
    }

    public void testOptimisticLockException() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department1 = dao.selectById(new IdDomain(1));
        department1.department_name().set("hoge");
        Department department2 = dao.selectById(new IdDomain(1));
        department2.department_name().set("foo");
        dao.update(department1);
        try {
            dao.update(department2);
            fail();
        } catch (OptimisticLockException expected) {
        }
    }

    public void testSuppressesOptimisticLockException() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department1 = dao.selectById(new IdDomain(1));
        department1.department_name().set("hoge");
        Department department2 = dao.selectById(new IdDomain(1));
        department2.department_name().set("foo");
        dao.update(department1);
        dao.update_suppressesOptimisticLockException(department2);
    }

    public void testNoId() throws Exception {
        NoIdDao dao = new NoIdDao_();
        NoId entity = new NoId_();
        entity.value1().set(1);
        entity.value2().set(2);
        try {
            dao.update(entity);
            fail();
        } catch (JdbcException expected) {
            assertEquals(DomaMessageCode.DOMA2022, expected.getMessageCode());
        }
    }

    public void testSqlExecutionSkip() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department = dao.selectById(new IdDomain(1));
        int result = dao.update(department);
        assertEquals(0, result);
    }
}
