package org.seasar.doma.it.auto;

import static junit.framework.Assert.*;

import java.sql.Time;
import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDao_;
import org.seasar.doma.it.dao.FunctionDao;
import org.seasar.doma.it.dao.FunctionDao_;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.NameDomain;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Employee;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

import doma.domain.IntegerDomain;
import doma.domain.TimeDomain;

@RunWith(Seasar2.class)
@Prerequisite("#ENV not in {'hsqldb'}")
public class AutoFunctionTest {

    public void testNoParam() throws Exception {
        FunctionDao dao = new FunctionDao_();
        IntegerDomain result = dao.func_none_param();
        assertEquals(new IntegerDomain(10), result);
    }

    public void testOneParam() throws Exception {
        FunctionDao dao = new FunctionDao_();
        IntegerDomain result = dao.func_simpletype_param(new IntegerDomain(10));
        assertEquals(new IntegerDomain(20), result);
    }

    public void testOneParam_time() throws Exception {
        FunctionDao dao = new FunctionDao_();
        TimeDomain result = dao.func_simpletype_time_param(new TimeDomain(Time
                .valueOf("12:34:56")));
        assertEquals(new TimeDomain(Time.valueOf("12:34:56")), result);
    }

    public void testTwoParams() throws Exception {
        FunctionDao dao = new FunctionDao_();
        IntegerDomain result = dao.func_dto_param(new IntegerDomain(10),
                new IntegerDomain(20));
        assertEquals(new IntegerDomain(30), result);
    }

    public void testTwoParams_time() throws Exception {
        FunctionDao dao = new FunctionDao_();
        TimeDomain result = dao.func_dto_time_param(new TimeDomain(Time
                .valueOf("12:34:56")), new IntegerDomain(20));
        assertEquals(new TimeDomain(Time.valueOf("12:34:56")), result);
    }

    @Prerequisite("#ENV not in {'mysql'}")
    public void testResultSet() throws Exception {
        FunctionDao dao = new FunctionDao_();
        List<Employee> result = dao.func_resultset(new IdDomain(1));
        assertEquals(13, result.size());
    }

    @Prerequisite("#ENV not in {'mysql'}")
    public void testResultSetAndUpdate() throws Exception {
        FunctionDao dao = new FunctionDao_();
        List<Employee> result = dao.func_resultset_update(new IdDomain(1));
        assertEquals(13, result.size());
        DepartmentDao departmentDao = new DepartmentDao_();
        Department department = departmentDao.selectById(new IdDomain(1));
        assertEquals(new NameDomain("HOGE"), department.department_name());
    }

    @Prerequisite("#ENV not in {'mysql'}")
    public void testResultSetAndUpdate2() throws Exception {
        FunctionDao dao = new FunctionDao_();
        List<Employee> result = dao.func_resultset_update2(new IdDomain(1));
        assertEquals(13, result.size());
        DepartmentDao departmentDao = new DepartmentDao_();
        Department department = departmentDao.selectById(new IdDomain(1));
        assertEquals(new NameDomain("HOGE"), department.department_name());
    }

}
