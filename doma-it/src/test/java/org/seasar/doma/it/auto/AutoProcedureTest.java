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

import static junit.framework.Assert.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.doma.domain.BuiltinIntegerDomain;
import org.seasar.doma.domain.BuiltinTimeDomain;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDao_;
import org.seasar.doma.it.dao.ProcedureDao;
import org.seasar.doma.it.dao.ProcedureDao_;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.NameDomain;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Employee;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

@RunWith(Seasar2.class)
@Prerequisite("#ENV not in {'hsqldb'}")
public class AutoProcedureTest {

    public void testNoParam() throws Exception {
        ProcedureDao dao = new ProcedureDao_();
        dao.proc_none_param();
    }

    public void testOneParam() throws Exception {
        ProcedureDao dao = new ProcedureDao_();
        dao.proc_simpletype_param(new BuiltinIntegerDomain(10));
    }

    public void testOneParam_time() throws Exception {
        ProcedureDao dao = new ProcedureDao_();
        dao.proc_simpletype_time_param(new BuiltinTimeDomain(Time
                .valueOf("12:34:56")));
    }

    public void testIn_InOut_Out() throws Exception {
        ProcedureDao dao = new ProcedureDao_();
        BuiltinIntegerDomain param1 = new BuiltinIntegerDomain(10);
        BuiltinIntegerDomain param2 = new BuiltinIntegerDomain(20);
        BuiltinIntegerDomain param3 = new BuiltinIntegerDomain();
        dao.proc_dto_param(param1, param2, param3);
        assertEquals(new BuiltinIntegerDomain(10), param1);
        assertEquals(new BuiltinIntegerDomain(30), param2);
        assertEquals(new BuiltinIntegerDomain(10), param3);
    }

    public void testIn_InOut_Out_time() throws Exception {
        ProcedureDao dao = new ProcedureDao_();
        BuiltinTimeDomain param1 = new BuiltinTimeDomain(Time
                .valueOf("12:34:56"));
        BuiltinTimeDomain param2 = new BuiltinTimeDomain(Time
                .valueOf("01:23:45"));
        BuiltinTimeDomain param3 = new BuiltinTimeDomain();
        dao.proc_dto_time_param(param1, param2, param3);
        assertEquals(param1, param1);
        assertEquals(param1, param2);
        assertEquals(param1, param3);
    }

    public void testResultSet() throws Exception {
        ProcedureDao dao = new ProcedureDao_();
        List<Employee> employees = new ArrayList<Employee>();
        dao.proc_resultset(employees, new BuiltinIntegerDomain(1));
        assertEquals(13, employees.size());
    }

    public void testResultSet_Out() throws Exception {
        ProcedureDao dao = new ProcedureDao_();
        List<Employee> employees = new ArrayList<Employee>();
        BuiltinIntegerDomain count = new BuiltinIntegerDomain();
        dao.proc_resultset_out(employees, new BuiltinIntegerDomain(1), count);
        assertEquals(13, employees.size());
        assertEquals(new BuiltinIntegerDomain(14), count);
    }

    public void testResultSetAndUpdate() throws Exception {
        ProcedureDao dao = new ProcedureDao_();
        List<Employee> employees = new ArrayList<Employee>();
        dao.proc_resultset_update(employees, new BuiltinIntegerDomain(1));
        assertEquals(13, employees.size());
        DepartmentDao departmentDao = new DepartmentDao_();
        Department department = departmentDao.selectById(new IdDomain(1));
        assertEquals(new NameDomain("HOGE"), department.department_name());
    }

    public void testResultSetAndUpdate2() throws Exception {
        ProcedureDao dao = new ProcedureDao_();
        List<Employee> employees = new ArrayList<Employee>();
        dao.proc_resultset_update2(employees, new BuiltinIntegerDomain(1));
        assertEquals(13, employees.size());
        DepartmentDao departmentDao = new DepartmentDao_();
        Department department = departmentDao.selectById(new IdDomain(1));
        assertEquals(new NameDomain("HOGE"), department.department_name());
    }

    public void testResultSets() throws Exception {
        ProcedureDao dao = new ProcedureDao_();
        List<Employee> employees = new ArrayList<Employee>();
        List<Department> departments = new ArrayList<Department>();
        dao.proc_resultsets(employees, departments,
                new BuiltinIntegerDomain(1), new BuiltinIntegerDomain(1));
        assertEquals(13, employees.size());
        assertEquals(3, departments.size());
    }

    public void testResultSetAndUpdate_Out() throws Exception {
        ProcedureDao dao = new ProcedureDao_();
        List<Employee> employees = new ArrayList<Employee>();
        List<Department> departments = new ArrayList<Department>();
        BuiltinIntegerDomain count = new BuiltinIntegerDomain();
        dao
                .proc_resultsets_updates_out(employees, departments,
                        new BuiltinIntegerDomain(1),
                        new BuiltinIntegerDomain(1), count);
        assertEquals(13, employees.size());
        assertEquals(3, departments.size());
        assertEquals(new BuiltinIntegerDomain(14), count);
    }
}
