package org.seasar.doma.it.sqlfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDaoImpl;
import org.seasar.doma.it.dao.PersonDao;
import org.seasar.doma.it.dao.PersonDaoImpl;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.it.entity.Person;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Result;

@ExtendWith(IntegrationTestEnvironment.class)
public class SqlFileDeleteTest {

  @Test
  public void test(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee = new Employee();
    employee.setEmployeeId(1);
    employee.setVersion(1);
    int result = dao.deleteBySqlFile(employee);
    assertEquals(1, result);

    employee = dao.selectById(1);
    assertNull(employee);
  }

  @Test
  public void testImmutable(Config config) throws Exception {
    PersonDao dao = new PersonDaoImpl(config);
    Person person = new Person(1, null, null, null, null, null, null, null, 1);
    Result<Person> result = dao.deleteBySqlFile(person);
    assertEquals(1, result.getCount());
    person = result.getEntity();
    assertEquals("null_preD_postD", person.getEmployeeName());

    person = dao.selectById(1);
    assertNull(person);
  }
}
