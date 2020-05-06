package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.statement.Listable;
import org.seasar.doma.jdbc.criteria.statement.Statement;

@ExtendWith(Env.class)
public class EntityqlBatchUpdateTest {

  private final Entityql entityql;

  public EntityqlBatchUpdateTest(Config config) {
    this.entityql = new Entityql(config);
  }

  @Test
  void test() {
    Employee_ e = new Employee_();

    Listable<Employee> select =
        entityql.from(e).where(c -> c.in(e.employeeId, Arrays.asList(5, 6)));

    List<Employee> employees = select.fetch();
    employees.forEach(it -> it.setEmployeeName("aaa"));

    Statement<List<Employee>> update = entityql.update(e, employees);
    List<Employee> results = update.execute();
    assertEquals(employees, results);

    assertTrue(select.stream().allMatch(it -> "aaa".equals(it.getEmployeeName())));
  }

  @Test
  void empty() {
    Employee_ e = new Employee_();

    Statement<List<Employee>> update = entityql.update(e, Collections.emptyList());
    List<Employee> results = update.execute();
    assertTrue(results.isEmpty());
  }
}
