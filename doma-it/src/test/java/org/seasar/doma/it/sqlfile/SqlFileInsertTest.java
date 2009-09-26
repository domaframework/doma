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

import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDaoImpl;
import org.seasar.doma.it.entity.Department;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SqlFileInsertTest {

    public void test() throws Exception {
        DepartmentDao dao = new DepartmentDaoImpl();
        Department department = new Department();
        department.setDepartmentId(99);
        department.setDepartmentNo(99);
        department.setDepartmentName("hoge");
        int result = dao.insertBySqlFile(department);
        assertEquals(1, result);

        department = dao.selectById(new Integer(99));
        assertEquals(new Integer(99), department.getDepartmentId());
        assertEquals(new Integer(99), department.getDepartmentNo());
    }

}
