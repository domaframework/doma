package org.seasar.doma.it.entity;

import java.math.BigDecimal;
import java.sql.Date;

import org.junit.runner.RunWith;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.TxBehavior;
import org.seasar.framework.unit.annotation.TxBehaviorType;

@RunWith(Seasar2.class)
public class ToStringTest {

    @TxBehavior(TxBehaviorType.NONE)
    public void test() throws Exception {
        Employee employee = new Employee_();
        employee.employee_id().set(1);
        employee.employee_no().set(2);
        employee.employee_name().set("aaa");
        employee.manager_id().set(3);
        employee.hiredate().set(Date.valueOf("2001-02-03"));
        employee.salary().set(new BigDecimal(100));
        employee.department_id().set(4);
        employee.address_id().set(5);
        employee.version().set(6);
        System.out.println(employee);
    }
}
