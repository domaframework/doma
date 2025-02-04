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
package org.seasar.doma.it.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.criteria.Street;
import org.seasar.doma.it.dao.DepartmentDao;
import org.seasar.doma.it.dao.DepartmentDaoImpl;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDaoImpl;
import org.seasar.doma.it.dao.ImmutableEmpDao;
import org.seasar.doma.it.dao.ImmutableEmpDaoImpl;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.domain.Salary;
import org.seasar.doma.it.entity.Address;
import org.seasar.doma.it.entity.Department;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.it.entity.ImmutableDept;
import org.seasar.doma.it.entity.ImmutableEmp;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
public class AggregateTest {

  @Test
  public void testAggregateSingle_oneToOne(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee = dao.selectByIdAsAggregate(1);

    // employee
    assertNotNull(employee);
    assertEquals(1, employee.getEmployeeId());
    assertEquals(7369, employee.getEmployeeNo());
    assertEquals("SMITH", employee.getEmployeeName());
    assertEquals(13, employee.getManagerId());
    assertEquals(Date.valueOf(LocalDate.of(1980, 12, 17)), employee.getHiredate());
    assertEquals(new Salary(new BigDecimal(800)), employee.getSalary());
    assertEquals(new Identity<>(2), employee.getDepartmentId());
    assertEquals(1, employee.getAddressId());
    assertEquals(1, employee.getVersion());

    // department
    Department department = employee.getDepartment();
    assertNotNull(department);
    assertEquals(new Identity<>(2), department.getDepartmentId());
    assertEquals(20, department.getDepartmentNo());
    assertEquals("RESEARCH", department.getDepartmentName());
    assertEquals(1, department.getVersion());
    assertEquals(1, department.getEmployeeList().size());
    assertEquals(employee, department.getEmployeeList().get(0));

    // address
    Address address = employee.getAddress();
    assertNotNull(address);
    assertEquals(1, address.getAddressId());
    assertEquals(new Street("STREET 1"), address.getStreet());
    assertEquals(1, address.getVersion());
  }

  @Test
  public void testAggregateSingle_oneToOne_null(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Employee employee = dao.selectByIdAsAggregate(0);
    assertNull(employee);
  }

  @Test
  public void testAggregateOptional_oneToOne(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    Optional<Employee> optionalEmployee = dao.selectOptionalByIdAsAggregate(1);

    // employee
    Employee employee = optionalEmployee.orElse(null);
    assertNotNull(employee);
    assertEquals(1, employee.getEmployeeId());
    assertEquals(7369, employee.getEmployeeNo());
    assertEquals("SMITH", employee.getEmployeeName());
    assertEquals(13, employee.getManagerId());
    assertEquals(Date.valueOf(LocalDate.of(1980, 12, 17)), employee.getHiredate());
    assertEquals(new Salary(new BigDecimal(800)), employee.getSalary());
    assertEquals(new Identity<>(2), employee.getDepartmentId());
    assertEquals(1, employee.getAddressId());
    assertEquals(1, employee.getVersion());

    // department
    Department department = employee.getDepartment();
    assertNotNull(department);
    assertEquals(new Identity<>(2), department.getDepartmentId());
    assertEquals(20, department.getDepartmentNo());
    assertEquals("RESEARCH", department.getDepartmentName());
    assertEquals(1, department.getVersion());
    assertEquals(1, department.getEmployeeList().size());
    assertEquals(employee, department.getEmployeeList().get(0));

    // address
    Address address = employee.getAddress();
    assertNotNull(address);
    assertEquals(1, address.getAddressId());
    assertEquals(new Street("STREET 1"), address.getStreet());
    assertEquals(1, address.getVersion());
  }

  @Test
  public void testAggregateList_oneToOne(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    List<Employee> employees = dao.selectAllAsAggregate();
    assertEquals(14, employees.size());
    int id = 1;
    for (Employee employee : employees) {
      assertEquals(id++, employee.getEmployeeId());
      assertNotNull(employee.getDepartment());
      assertNotNull(employee.getAddress());
    }

    // employee
    Employee employee = employees.get(0);
    assertNotNull(employee);
    assertEquals(1, employee.getEmployeeId());
    assertEquals(7369, employee.getEmployeeNo());
    assertEquals("SMITH", employee.getEmployeeName());
    assertEquals(13, employee.getManagerId());
    assertEquals(Date.valueOf(LocalDate.of(1980, 12, 17)), employee.getHiredate());
    assertEquals(new Salary(new BigDecimal(800)), employee.getSalary());
    assertEquals(new Identity<>(2), employee.getDepartmentId());
    assertEquals(1, employee.getAddressId());
    assertEquals(1, employee.getVersion());

    // department
    Department department = employee.getDepartment();
    assertNotNull(department);
    assertEquals(new Identity<>(2), department.getDepartmentId());
    assertEquals(20, department.getDepartmentNo());
    assertEquals("RESEARCH", department.getDepartmentName());
    assertEquals(1, department.getVersion());
    assertEquals(5, department.getEmployeeList().size());
    assertEquals(
        List.of(1, 4, 8, 11, 13),
        department.getEmployeeList().stream().map(Employee::getEmployeeId).toList());

    // address
    Address address = employee.getAddress();
    assertNotNull(address);
    assertEquals(1, address.getAddressId());
    assertEquals(new Street("STREET 1"), address.getStreet());
    assertEquals(1, address.getVersion());
  }

  @Test
  public void testAggregateSingle_oneToMany(Config config) {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = dao.selectByIdAsAggregate(2);

    // department
    assertNotNull(department);
    assertEquals(new Identity<>(2), department.getDepartmentId());
    assertEquals(20, department.getDepartmentNo());
    assertEquals("RESEARCH", department.getDepartmentName());
    assertEquals(1, department.getVersion());
    List<Employee> employeeList = department.getEmployeeList();
    assertEquals(5, employeeList.size());
    assertEquals(
        List.of(1, 4, 8, 11, 13),
        department.getEmployeeList().stream().map(Employee::getEmployeeId).sorted().toList());

    // employee
    Employee employee =
        employeeList.stream().filter(e -> e.getEmployeeId() == 8).findFirst().orElse(null);
    assertNotNull(employee);
    assertEquals(8, employee.getEmployeeId());
    assertEquals(7788, employee.getEmployeeNo());
    assertEquals("SCOTT", employee.getEmployeeName());
    assertEquals(4, employee.getManagerId());
    assertEquals(Date.valueOf(LocalDate.of(1982, 12, 9)), employee.getHiredate());
    assertEquals(new Salary(new BigDecimal(3000)), employee.getSalary());
    assertEquals(new Identity<>(2), employee.getDepartmentId());
    assertEquals(8, employee.getAddressId());
    assertEquals(1, employee.getVersion());

    // address
    Address address = employee.getAddress();
    assertNotNull(address);
    assertEquals(8, address.getAddressId());
    assertEquals(new Street("STREET 8"), address.getStreet());
    assertEquals(1, address.getVersion());
  }

  @Test
  public void testAggregateSingle_oneToMany_null(Config config) {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    Department department = dao.selectByIdAsAggregate(0);
    assertNull(department);
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

    // department
    Department department =
        departments.stream()
            .filter(d -> d.getDepartmentId().getValue() == 2)
            .findFirst()
            .orElse(null);
    assertNotNull(department);
    assertNotNull(department);
    assertEquals(new Identity<>(2), department.getDepartmentId());
    assertEquals(20, department.getDepartmentNo());
    assertEquals("RESEARCH", department.getDepartmentName());
    assertEquals(1, department.getVersion());
    List<Employee> employeeList = department.getEmployeeList();
    assertEquals(5, employeeList.size());
    assertEquals(
        List.of(1, 4, 8, 11, 13),
        department.getEmployeeList().stream().map(Employee::getEmployeeId).sorted().toList());

    // employee
    Employee employee =
        employeeList.stream().filter(e -> e.getEmployeeId() == 8).findFirst().orElse(null);
    assertNotNull(employee);
    assertEquals(8, employee.getEmployeeId());
    assertEquals(7788, employee.getEmployeeNo());
    assertEquals("SCOTT", employee.getEmployeeName());
    assertEquals(4, employee.getManagerId());
    assertEquals(Date.valueOf(LocalDate.of(1982, 12, 9)), employee.getHiredate());
    assertEquals(new Salary(new BigDecimal(3000)), employee.getSalary());
    assertEquals(new Identity<>(2), employee.getDepartmentId());
    assertEquals(8, employee.getAddressId());
    assertEquals(1, employee.getVersion());

    // address
    Address address = employee.getAddress();
    assertNotNull(address);
    assertEquals(8, address.getAddressId());
    assertEquals(new Street("STREET 8"), address.getStreet());
    assertEquals(1, address.getVersion());
  }

  @Test
  public void testAggregateList_oneToMany_empty(Config config) {
    DepartmentDao dao = new DepartmentDaoImpl(config);
    List<Department> departments = dao.selectByIdsAsAggregate(List.of(0));
    assertTrue(departments.isEmpty());
  }

  @Test
  public void testSelfJoin(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    List<Employee> employeeList = dao.selectByIdWithManager(List.of(6, 9));

    Employee blake = employeeList.get(0);
    Employee king = employeeList.get(1);
    assertNotNull(blake);
    assertNotNull(king);
    assertSame(king, blake.getManager());
  }

  @Test
  public void testSelfJoin_bidirectional(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    List<Employee> employeeList = dao.selectAllWithManager();

    // managers
    Map<Integer, Employee> managerMap =
        employeeList.stream()
            .map(Employee::getManager)
            .filter(Objects::nonNull)
            .collect(
                Collectors.toMap(
                    Employee::getEmployeeId,
                    Function.identity(),
                    (f, s) -> {
                      assertSame(f, s);
                      return f;
                    }));

    for (Employee employee : employeeList) {
      Employee manager = managerMap.get(employee.getEmployeeId());
      if (manager != null) {
        assertSame(manager, employee);
      }
    }

    // assistants
    Map<Integer, Employee> assistantMap =
        employeeList.stream()
            .flatMap(e -> e.getAssistants().stream())
            .collect(
                Collectors.toMap(
                    Employee::getEmployeeId,
                    Function.identity(),
                    (f, s) -> {
                      assertSame(f, s);
                      return f;
                    }));

    for (Employee employee : employeeList) {
      Employee assistant = assistantMap.get(employee.getEmployeeId());
      if (assistant != null) {
        assertSame(assistant, employee);
      }
    }

    // employee
    List<Integer> idList = employeeList.stream().map(Employee::getEmployeeId).toList();
    assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14), idList);

    Employee jones = employeeList.get(3);
    assertEquals(4, jones.getEmployeeId());
    assertEquals("JONES", jones.getEmployeeName());
    assertEquals(1, jones.getVersion());
    assertEquals(new Identity<>(2), jones.getDepartmentId());
    assertEquals(new Salary(new BigDecimal(2975)), jones.getSalary());
    assertEquals(Date.valueOf(LocalDate.of(1981, 4, 2)), jones.getHiredate());
    assertEquals(4, jones.getAddressId());
    assertEquals(9, jones.getManagerId());
    assertEquals(9, jones.getManager().getEmployeeId());
    assertEquals(
        List.of(8, 13), jones.getAssistants().stream().map(Employee::getEmployeeId).toList());

    assertEquals(2, jones.getAssistants().size());
    assertSame(jones, jones.getAssistants().get(0).getManager());
    assertSame(jones, jones.getAssistants().get(1).getManager());

    for (Employee employee : employeeList) {
      Employee m = employee.getManager();
      System.out.printf(
          "id=%d, name=%s, manager=%s, assistants=%s%n",
          employee.getEmployeeId(),
          employee.getEmployeeName(),
          employee.getManager() == null ? null : employee.getManager().getEmployeeId(),
          employee.getAssistants().stream().map(Employee::getEmployeeId).toList());
    }
  }

  @Test
  public void testImmutable(Config config) {
    ImmutableEmpDao dao = new ImmutableEmpDaoImpl(config);
    ImmutableEmp emp = dao.selectById(1);
    assertNotNull(emp);
    assertEquals("SMITH", emp.employeeName());
    ImmutableDept dept = emp.dept();
    assertNotNull(dept);
    assertEquals(2, dept.departmentId());
    assertEquals("RESEARCH", dept.departmentName());
  }

  @Test
  public void testImmutable_selfJoin(Config config) {
    ImmutableEmpDao dao = new ImmutableEmpDaoImpl(config);
    List<ImmutableEmp> employeeList = dao.selectByIds(List.of(6, 9));
    ImmutableEmp blake = employeeList.get(0);
    ImmutableEmp king = employeeList.get(1);
    assertNotNull(blake);
    assertNotNull(blake.dept());
    assertNotNull(blake.manager());
    assertNotNull(king);
    assertNotNull(king.dept());
    assertNull(king.manager());
    assertEquals(king.employeeName(), blake.manager().employeeName());
  }
}
