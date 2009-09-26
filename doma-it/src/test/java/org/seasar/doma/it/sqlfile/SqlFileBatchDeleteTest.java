package org.seasar.doma.it.sqlfile;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDaoImpl;
import org.seasar.doma.it.entity.Employee;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SqlFileBatchDeleteTest {

    public void test() throws Exception {
        EmployeeDao dao = new EmployeeDaoImpl();
        Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setVersion(1);
        Employee employee2 = new Employee();
        employee2.setEmployeeId(2);
        employee2.setVersion(1);
        int[] result = dao.deleteBySqlFile(Arrays.asList(employee, employee2));
        assertEquals(2, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);

        employee = dao.selectById(1);
        assertNull(employee);
        employee = dao.selectById(2);
        assertNull(employee);
    }
}
