package example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.statement.Listable;
import org.seasar.doma.jdbc.criteria.statement.Statement;

@ExtendWith(Env.class)
public class EntityqlDeleteTest {

  private final Entityql entityql;

  public EntityqlDeleteTest(Config config) {
    this.entityql = new Entityql(config);
  }

  @Test
  void test() {
    Employee_ e = new Employee_();
    Listable<Employee> select = entityql.from(e).where(c -> c.eq(e.employeeId, 5));

    Employee employee = select.fetchOptional().orElseThrow(AssertionError::new);

    Statement<Employee> delete = entityql.delete(e, employee);
    Employee result = delete.execute();
    assertEquals(employee, result);

    List<Employee> employees = select.fetch();
    assertTrue(employees.isEmpty());
  }
}
