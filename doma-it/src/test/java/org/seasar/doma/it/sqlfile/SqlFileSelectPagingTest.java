package org.seasar.doma.it.sqlfile;

import static junit.framework.Assert.*;

import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDao_;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.entity.Employee;
import org.seasar.framework.unit.Seasar2;

import doma.jdbc.SelectOptions;

@RunWith(Seasar2.class)
public class SqlFileSelectPagingTest {

    public void testNoPaging() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        List<Employee> employees = dao.selectAll();
        assertEquals(14, employees.size());
    }

    public void testLimitOffset() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        List<Employee> employees = dao.selectAll(SelectOptions.get().limit(5)
                .offset(3));
        assertEquals(5, employees.size());
        assertEquals(new IdDomain(4), employees.get(0).employee_id());
        assertEquals(new IdDomain(5), employees.get(1).employee_id());
        assertEquals(new IdDomain(6), employees.get(2).employee_id());
        assertEquals(new IdDomain(7), employees.get(3).employee_id());
        assertEquals(new IdDomain(8), employees.get(4).employee_id());
    }

    public void testLimitOffset_offsetIsZero() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        List<Employee> employees = dao.selectAll(SelectOptions.get().limit(5)
                .offset(0));
        assertEquals(5, employees.size());
        assertEquals(new IdDomain(1), employees.get(0).employee_id());
        assertEquals(new IdDomain(2), employees.get(1).employee_id());
        assertEquals(new IdDomain(3), employees.get(2).employee_id());
        assertEquals(new IdDomain(4), employees.get(3).employee_id());
        assertEquals(new IdDomain(5), employees.get(4).employee_id());
    }

    public void testLimitOffset_limitIsZero() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        List<Employee> employees = dao.selectAll(SelectOptions.get().limit(0)
                .offset(10));
        assertEquals(4, employees.size());
        assertEquals(new IdDomain(11), employees.get(0).employee_id());
        assertEquals(new IdDomain(12), employees.get(1).employee_id());
        assertEquals(new IdDomain(13), employees.get(2).employee_id());
        assertEquals(new IdDomain(14), employees.get(3).employee_id());
    }

    public void testLimitOnly() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        List<Employee> employees = dao.selectAll(SelectOptions.get().limit(5));
        assertEquals(5, employees.size());
        assertEquals(new IdDomain(1), employees.get(0).employee_id());
        assertEquals(new IdDomain(2), employees.get(1).employee_id());
        assertEquals(new IdDomain(3), employees.get(2).employee_id());
        assertEquals(new IdDomain(4), employees.get(3).employee_id());
        assertEquals(new IdDomain(5), employees.get(4).employee_id());
    }

    public void testOffsetOnly() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        List<Employee> employees = dao
                .selectAll(SelectOptions.get().offset(10));
        assertEquals(4, employees.size());
        assertEquals(new IdDomain(11), employees.get(0).employee_id());
        assertEquals(new IdDomain(12), employees.get(1).employee_id());
        assertEquals(new IdDomain(13), employees.get(2).employee_id());
        assertEquals(new IdDomain(14), employees.get(3).employee_id());
    }

}
