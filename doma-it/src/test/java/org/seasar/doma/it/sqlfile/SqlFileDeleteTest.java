package org.seasar.doma.it.sqlfile;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDao_;
import org.seasar.doma.it.entity.Employee;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SqlFileDeleteTest {

    public void test() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setVersion(1);
        int result = dao.deleteBySqlFile(employee);
        assertEquals(1, result);

        employee = dao.selectById(1);
        assertNull(employee);
    }
}
