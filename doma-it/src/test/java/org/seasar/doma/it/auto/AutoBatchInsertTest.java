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

import java.util.Arrays;

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
import org.seasar.doma.it.entity.CompKeyDepartment;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.IdentityStrategy;
import org.seasar.doma.it.entity.NoId;
import org.seasar.doma.it.entity.SequenceStrategy;
import org.seasar.doma.it.entity.TableStrategy;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.DomaMessageCode;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

@RunWith(Seasar2.class)
public class AutoBatchInsertTest {

    public void test() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department = new Department();
        department.setDepartment_id(99);
        department.setDepartment_no(99);
        department.setDepartment_name("hoge");
        Department department2 = new Department();
        department2.setDepartment_id(98);
        department2.setDepartment_no(98);
        department2.setDepartment_name("foo");
        int[] result = dao.insert(Arrays.asList(department, department2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(new Integer(1), department.getVersion());
        assertEquals(new Integer(1), department2.getVersion());

        department = dao.selectById(99);
        assertEquals(new Integer(99), department.getDepartment_id());
        assertEquals(new Integer(99), department.getDepartment_no());
        assertEquals("hoge", department.getDepartment_name());
        assertNull(department.getLocation());
        assertEquals(new Integer(1), department.getVersion());
        department = dao.selectById(new Integer(98));
        assertEquals(new Integer(98), department.getDepartment_id());
        assertEquals(new Integer(98), department.getDepartment_no());
        assertEquals("foo", department.getDepartment_name());
        assertNull(department.getLocation());
        assertEquals(new Integer(1), department.getVersion());
    }

    public void testCompositeKey() throws Exception {
        CompKeyDepartmentDao dao = new CompKeyDepartmentDao_();
        CompKeyDepartment department = new CompKeyDepartment();
        department.setDepartment_id1(99);
        department.setDepartment_id2(99);
        department.setDepartment_no(99);
        department.setDepartment_name("hoge");
        CompKeyDepartment department2 = new CompKeyDepartment();
        department2.setDepartment_id1(98);
        department2.setDepartment_id2(98);
        department2.setDepartment_no(98);
        department2.setDepartment_name("hoge");
        int[] result = dao.insert(Arrays.asList(department, department2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(new Integer(1), department.getVersion());
        assertEquals(new Integer(1), department2.getVersion());

        department = dao.selectById(new Integer(99), new Integer(99));
        assertEquals(new Integer(99), department.getDepartment_id1());
        assertEquals(new Integer(99), department.getDepartment_id2());
        assertEquals(new Integer(99), department.getDepartment_no());
        assertEquals("hoge", department.getDepartment_name());
        assertNull(department.getLocation());
        assertEquals(new Integer(1), department.getVersion());
        department = dao.selectById(98, 98);
        assertEquals(new Integer(98), department.getDepartment_id1());
        assertEquals(new Integer(98), department.getDepartment_id2());
        assertEquals(new Integer(98), department.getDepartment_no());
        assertEquals("hoge", department.getDepartment_name());
        assertNull(department.getLocation());
        assertEquals(new Integer(1), department.getVersion());
    }

    public void testIdNotAssigned() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department = new Department();
        department.setDepartment_no(99);
        department.setDepartment_name("hoge");
        Department department2 = new Department();
        department2.setDepartment_no(98);
        department2.setDepartment_name("hoge");
        try {
            dao.insert(Arrays.asList(department, department2));
            fail();
        } catch (JdbcException expected) {
            assertEquals(DomaMessageCode.DOMA2020, expected.getMessageCode());
        }
    }

    @Prerequisite("#ENV not in {'oracle'}")
    public void testId_Identity() throws Exception {
        IdentityStrategyDao dao = new IdentityStrategyDao_();
        for (int i = 0; i < 110; i++) {
            IdentityStrategy entity = new IdentityStrategy();
            IdentityStrategy entity2 = new IdentityStrategy();
            dao.insert(Arrays.asList(entity, entity2));
            assertNotNull(entity.getId());
            assertNotNull(entity2.getId());
            assertTrue(entity.getId() < entity2.getId());
        }
    }

    @Prerequisite("#ENV not in {'mysql'}")
    public void testId_sequence() throws Exception {
        SequenceStrategyDao dao = new SequenceStrategyDao_();
        for (int i = 0; i < 110; i++) {
            SequenceStrategy entity = new SequenceStrategy();
            SequenceStrategy entity2 = new SequenceStrategy();
            dao.insert(Arrays.asList(entity, entity2));
            assertNotNull(entity.getId());
            assertNotNull(entity2.getId());
            assertTrue(entity.getId() < entity2.getId());
        }
    }

    public void testId_table() throws Exception {
        TableStrategyDao dao = new TableStrategyDao_();
        for (int i = 0; i < 110; i++) {
            TableStrategy entity = new TableStrategy();
            TableStrategy entity2 = new TableStrategy();
            dao.insert(Arrays.asList(entity, entity2));
            assertNotNull(entity.getId());
            assertNotNull(entity2.getId());
            assertTrue(entity.getId() < entity2.getId());
        }
    }

    public void testNoId() throws Exception {
        NoIdDao dao = new NoIdDao_();
        NoId entity = new NoId();
        entity.setValue1(1);
        entity.setValue2(2);
        NoId entity2 = new NoId();
        entity2.setValue1(1);
        entity2.setValue2(2);
        int[] result = dao.insert(Arrays.asList(entity, entity2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
    }
}
