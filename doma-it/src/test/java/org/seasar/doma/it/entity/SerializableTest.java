package org.seasar.doma.it.entity;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Date;

import org.junit.runner.RunWith;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.TxBehavior;
import org.seasar.framework.unit.annotation.TxBehaviorType;

@RunWith(Seasar2.class)
public class SerializableTest {

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

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(employee);
        oos.flush();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Employee employee2 = Employee.class.cast(ois.readObject());

        assertEquals(employee.employee_id(), employee2.employee_id());
        assertEquals(employee.employee_no(), employee2.employee_no());
        assertEquals(employee.employee_name(), employee2.employee_name());
        assertEquals(employee.manager_id(), employee2.manager_id());
        assertEquals(employee.hiredate(), employee2.hiredate());
        assertEquals(employee.salary(), employee2.salary());
        assertEquals(employee.department_id(), employee2.department_id());
        assertEquals(employee.address_id(), employee2.address_id());
        assertEquals(employee.version(), employee2.version());
    }

}
