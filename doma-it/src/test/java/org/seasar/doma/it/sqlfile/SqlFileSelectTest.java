package org.seasar.doma.it.sqlfile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.ResultMappingException;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SqlFileSelectTest {

    @Test
    public void testEmbeddedVariable() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        List<Employee> list = dao.selectWithOptionalOrderBy("S",
                "order by EMPLOYEE_ID");
        assertEquals(2, list.size());
        assertEquals(new Integer(1), list.get(0).getEmployeeId());
        assertEquals(new Integer(8), list.get(1).getEmployeeId());

        list = dao.selectWithOptionalOrderBy("S", "order by EMPLOYEE_ID desc");
        assertEquals(2, list.size());
        assertEquals(new Integer(8), list.get(0).getEmployeeId());
        assertEquals(new Integer(1), list.get(1).getEmployeeId());
    }

    @Test
    public void testNull() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        Employee employee = dao.selectById(9);
        assertNull(employee.getManagerId());
    }

    @Test
    public void testPrefixSearch() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        List<Employee> employees = dao.selectByNamePrefix("S");
        assertEquals(2, employees.size());
    }

    @Test
    public void testInsideSearch() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        List<Employee> employees = dao.selectByNameInfix("S");
        assertEquals(5, employees.size());
    }

    @Test
    public void testSuffixSearch() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        List<Employee> employees = dao.selectByNameSuffix("S");
        assertEquals(3, employees.size());
    }

    @Test
    public void testMap() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        Map<String, Object> employee = dao.selectByIdAsMap(1);
        assertNotNull(employee);
        assertNotNull(employee.get("employeeId"));
        assertNotNull(employee.get("employeeName"));
        assertNotNull(employee.get("hiredate"));
    }

    @Test
    public void testMapList() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        List<Map<String, Object>> employees = dao.selectAllAsMapList();
        assertEquals(14, employees.size());
    }

    @Test
    public void testEnsureResultMappping_false() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        List<Employee> employees = dao.selectOnlyNameWithoutMappingCheck();
        assertEquals(14, employees.size());
    }

    @Test
    public void testEnsureResultMappping_true() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        try {
            dao.selectOnlyNameWithMappingCheck();
            fail();
        } catch (ResultMappingException expected) {
            System.err.print(expected);
        }
    }
}
