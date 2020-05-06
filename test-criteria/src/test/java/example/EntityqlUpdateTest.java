package example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.statement.Listable;
import org.seasar.doma.jdbc.criteria.statement.Statement;

@ExtendWith(Env.class)
public class EntityqlUpdateTest {

  private final Entityql entityql;

  public EntityqlUpdateTest(Config config) {
    this.entityql = new Entityql(config);
  }

  @Test
  void test() {
    Employee_ e = new Employee_();
    Listable<Employee> select = entityql.from(e).where(c -> c.eq(e.employeeId, 5));

    Employee employee = select.getSingleResult().orElseThrow(AssertionError::new);
    employee.setEmployeeName("aaa");
    employee.setSalary(new Salary("2000"));

    Statement<Employee> update = entityql.update(e, employee);
    Employee result = update.execute();
    assertEquals(employee, result);

    Employee employee2 = select.getSingleResult().orElseThrow(AssertionError::new);
    assertEquals("aaa", employee2.getEmployeeName());
    assertEquals(0, new BigDecimal("2000").compareTo(employee2.getSalary().getValue()));
  }
}
