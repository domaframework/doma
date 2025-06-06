/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.it.auto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;
import java.util.OptionalInt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.it.dao.BusinessmanDao;
import org.seasar.doma.it.dao.BusinessmanDaoImpl;
import org.seasar.doma.it.dao.CompKeyEmployeeDao;
import org.seasar.doma.it.dao.CompKeyEmployeeDaoImpl;
import org.seasar.doma.it.dao.DeleteReturningDao;
import org.seasar.doma.it.dao.DeleteReturningDaoImpl;
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
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.message.Message;

@ExtendWith(IntegrationTestEnvironment.class)
public class AutoDeleteTest {

  @Test
  public void test(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee = new Employee();
    employee.setEmployeeId(1);
    employee.setVersion(1);
    int result = dao.delete(employee);
    assertEquals(1, result);

    employee = dao.selectById(1);
    assertNull(employee);
  }

  @Test
  public void testImmutable(Config config) {
    PersonDao dao = new PersonDaoImpl(config);
    Person person = new Person(1, null, null, null, null, null, null, null, 1);
    Result<Person> result = dao.delete(person);
    assertEquals(1, result.getCount());
    person = result.getEntity();
    assertEquals("null_preD_postD", person.getEmployeeName());

    person = dao.selectById(1);
    assertNull(person);
  }

  @Test
  public void testIgnoreVersion(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee = new Employee();
    employee.setEmployeeId(1);
    employee.setVersion(99);
    int result = dao.delete_ignoreVersion(employee);
    assertEquals(1, result);

    employee = dao.selectById(1);
    assertNull(employee);
  }

  @Test
  public void testCompositeKey(Config config) {
    CompKeyEmployeeDao dao = new CompKeyEmployeeDaoImpl(config);
    CompKeyEmployee employee = new CompKeyEmployee();
    employee.setEmployeeId1(1);
    employee.setEmployeeId2(1);
    employee.setVersion(1);
    int result = dao.delete(employee);
    assertEquals(1, result);

    employee = dao.selectById(1, 1);
    assertNull(employee);
  }

  @Test
  public void testOptimisticLockException(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee1 = dao.selectById(1);
    employee1.setEmployeeName("hoge");
    Employee employee2 = dao.selectById(1);
    employee2.setEmployeeName("foo");
    dao.delete(employee1);
    try {
      dao.delete(employee2);
      fail();
    } catch (OptimisticLockException expected) {
    }
  }

  @Test
  public void testSuppressOptimisticLockException(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee1 = dao.selectById(1);
    employee1.setEmployeeName("hoge");
    Employee employee2 = dao.selectById(1);
    employee2.setEmployeeName("foo");
    dao.delete(employee1);
    dao.delete_suppressOptimisticLockException(employee2);
  }

  @Test
  public void testNoId(Config config) {
    NoIdDao dao = new NoIdDaoImpl(config);
    NoId entity = new NoId();
    entity.setValue1(1);
    entity.setValue2(2);
    try {
      dao.delete(entity);
      fail();
    } catch (JdbcException expected) {
      assertEquals(Message.DOMA2022, expected.getMessageResource());
    }
  }

  @Test
  public void testOptional(Config config) {
    WorkerDao dao = new WorkerDaoImpl(config);
    Worker employee = new Worker();
    employee.employeeId = Optional.of(1);
    employee.version = Optional.of(1);
    int result = dao.delete(employee);
    assertEquals(1, result);

    employee = dao.selectById(Optional.of(1));
    assertNull(employee);
  }

  @Test
  public void testOptionalInt(Config config) {
    BusinessmanDao dao = new BusinessmanDaoImpl(config);
    Businessman employee = new Businessman();
    employee.employeeId = OptionalInt.of(1);
    employee.version = OptionalInt.of(1);
    int result = dao.delete(employee);
    assertEquals(1, result);

    employee = dao.selectById(OptionalInt.of(1));
    assertNull(employee);
  }

  @Test
  public void testEmbeddable(Config config) {
    StaffDao dao = new StaffDaoImpl(config);
    Staff staff = new Staff();
    staff.employeeId = 1;
    staff.version = 1;
    int result = dao.delete(staff);
    assertEquals(1, result);

    staff = dao.selectById(1);
    assertNull(staff);
  }

  @Test
  public void testTenantId(Config config) {
    SalesmanDao dao = new SalesmanDaoImpl(config);
    Salesman salesman = dao.selectById(1);
    Integer tenantId = salesman.departmentId;
    salesman.departmentId = -1;
    try {
      dao.delete(salesman);
      fail();
    } catch (OptimisticLockException expected) {
    }
    salesman.departmentId = tenantId;
    dao.delete(salesman);
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning(Config config) {
    DeleteReturningDao dao = new DeleteReturningDaoImpl(config);

    var entity = dao.selectById(1);
    var result = dao.deleteThenReturnAll(entity);

    assertEquals(1, entity.getAddressId());
    assertEquals("SMITH", result.getEmployeeName());
    assertEquals(1, result.getVersion());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning_include(Config config) {
    DeleteReturningDao dao = new DeleteReturningDaoImpl(config);

    var entity = dao.selectById(1);
    var result = dao.deleteThenReturnOnlyId(entity);

    assertEquals(1, entity.getAddressId());
    assertNull(result.getEmployeeName());
    assertNull(result.getVersion());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  public void returning_exclude(Config config) {
    DeleteReturningDao dao = new DeleteReturningDaoImpl(config);

    var entity = dao.selectById(1);
    var result = dao.deleteThenReturnExceptVersion(entity);

    assertEquals(1, entity.getAddressId());
    assertEquals("SMITH", result.getEmployeeName());
    assertNull(result.getVersion());
  }
}
