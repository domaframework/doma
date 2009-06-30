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
import org.seasar.doma.it.dao.IdentityStrategyDao;
import org.seasar.doma.it.dao.IdentityStrategyDao_;
import org.seasar.doma.it.dao.NoIdDao;
import org.seasar.doma.it.dao.NoIdDao_;
import org.seasar.doma.it.dao.SequenceStrategyDao;
import org.seasar.doma.it.dao.SequenceStrategyDao_;
import org.seasar.doma.it.dao.TableStrategyDao;
import org.seasar.doma.it.dao.TableStrategyDao_;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.LocationDomain;
import org.seasar.doma.it.domain.NameDomain;
import org.seasar.doma.it.domain.NoDomain;
import org.seasar.doma.it.domain.VersionDomain;
import org.seasar.doma.it.entity.CompKeyDepartment;
import org.seasar.doma.it.entity.CompKeyDepartment_;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Department_;
import org.seasar.doma.it.entity.IdentityStrategy;
import org.seasar.doma.it.entity.IdentityStrategy_;
import org.seasar.doma.it.entity.NoId;
import org.seasar.doma.it.entity.NoId_;
import org.seasar.doma.it.entity.SequenceStrategy;
import org.seasar.doma.it.entity.SequenceStrategy_;
import org.seasar.doma.it.entity.TableStrategy;
import org.seasar.doma.it.entity.TableStrategy_;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.MessageCode;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

@RunWith(Seasar2.class)
public class AutoInsertTest {

    public void test() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department = new Department_();
        department.department_id().set(99);
        department.department_no().set(99);
        department.department_name().set("hoge");
        int result = dao.insert(department);
        assertEquals(1, result);
        assertEquals(new VersionDomain(1), department.version());

        department = dao.selectById(new IdDomain(99));
        assertEquals(new IdDomain(99), department.department_id());
        assertEquals(new NoDomain(99), department.department_no());
        assertEquals(new NameDomain("hoge"), department.department_name());
        assertTrue(department.location().isNull());
        assertEquals(new VersionDomain(1), department.version());
    }

    public void testExcludesNull() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department = new Department_();
        department.department_id().set(99);
        department.department_no().set(99);
        department.department_name().set("hoge");
        int result = dao.insert_excludesNull(department);
        assertEquals(1, result);
        assertEquals(new VersionDomain(1), department.version());

        department = dao.selectById(new IdDomain(99));
        assertEquals(new IdDomain(99), department.department_id());
        assertEquals(new NoDomain(99), department.department_no());
        assertEquals(new NameDomain("hoge"), department.department_name());
        assertEquals(new LocationDomain("TOKYO"), department.location());
        assertEquals(new VersionDomain(1), department.version());
    }

    public void testCompositeKey() throws Exception {
        CompKeyDepartmentDao dao = new CompKeyDepartmentDao_();
        CompKeyDepartment department = new CompKeyDepartment_();
        department.department_id1().set(99);
        department.department_id2().set(99);
        department.department_no().set(99);
        department.department_name().set("hoge");
        int result = dao.insert(department);
        assertEquals(1, result);
        assertEquals(new VersionDomain(1), department.version());

        department = dao.selectById(new IdDomain(99), new IdDomain(99));
        assertEquals(new IdDomain(99), department.department_id1());
        assertEquals(new IdDomain(99), department.department_id2());
        assertEquals(new NoDomain(99), department.department_no());
        assertEquals(new NameDomain("hoge"), department.department_name());
        assertTrue(department.location().isNull());
        assertEquals(new VersionDomain(1), department.version());
    }

    public void testIdNotAssigned() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department = new Department_();
        department.department_no().set(99);
        department.department_name().set("hoge");
        try {
            dao.insert(department);
            fail();
        } catch (JdbcException expected) {
            assertEquals(MessageCode.DOMA2020, expected.getMessageCode());
        }
    }

    @Prerequisite("#ENV not in {'oracle'}")
    public void testId_Identity() throws Exception {
        IdentityStrategyDao dao = new IdentityStrategyDao_();
        for (int i = 0; i < 110; i++) {
            IdentityStrategy entity = new IdentityStrategy_();
            dao.insert(entity);
            assertTrue(entity.id().isNotNull());
        }
    }

    @Prerequisite("#ENV not in {'mysql'}")
    public void testId_sequence() throws Exception {
        SequenceStrategyDao dao = new SequenceStrategyDao_();
        for (int i = 0; i < 110; i++) {
            SequenceStrategy entity = new SequenceStrategy_();
            dao.insert(entity);
            assertTrue(entity.id().isNotNull());
        }
    }

    public void testId_table() throws Exception {
        TableStrategyDao dao = new TableStrategyDao_();
        for (int i = 0; i < 110; i++) {
            TableStrategy entity = new TableStrategy_();
            dao.insert(entity);
            assertTrue(entity.id().isNotNull());
        }
    }

    public void testNoId() throws Exception {
        NoIdDao dao = new NoIdDao_();
        NoId entity = new NoId_();
        entity.value1().set(1);
        entity.value2().set(2);
        int result = dao.insert(entity);
        assertEquals(1, result);
    }
}
