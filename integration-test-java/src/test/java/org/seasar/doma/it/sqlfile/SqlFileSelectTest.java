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
package org.seasar.doma.it.sqlfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.*;
import org.seasar.doma.it.dao.BranchDao.Branch;
import org.seasar.doma.it.dao.BranchDao.BranchDetail;
import org.seasar.doma.it.dao.BranchDao.Location;
import org.seasar.doma.it.domain.Salary;
import org.seasar.doma.it.entity.*;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ResultMappingException;

@ExtendWith(IntegrationTestEnvironment.class)
public class SqlFileSelectTest {

  @Test
  public void testEmbeddedVariable(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    List<Employee> list = dao.selectWithOptionalOrderBy("S", "order by EMPLOYEE_ID");
    assertEquals(2, list.size());
    assertEquals(Integer.valueOf(1), list.get(0).getEmployeeId());
    assertEquals(Integer.valueOf(8), list.get(1).getEmployeeId());

    list = dao.selectWithOptionalOrderBy("S", "order by EMPLOYEE_ID desc");
    assertEquals(2, list.size());
    assertEquals(Integer.valueOf(8), list.get(0).getEmployeeId());
    assertEquals(Integer.valueOf(1), list.get(1).getEmployeeId());
  }

  private static final java.lang.reflect.Method __method2 =
      org.seasar.doma.internal.jdbc.dao.DaoImplSupport.getDeclaredMethod(
          org.seasar.doma.it.dao.EmployeeDao.class, "selectById", java.lang.Integer.class);

  @Test
  public void testNull(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee = dao.selectById(9);
    assertNull(employee.getManagerId());
  }

  @Test
  public void testPrefixSearch(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    List<Employee> employees = dao.selectByNamePrefix("S");
    assertEquals(2, employees.size());
  }

  @Test
  public void testInsideSearch(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    List<Employee> employees = dao.selectByNameInfix("S");
    assertEquals(5, employees.size());
  }

  @Test
  public void testSuffixSearch(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    List<Employee> employees = dao.selectByNameSuffix("S");
    assertEquals(3, employees.size());
  }

  @Test
  public void testMap(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Map<String, Object> employee = dao.selectByIdAsMap(1);
    assertNotNull(employee);
    assertNotNull(employee.get("employeeId"));
    assertNotNull(employee.get("employeeName"));
    assertNotNull(employee.get("hiredate"));
  }

  @Test
  public void testMapList(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    List<Map<String, Object>> employees = dao.selectAllAsMapList();
    assertEquals(14, employees.size());
  }

  @Test
  public void testEnsureResultMapping_false(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    List<Employee> employees = dao.selectOnlyNameWithoutMappingCheck();
    assertEquals(14, employees.size());
  }

  @Test
  public void testEnsureResultMapping_true(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    try {
      dao.selectOnlyNameWithMappingCheck();
      fail();
    } catch (ResultMappingException expected) {
      System.err.print(expected);
    }
  }

  @Test
  public void testOptional(Config config) {
    WorkerDao dao = new WorkerDaoImpl(config);
    Worker worker = dao.selectById(Optional.of(9));
    assertEquals(Integer.valueOf(9), worker.employeeId.get());
    assertEquals(Integer.valueOf(7839), worker.employeeNo.get());
    assertEquals("KING", worker.employeeName.get());
    assertFalse(worker.managerId.isPresent());
    assertEquals(java.sql.Date.valueOf("1981-11-17"), worker.hiredate.get());
    assertEquals(0, new BigDecimal("5000").compareTo(worker.salary.get().getValue()));
    assertEquals(Integer.valueOf(1), worker.departmentId.get().getValue());
    assertEquals(Integer.valueOf(9), worker.addressId.get());
    assertEquals(Integer.valueOf(1), worker.version.get());
  }

  @Test
  public void testOptional_expression(Config config) {
    WorkerDao dao = new WorkerDaoImpl(config);
    Worker worker = new Worker();
    worker.employeeNo = Optional.of(7801);
    worker.managerId = Optional.empty();
    worker.salary = Optional.of(new Salary("3000"));

    List<Worker> workers = dao.selectByExample(worker);
    assertEquals(14, workers.size());

    worker.managerId = Optional.of(1);

    workers = dao.selectByExample(worker);
    assertEquals(3, workers.size());
  }

  @Test
  public void testOptionalInt(Config config) {
    BusinessmanDao dao = new BusinessmanDaoImpl(config);
    Businessman worker = dao.selectById(OptionalInt.of(9));
    assertEquals(9, worker.employeeId.getAsInt());
    assertEquals(7839, worker.employeeNo.getAsInt());
    assertEquals("KING", worker.employeeName.get());
    assertFalse(worker.managerId.isPresent());
    assertEquals(java.sql.Date.valueOf("1981-11-17"), worker.hiredate.get());
    assertEquals(5000L, worker.salary.getAsLong());
    assertEquals(1, worker.departmentId.getAsInt());
    assertEquals(9, worker.addressId.getAsInt());
    assertEquals(1, worker.version.getAsInt());
  }

  @Test
  public void testOptionalInt_expression(Config config) {
    BusinessmanDao dao = new BusinessmanDaoImpl(config);
    Businessman worker = new Businessman();
    worker.employeeNo = OptionalInt.of(7801);
    worker.managerId = OptionalInt.empty();
    worker.salary = OptionalLong.of(3000L);

    List<Businessman> workers = dao.selectByExample(worker);
    assertEquals(14, workers.size());

    worker.managerId = OptionalInt.of(1);

    workers = dao.selectByExample(worker);
    assertEquals(3, workers.size());
  }

  @Test
  public void testNestedEntity(Config config) {
    BranchDao dao = new BranchDaoImpl(config);
    Branch branch = dao.selectById(1);
    assertNotNull(branch);
    assertEquals(Integer.valueOf(1), branch.branchId);
    assertEquals(Integer.valueOf(1), branch.version);
    BranchDetail branchDetail = branch.branchDetail;
    assertNotNull(branchDetail);
    assertEquals(Integer.valueOf(10), branchDetail.branchNo);
    assertEquals("ACCOUNTING", branchDetail.branchName);
    Location location = branchDetail.location;
    assertNotNull(location);
    assertEquals("NEW YORK", location.getValue());
  }

  @Test
  public void testAggregateSingle_oneToOne(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee = dao.selectByIdAsAggregate(1);
    assertNotNull(employee);
    assertNotNull(employee.getDepartment());
    assertNotNull(employee.getAddress());
  }

  @Test
  public void testAggregateOptional_oneToOne(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Optional<Employee> optionalEmployee = dao.selectOptionalByIdAsAggregate(1);
    Employee employee = optionalEmployee.orElse(null);
    assertNotNull(employee);
    assertNotNull(employee.getDepartment());
    assertNotNull(employee.getAddress());
  }

  @Test
  public void testAggregateList_oneToOne(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    List<Employee> employees = dao.selectAllAsAggregate();
    assertEquals(14, employees.size());
    for (Employee employee : employees) {
      assertNotNull(employee.getDepartment());
      assertNotNull(employee.getAddress());
    }
  }

  @Test
  public void testAggregateSingle_oneToMany(Config config) {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = dao.selectByIdAsAggregate(1);
    assertNotNull(department);
    assertFalse(department.getEmployeeList().isEmpty());
    assertNotNull(department.getEmployeeList().get(0).getAddress());
  }

  @Test
  public void testAggregateList_oneToMany(Config config) {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    List<Department> departments = dao.selectAllAsAggregate();
    assertEquals(4, departments.size());
    for (Department department : departments) {
      assertNotNull(department);
      if (department.getDepartmentId().getValue() == 4) {
        assertTrue(department.getEmployeeList().isEmpty());
      } else {
        assertFalse(department.getEmployeeList().isEmpty());
        assertTrue(department.getEmployeeList().stream().allMatch(e -> e.getAddress() != null));
      }
    }
  }
}
