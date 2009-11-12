package org.seasar.doma.it.sqlfile;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDaoImpl;
import org.seasar.doma.it.entity.Employee;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SqlFileSelectTest {

    public void testEmbeddedVariable() throws Exception {
        EmployeeDao dao = new EmployeeDaoImpl();
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

}
