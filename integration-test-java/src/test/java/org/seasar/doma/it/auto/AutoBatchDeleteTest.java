package org.seasar.doma.it.auto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.BusinessmanDao;
import org.seasar.doma.it.dao.BusinessmanDaoImpl;
import org.seasar.doma.it.dao.CompKeyEmployeeDao;
import org.seasar.doma.it.dao.CompKeyEmployeeDaoImpl;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDaoImpl;
import org.seasar.doma.it.dao.NoIdDao;
import org.seasar.doma.it.dao.NoIdDaoImpl;
import org.seasar.doma.it.dao.PersonDao;
import org.seasar.doma.it.dao.PersonDaoImpl;
import org.seasar.doma.it.dao.SalesmanDao;
import org.seasar.doma.it.dao.SalesmanDaoImpl;
import org.seasar.doma.it.dao.StaffDao;
import org.seasar.doma.it.dao.StaffDaoImpl;
import org.seasar.doma.it.dao.WorkerDao;
import org.seasar.doma.it.dao.WorkerDaoImpl;
import org.seasar.doma.it.entity.Businessman;
import org.seasar.doma.it.entity.CompKeyEmployee;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.it.entity.NoId;
import org.seasar.doma.it.entity.Person;
import org.seasar.doma.it.entity.Salesman;
import org.seasar.doma.it.entity.Staff;
import org.seasar.doma.it.entity.Worker;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.message.Message;

@ExtendWith(IntegrationTestEnvironment.class)
public class AutoBatchDeleteTest {

  @Test
  public void test(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee = new Employee();
    employee.setEmployeeId(1);
    employee.setVersion(1);
    Employee employee2 = new Employee();
    employee2.setEmployeeId(2);
    employee2.setVersion(1);
    int[] result = dao.delete(Arrays.asList(employee, employee2));
    assertEquals(2, result.length);
    assertEquals(1, result[0]);
    assertEquals(1, result[1]);

    employee = dao.selectById(1);
    assertNull(employee);
    employee = dao.selectById(2);
    assertNull(employee);
  }

  @Test
  public void testImmutable(Config config) throws Exception {
    PersonDao dao = new PersonDaoImpl(config);
    Person person = new Person(1, null, null, null, null, null, null, null, 1);
    Person person2 = new Person(2, null, null, null, null, null, null, null, 1);
    BatchResult<Person> result = dao.delete(Arrays.asList(person, person2));
    int[] counts = result.getCounts();
    assertEquals(2, counts.length);
    assertEquals(1, counts[0]);
    assertEquals(1, counts[1]);
    person = result.getEntities().get(0);
    assertEquals("null_preD_postD", person.getEmployeeName());
    person2 = result.getEntities().get(0);
    assertEquals("null_preD_postD", person2.getEmployeeName());

    person = dao.selectById(1);
    assertNull(person);
    person2 = dao.selectById(2);
    assertNull(person2);
  }

  @Test
  public void testIgnoreVersion(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee = new Employee();
    employee.setEmployeeId(1);
    employee.setVersion(99);
    Employee employee2 = new Employee();
    employee2.setEmployeeId(2);
    employee2.setVersion(99);
    int[] result = dao.delete_ignoreVersion(Arrays.asList(employee, employee2));
    assertEquals(2, result.length);
    assertEquals(1, result[0]);
    assertEquals(1, result[1]);

    employee = dao.selectById(1);
    assertNull(employee);
    employee = dao.selectById(2);
    assertNull(employee);
  }

  @Test
  public void testCompositeKey(Config config) throws Exception {
    CompKeyEmployeeDao dao = new CompKeyEmployeeDaoImpl(config);
    CompKeyEmployee employee = new CompKeyEmployee();
    employee.setEmployeeId1(1);
    employee.setEmployeeId2(1);
    employee.setVersion(1);
    CompKeyEmployee employee2 = new CompKeyEmployee();
    employee2.setEmployeeId1(2);
    employee2.setEmployeeId2(2);
    employee2.setVersion(1);

    int[] result = dao.delete(Arrays.asList(employee, employee2));
    assertEquals(2, result.length);
    assertEquals(1, result[0]);
    assertEquals(1, result[1]);

    employee = dao.selectById(1, 1);
    assertNull(employee);
    employee = dao.selectById(2, 2);
    assertNull(employee);
  }

  @Test
  public void testOptimisticLockException(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee1 = dao.selectById(1);
    employee1.setEmployeeName("hoge");
    Employee employee2 = dao.selectById(2);
    employee2.setEmployeeName("foo");
    Employee employee3 = dao.selectById(1);
    employee2.setEmployeeName("bar");
    dao.delete(employee1);
    try {
      dao.delete(Arrays.asList(employee2, employee3));
      fail();
    } catch (OptimisticLockException expected) {
    }
  }

  @Test
  public void testSuppressOptimisticLockException(Config config) throws Exception {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee1 = dao.selectById(1);
    employee1.setEmployeeName("hoge");
    Employee employee2 = dao.selectById(2);
    employee2.setEmployeeName("foo");
    Employee employee3 = dao.selectById(1);
    employee2.setEmployeeName("bar");
    dao.delete(employee1);
    dao.delete_suppressOptimisticLockException(Arrays.asList(employee2, employee3));
  }

  @Test
  public void testNoId(Config config) throws Exception {
    NoIdDao dao = new NoIdDaoImpl(config);
    NoId entity = new NoId();
    entity.setValue1(1);
    entity.setValue2(2);
    NoId entity2 = new NoId();
    entity2.setValue1(1);
    entity2.setValue2(2);
    try {
      dao.delete(Arrays.asList(entity, entity2));
      fail();
    } catch (JdbcException expected) {
      assertEquals(Message.DOMA2022, expected.getMessageResource());
    }
  }

  @Test
  public void testOptional(Config config) throws Exception {
    WorkerDao dao = new WorkerDaoImpl(config);
    Worker employee = new Worker();
    employee.employeeId = Optional.of(1);
    employee.version = Optional.of(1);
    Worker employee2 = new Worker();
    employee2.employeeId = Optional.of(2);
    employee2.version = Optional.of(1);
    int[] result = dao.delete(Arrays.asList(employee, employee2));
    assertEquals(2, result.length);
    assertEquals(1, result[0]);
    assertEquals(1, result[1]);

    employee = dao.selectById(Optional.of(1));
    assertNull(employee);
    employee = dao.selectById(Optional.of(2));
    assertNull(employee);
  }

  @Test
  public void testOptionalInt(Config config) throws Exception {
    BusinessmanDao dao = new BusinessmanDaoImpl(config);
    Businessman employee = new Businessman();
    employee.employeeId = OptionalInt.of(1);
    employee.version = OptionalInt.of(1);
    Businessman employee2 = new Businessman();
    employee2.employeeId = OptionalInt.of(2);
    employee2.version = OptionalInt.of(1);
    int[] result = dao.delete(Arrays.asList(employee, employee2));
    assertEquals(2, result.length);
    assertEquals(1, result[0]);
    assertEquals(1, result[1]);

    employee = dao.selectById(OptionalInt.of(1));
    assertNull(employee);
    employee = dao.selectById(OptionalInt.of(2));
    assertNull(employee);
  }

  @Test
  public void testEmbeddable(Config config) throws Exception {
    StaffDao dao = new StaffDaoImpl(config);
    Staff staff = new Staff();
    staff.employeeId = 1;
    staff.version = 1;
    Staff staff2 = new Staff();
    staff2.employeeId = 2;
    staff2.version = 1;
    int[] result = dao.delete(Arrays.asList(staff, staff2));
    assertEquals(2, result.length);
    assertEquals(1, result[0]);
    assertEquals(1, result[1]);

    staff = dao.selectById(1);
    assertNull(staff);
    staff = dao.selectById(2);
    assertNull(staff);
  }

  @Test
  public void testTenantId(Config config) throws Exception {
    SalesmanDao dao = new SalesmanDaoImpl(config);
    Salesman salesman = dao.selectById(1);
    Integer tenantId = salesman.departmentId;
    salesman.departmentId = -1;
    try {
      dao.delete(Arrays.asList(salesman));
      fail();
    } catch (OptimisticLockException expected) {
    }
    salesman.departmentId = tenantId;
    dao.delete(Arrays.asList(salesman));
  }
}
