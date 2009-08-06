package org.seasar.doma.it.sqlfile;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDao_;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.it.entity.Employee_;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SqlFileBatchDeleteTest {

    public void test() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = new Employee_();
        employee.employee_id().set(1);
        employee.version().set(1);
        Employee employee2 = new Employee_();
        employee2.employee_id().set(2);
        employee2.version().set(1);
        int[] result = dao.deleteBySqlFile(Arrays.asList(employee, employee2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);

        employee = dao.selectById(new IdDomain(1));
        assertNull(employee);
        employee = dao.selectById(new IdDomain(2));
        assertNull(employee);
    }
}
