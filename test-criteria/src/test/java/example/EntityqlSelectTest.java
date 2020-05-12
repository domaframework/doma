package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.option.AssociationOption;
import org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException;
import org.seasar.doma.jdbc.criteria.statement.Listable;

@ExtendWith(Env.class)
public class EntityqlSelectTest {

  private final Entityql entityql;

  public EntityqlSelectTest(Config config) {
    this.entityql = new Entityql(config);
  }

  @Test
  void settings() {
    Employee_ e = new Employee_();

    List<Employee> list =
        entityql
            .from(
                e,
                settings -> {
                  settings.setComment("all employees");
                  settings.setSqlLogType(SqlLogType.RAW);
                  settings.setQueryTimeout(1000);
                  settings.setAllowEmptyWhere(true);
                  settings.setFetchSize(100);
                  settings.setMaxRows(100);
                })
            .fetch();
    assertEquals(14, list.size());
  }

  @Test
  void allowEmptyWhere_disabled() {
    Employee_ e = new Employee_();

    assertThrows(
        EmptyWhereClauseException.class,
        () -> entityql.from(e, settings -> settings.setAllowEmptyWhere(false)).fetch());
  }

  @Test
  void fetch() {
    Employee_ e = new Employee_();

    List<Employee> list = entityql.from(e).fetch();
    assertEquals(14, list.size());
  }

  @Test
  void fetchOptional() {
    Employee_ e = new Employee_();

    Optional<Employee> employee =
        entityql.from(e).where(c -> c.eq(e.employeeId, 1)).fetchOptional();
    assertTrue(employee.isPresent());
  }

  @Test
  void fetchOptional_notPresent() {
    Employee_ e = new Employee_();

    Optional<Employee> employee =
        entityql.from(e).where(c -> c.eq(e.employeeId, 100)).fetchOptional();
    assertFalse(employee.isPresent());
  }

  @Test
  void fetchOne() {
    Employee_ e = new Employee_();

    Employee employee = entityql.from(e).where(c -> c.eq(e.employeeId, 1)).fetchOne();
    assertNotNull(employee);
  }

  @Test
  void fetchOne_null() {
    Employee_ e = new Employee_();

    Employee employee = entityql.from(e).where(c -> c.eq(e.employeeId, 100)).fetchOne();
    assertNull(employee);
  }

  @Test
  void where() {
    Employee_ e = new Employee_();

    List<Employee> list =
        entityql
            .from(e)
            .where(
                c -> {
                  c.eq(e.departmentId, 2);
                  c.isNotNull(e.managerId);
                  c.or(
                      () -> {
                        c.gt(e.salary, new Salary("1000"));
                        c.lt(e.salary, new Salary("2000"));
                      });
                })
            .fetch();

    assertEquals(10, list.size());
  }

  @Test
  void where_dynamic() {
    List<Employee> list = where_dynamic(null);
    assertEquals(3, list.size());

    List<Employee> list2 = where_dynamic("C%");
    assertEquals(1, list2.size());
  }

  @SuppressWarnings("UnnecessaryLocalVariable")
  private List<Employee> where_dynamic(String name) {
    Employee_ e = new Employee_();
    List<Employee> list =
        entityql
            .from(e)
            .where(
                c -> {
                  c.eq(e.departmentId, 1);
                  if (name != null) {
                    c.like(e.employeeName, name);
                  }
                })
            .fetch();
    return list;
  }

  @Test
  void where_in() {
    Employee_ e = new Employee_();

    List<Employee> list =
        entityql
            .from(e)
            .where(c -> c.in(e.employeeId, Arrays.asList(2, 3, 4)))
            .orderBy(c -> c.asc(e.employeeId))
            .fetch();

    assertEquals(3, list.size());
  }

  @Test
  void where_in_subQuery() {
    Employee_ e = new Employee_();
    Employee_ e2 = new Employee_();

    List<Employee> list =
        entityql
            .from(e)
            .where(c -> c.in(e.employeeId, c.from(e2).select(e2.managerId)))
            .orderBy(c -> c.asc(e.employeeId))
            .fetch();

    assertEquals(6, list.size());
  }

  @Test
  void where_exists_subQuery() {
    Employee_ e = new Employee_();
    Employee_ e2 = new Employee_();

    List<Employee> list =
        entityql
            .from(e)
            .where(
                c ->
                    c.exists(
                        c.from(e2)
                            .where(c2 -> c2.eq(e.employeeId, e2.managerId))
                            .select(e2.managerId)))
            .orderBy(c -> c.asc(e.employeeId))
            .fetch();

    assertEquals(6, list.size());
  }

  @Test
  void innerJoin() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Employee> list =
        entityql.from(e).innerJoin(d, on -> on.eq(e.departmentId, d.departmentId)).fetch();

    assertEquals(14, list.size());
  }

  @Test
  void innerJoin_dynamic() {
    List<Employee> list = innerJoin_dynamic(true);
    assertEquals(13, list.size());

    List<Employee> list2 = innerJoin_dynamic(false);
    assertEquals(14, list2.size());
  }

  @SuppressWarnings("UnnecessaryLocalVariable")
  private List<Employee> innerJoin_dynamic(boolean join) {
    Employee_ e = new Employee_();
    Employee_ e2 = new Employee_();

    List<Employee> list =
        entityql
            .from(e)
            .innerJoin(
                e2,
                on -> {
                  if (join) {
                    on.eq(e.managerId, e2.employeeId);
                  }
                })
            .fetch();
    return list;
  }

  @Test
  void leftJoin() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Employee> list =
        entityql.from(e).leftJoin(d, on -> on.eq(e.departmentId, d.departmentId)).fetch();

    assertEquals(14, list.size());
  }

  @Test
  void associate() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Employee> list =
        entityql
            .from(e)
            .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
            .where(c -> c.eq(d.departmentName, "SALES"))
            .associate(
                e,
                d,
                (employee, department) -> {
                  employee.setDepartment(department);
                  department.getEmployeeList().add(employee);
                })
            .fetch();

    assertEquals(6, list.size());
    assertTrue(
        list.stream().allMatch(it -> it.getDepartment().getDepartmentName().equals("SALES")));
    assertEquals(list.get(0).getDepartment().getEmployeeList().size(), 6);
  }

  @Test
  void associate_dynamic() {
    List<Employee> list = associate_dynamic(true);
    assertEquals(14, list.size());
    assertTrue(list.stream().allMatch(emp -> emp.getDepartment() != null));
    List<Employee> list2 = associate_dynamic(false);
    assertEquals(14, list2.size());
    assertTrue(list2.stream().allMatch(emp -> emp.getDepartment() == null));
  }

  @SuppressWarnings("UnnecessaryLocalVariable")
  private List<Employee> associate_dynamic(boolean join) {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Employee> list =
        entityql
            .from(e)
            .innerJoin(
                d,
                on -> {
                  if (join) {
                    on.eq(e.departmentId, d.departmentId);
                  }
                })
            .associate(
                e,
                d,
                (employee, department) -> {
                  employee.setDepartment(department);
                  department.getEmployeeList().add(employee);
                },
                AssociationOption.OPTIONAL)
            .fetch();

    return list;
  }

  @Test
  void associate_multi() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();
    Address_ a = new Address_();

    List<Employee> list =
        entityql
            .from(e)
            .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
            .innerJoin(a, on -> on.eq(e.addressId, a.addressId))
            .where(c -> c.eq(d.departmentName, "SALES"))
            .associate(
                e,
                d,
                (employee, department) -> {
                  employee.setDepartment(department);
                  department.getEmployeeList().add(employee);
                })
            .associate(e, a, Employee::setAddress)
            .fetch();

    assertEquals(6, list.size());
    assertTrue(
        list.stream().allMatch(it -> it.getDepartment().getDepartmentName().equals("SALES")));
    assertEquals(list.get(0).getDepartment().getEmployeeList().size(), 6);
    assertTrue(list.stream().allMatch(it -> it.getAddress() != null));
  }

  @Test
  void orderBy() {
    Employee_ e = new Employee_();

    List<Employee> list =
        entityql
            .from(e)
            .orderBy(
                c -> {
                  c.asc(e.departmentId);
                  c.desc(e.salary);
                })
            .fetch();

    assertEquals(14, list.size());
  }

  @Test
  void asSql() {
    Department_ d = new Department_();

    Listable<Department> stmt = entityql.from(d).where(c -> c.eq(d.departmentName, "SALES"));

    Sql<?> sql = stmt.asSql();
    System.out.printf("Raw SQL      : %s\n", sql.getRawSql());
    System.out.printf("Formatted SQL: %s\n", sql.getFormattedSql());
  }

  @Test
  void peek() {
    Department_ d = new Department_();

    entityql
        .from(d)
        .peek(System.out::println)
        .where(c -> c.eq(d.departmentName, "SALES"))
        .peek(System.out::println)
        .orderBy(c -> c.asc(d.location))
        .peek(sql -> System.out.println(sql.getFormattedSql()))
        .fetch();
  }
}
