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
import org.seasar.doma.it.dao.DepartmentDao_;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.NoDomain;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Department_;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SqlFileBatchInsertTest {

    public void test() throws Exception {
        DepartmentDao dao = new DepartmentDao_();
        Department department = new Department_();
        department.department_id().set(99);
        department.department_no().set(99);
        department.department_name().set("hoge");
        Department department2 = new Department_();
        department2.department_id().set(98);
        department2.department_no().set(98);
        department2.department_name().set("foo");
        int[] result = dao.insertBySqlFile(Arrays.asList(department,
                department2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);

        department = dao.selectById(new IdDomain(99));
        assertEquals(new IdDomain(99), department.department_id());
        assertEquals(new NoDomain(99), department.department_no());
        department = dao.selectById(new IdDomain(98));
        assertEquals(new IdDomain(98), department.department_id());
        assertEquals(new NoDomain(98), department.department_no());
    }

}
