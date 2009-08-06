package org.seasar.doma.it.sqlfile;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDao_;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.it.entity.Employee_;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SqlFileDeleteTest {

    public void test() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = new Employee_();
        employee.employee_id().set(1);
        employee.version().set(1);
        int result = dao.deleteBySqlFile(employee);
        assertEquals(1, result);

        employee = dao.selectById(new IdDomain(1));
        assertNull(employee);
    }
}
