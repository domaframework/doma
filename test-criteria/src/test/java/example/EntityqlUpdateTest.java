package example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.statement.Statement;

@ExtendWith(Env.class)
public class EntityqlUpdateTest {

  private final Config config;

  public EntityqlUpdateTest(Config config) {
    this.config = config;
  }

  @Test
  void test() {
    Employee_ e = new Employee_();
    Statement<List<Employee>> select = Entityql.from(e).where(c -> c.eq(e.employeeId, 5));

    Employee employee = select.execute(config).get(0);
    employee.setEmployeeName("aaa");
    employee.setSalary(new Salary("2000"));

    assertEquals(employee, Entityql.update(e, employee).execute(config));

    Employee employee2 = select.execute(config).get(0);
    assertEquals("aaa", employee2.getEmployeeName());
    assertEquals(0, new BigDecimal("2000").compareTo(employee2.getSalary().getValue()));
  }
}
