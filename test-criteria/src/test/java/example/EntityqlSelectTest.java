package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.statement.Listable;

@ExtendWith(Env.class)
public class EntityqlSelectTest {

  private final Entityql entityql;

  public EntityqlSelectTest(Config config) {
    this.entityql = new Entityql(config);
  }

  @Test
  void all() {
    Employee_ e = new Employee_();

    List<Employee> list = entityql.from(e).getResultList();
    assertEquals(14, list.size());
  }

  @Test
  void where() {
    Employee_ e = new Employee_();

    Listable<Employee> stmt =
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
                });

    List<Employee> list = stmt.getResultList();
    assertEquals(10, list.size());
  }

  @Test
  void where_in() {
    Employee_ e = new Employee_();

    Listable<Employee> stmt =
        entityql
            .from(e)
            .where(c -> c.in(e.employeeId, Arrays.asList(2, 3, 4)))
            .orderBy(c -> c.asc(e.employeeId));

    List<Employee> list = stmt.getResultList();
    assertEquals(3, list.size());
  }

  @Test
  void where_in_subQuery() {
    Employee_ e = new Employee_();
    Employee_ e2 = new Employee_();

    Listable<Employee> stmt =
        entityql
            .from(e)
            .where(c -> c.in(e.employeeId, c.from(e2).select(e2.managerId)))
            .orderBy(c -> c.asc(e.employeeId));

    List<Employee> list = stmt.getResultList();
    assertEquals(6, list.size());
  }

  @Test
  void where_exists_subQuery() {
    Employee_ e = new Employee_();
    Employee_ e2 = new Employee_();

    Listable<Employee> stmt =
        entityql
            .from(e)
            .where(
                c ->
                    c.exists(
                        c.from(e2)
                            .where(c2 -> c2.eq(e.employeeId, e2.managerId))
                            .select(e2.managerId)))
            .orderBy(c -> c.asc(e.employeeId));

    List<Employee> list = stmt.getResultList();
    assertEquals(6, list.size());
  }

  @Test
  void innerJoin() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    Listable<Employee> stmt =
        entityql.from(e).innerJoin(d, on -> on.eq(e.departmentId, d.departmentId));

    List<Employee> list = stmt.getResultList();
    assertEquals(14, list.size());
  }

  @Test
  void leftJoin() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    Listable<Employee> stmt =
        entityql.from(e).leftJoin(d, on -> on.eq(e.departmentId, d.departmentId));

    List<Employee> list = stmt.getResultList();
    assertEquals(14, list.size());
  }

  @Test
  void associate() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    Listable<Employee> stmt =
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
                });

    List<Employee> list = stmt.getResultList();
    assertEquals(6, list.size());
    assertTrue(list.stream().allMatch(it -> it.getDepartment().departmentName.equals("SALES")));
    assertEquals(list.get(0).getDepartment().getEmployeeList().size(), 6);
  }

  @Test
  void associate_multi() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();
    Address_ a = new Address_();

    Listable<Employee> stmt =
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
            .associate(e, a, (employee, address) -> employee.setAddress(address));

    List<Employee> list = stmt.getResultList();
    assertEquals(6, list.size());
    assertTrue(list.stream().allMatch(it -> it.getDepartment().departmentName.equals("SALES")));
    assertEquals(list.get(0).getDepartment().getEmployeeList().size(), 6);
    assertTrue(list.stream().allMatch(it -> it.getAddress() != null));
  }

  @Test
  void orderBy() {
    Employee_ e = new Employee_();

    Listable<Employee> stmt =
        entityql
            .from(e)
            .orderBy(
                c -> {
                  c.asc(e.departmentId);
                  c.desc(e.salary);
                });

    List<Employee> list = stmt.getResultList();

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
}
