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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.ProcedureDao;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.ResultMappingException;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

@RunWith(Seasar2.class)
@Prerequisite("#ENV not in {'hsqldb', 'h2', 'sqlite'}")
public class AutoProcedureTest {

    @Test
    public void testNoParam() throws Exception {
        ProcedureDao dao = ProcedureDao.get();
        dao.proc_none_param();
    }

    @Test
    public void testOneParam() throws Exception {
        ProcedureDao dao = ProcedureDao.get();
        dao.proc_simpletype_param(10);
    }

    @Test
    public void testOneParam_time() throws Exception {
        ProcedureDao dao = ProcedureDao.get();
        dao.proc_simpletype_time_param(Time.valueOf("12:34:56"));
    }

    @Test
    public void testIn_InOut_Out() throws Exception {
        ProcedureDao dao = ProcedureDao.get();
        Integer param1 = 10;
        Reference<Integer> param2 = new Reference<Integer>(20);
        Reference<Integer> param3 = new Reference<Integer>();
        dao.proc_dto_param(param1, param2, param3);
        assertEquals(new Integer(10), param1);
        assertEquals(new Integer(30), param2.get());
        assertEquals(new Integer(10), param3.get());
    }

    @Test
    public void testIn_InOut_Out_time() throws Exception {
        ProcedureDao dao = ProcedureDao.get();
        Time param1 = Time.valueOf("12:34:56");
        Reference<Time> param2 = new Reference<Time>(Time.valueOf("01:23:45"));
        Reference<Time> param3 = new Reference<Time>();
        dao.proc_dto_time_param(param1, param2, param3);
        assertEquals(param1, param1);
        assertEquals(param1, param2.get());
        assertEquals(param1, param3.get());
    }

    @Test
    public void testResultSet() throws Exception {
        ProcedureDao dao = ProcedureDao.get();
        List<Employee> employees = new ArrayList<Employee>();
        dao.proc_resultset(employees, 1);
        assertEquals(13, employees.size());
    }

    @Test
    public void testResultSet_check() throws Exception {
        ProcedureDao dao = ProcedureDao.get();
        List<Employee> employees = new ArrayList<Employee>();
        try {
            dao.proc_resultset_check(employees, 1);
            fail();
        } catch (ResultMappingException ignored) {
            System.err.println(ignored);
        }
    }

    @Test
    public void testResultSet_nocheck() throws Exception {
        ProcedureDao dao = ProcedureDao.get();
        List<Employee> employees = new ArrayList<Employee>();
        dao.proc_resultset_nocheck(employees, 1);
        assertEquals(13, employees.size());
    }

    @Test
    public void testResultSet_map() throws Exception {
        ProcedureDao dao = ProcedureDao.get();
        List<Map<String, Object>> employees = new ArrayList<Map<String, Object>>();
        dao.proc_resultset_map(employees, 1);
        assertEquals(13, employees.size());
    }

    @Test
    public void testResultSet_Out() throws Exception {
        ProcedureDao dao = ProcedureDao.get();
        List<Employee> employees = new ArrayList<Employee>();
        Reference<Integer> count = new Reference<Integer>();
        dao.proc_resultset_out(employees, 1, count);
        assertEquals(13, employees.size());
        assertEquals(new Integer(14), count.get());
    }

    @Test
    public void testResultSetAndUpdate() throws Exception {
        ProcedureDao dao = ProcedureDao.get();
        List<Employee> employees = new ArrayList<Employee>();
        dao.proc_resultset_update(employees, 1);
        assertEquals(13, employees.size());
        DepartmentDao departmentDao = DepartmentDao.get();
        Department department = departmentDao.selectById(1);
        assertEquals("HOGE", department.getDepartmentName());
    }

    @Test
    public void testResultSetAndUpdate2() throws Exception {
        ProcedureDao dao = ProcedureDao.get();
        List<Employee> employees = new ArrayList<Employee>();
        dao.proc_resultset_update2(employees, 1);
        assertEquals(13, employees.size());
        DepartmentDao departmentDao = DepartmentDao.get();
        Department department = departmentDao.selectById(1);
        assertEquals("HOGE", department.getDepartmentName());
    }

    @Test
    public void testResultSets() throws Exception {
        ProcedureDao dao = ProcedureDao.get();
        List<Employee> employees = new ArrayList<Employee>();
        List<Department> departments = new ArrayList<Department>();
        dao.proc_resultsets(employees, departments, 1, 1);
        assertEquals(13, employees.size());
        assertEquals(3, departments.size());
    }

    @Test
    public void testResultSetAndUpdate_Out() throws Exception {
        ProcedureDao dao = ProcedureDao.get();
        List<Employee> employees = new ArrayList<Employee>();
        List<Department> departments = new ArrayList<Department>();
        Reference<Integer> count = new Reference<Integer>();
        dao.proc_resultsets_updates_out(employees, departments, 1, 1, count);
        assertEquals(13, employees.size());
        assertEquals(3, departments.size());
        assertEquals(new Integer(14), count.get());
    }
}
