package org.seasar.doma.it.sqlfile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.PersonDao;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.it.entity.Person;
import org.seasar.doma.jdbc.Result;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SqlFileDeleteTest {

    @Test
    public void test() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setVersion(1);
        int result = dao.deleteBySqlFile(employee);
        assertEquals(1, result);

        employee = dao.selectById(1);
        assertNull(employee);
    }

    @Test
    public void testImmutable() throws Exception {
        PersonDao dao = PersonDao.get();
        Person person = new Person(1, null, null, null, null, null, null, null,
                1);
        Result<Person> result = dao.deleteBySqlFile(person);
        assertEquals(1, result.getCount());
        person = result.getEntity();
        assertEquals("null_preD_postD", person.getEmployeeName());

        person = dao.selectById(1);
        assertNull(person);
    }
}
