package org.seasar.doma.it.sqlfile;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDao_;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.NameDomain;
import org.seasar.doma.it.domain.OrderBy;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.it.entity.Employee_;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SqlFileSelectTest {

    public void testEmbeddedVariable() throws Exception {
        EmployeeDao dao = new EmployeeDao_();
        OrderBy orderBy = new OrderBy();
        orderBy.set("order by EMPLOYEE_ID");
        List<Employee> list = dao.selectWithOptionalOrderBy(
                new NameDomain("S"), orderBy);
        assertEquals(2, list.size());
        assertEquals(new IdDomain(1), list.get(0).employee_id());
        assertEquals(new IdDomain(8), list.get(1).employee_id());

        orderBy.set("order by EMPLOYEE_ID desc");
        list = dao.selectWithOptionalOrderBy(new NameDomain("S"), orderBy);
        assertEquals(2, list.size());
        assertEquals(new IdDomain(8), list.get(0).employee_id());
        assertEquals(new IdDomain(1), list.get(1).employee_id());
    }

    public void testNesteadIf() throws Exception {
        Employee example = new Employee_();
        example.employee_no().set(7801);
        example.manager_id().set(1);
        example.salary().set(new BigDecimal("5000"));
        EmployeeDao dao = new EmployeeDao_();
        List<Employee> list = dao.selectByExample(example);
        assertEquals(1, list.size());

        example.employee_no().set(7777);
        list = dao.selectByExample(example);
        assertEquals(14, list.size());
    }
}
