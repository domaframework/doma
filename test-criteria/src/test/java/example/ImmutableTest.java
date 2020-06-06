package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.context.AssociationPair;

@ExtendWith(Env.class)
public class ImmutableTest {

  private final Entityql entityql;

  public ImmutableTest(Config config) {
    this.entityql = new Entityql(config);
  }

  @Test
  void fetch() {
    Emp_ e = new Emp_();

    List<Emp> list = entityql.from(e).fetch();
    assertEquals(14, list.size());
  }

  @Test
  void associateAndReplace() {
    Emp_ e = new Emp_();
    Emp_ m = new Emp_();
    Dept_ d = new Dept_();

    List<Emp> list =
        entityql
            .from(e)
            .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
            .leftJoin(m, on -> on.eq(e.managerId, m.employeeId))
            .where(c -> c.eq(d.departmentName, "SALES"))
            .associateAndReplace(
                e,
                d,
                (emp, dept) -> {
                  Emp newEmp =
                      new Emp(
                          emp.getEmployeeId(),
                          emp.getEmployeeNo(),
                          emp.getEmployeeName(),
                          emp.getManagerId(),
                          emp.getHiredate(),
                          emp.getSalary(),
                          emp.getDepartmentId(),
                          emp.getAddressId(),
                          emp.getVersion(),
                          dept,
                          emp.getManager());
                  return new AssociationPair<>(newEmp, dept);
                })
            .associateAndReplace(
                e,
                m,
                (emp, manager) -> {
                  Emp newEmp =
                      new Emp(
                          emp.getEmployeeId(),
                          emp.getEmployeeNo(),
                          emp.getEmployeeName(),
                          emp.getManagerId(),
                          emp.getHiredate(),
                          emp.getSalary(),
                          emp.getDepartmentId(),
                          emp.getAddressId(),
                          emp.getVersion(),
                          emp.getDepartment(),
                          manager);
                  return new AssociationPair<>(newEmp, manager);
                })
            .fetch();

    assertEquals(6, list.size());
    assertTrue(
        list.stream().allMatch(it -> it.getDepartment().getDepartmentName().equals("SALES")));
    assertTrue(list.stream().allMatch(it -> Objects.nonNull(it.getManager())));
  }
}
