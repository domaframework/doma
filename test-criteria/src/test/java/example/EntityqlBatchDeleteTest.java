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

@ExtendWith(Env.class)
public class EntityqlBatchDeleteTest {

  private final Entityql entityql;

  public EntityqlBatchDeleteTest(Config config) {
    this.entityql = new Entityql(config);
  }

  @Test
  void test() {
    Employee_ e = new Employee_();

    List<Employee> employees =
        entityql.from(e).where(c -> c.in(e.employeeId, Arrays.asList(5, 6))).fetch();

    List<Employee> results = entityql.delete(e, employees).execute();
    assertEquals(employees, results);

    List<Employee> employees2 =
        entityql.from(e).where(c -> c.in(e.employeeId, Arrays.asList(5, 6))).fetch();
    assertTrue(employees2.isEmpty());
  }

  @Test
  void empty() {
    Employee_ e = new Employee_();

    List<Employee> results = entityql.delete(e, Collections.emptyList()).execute();
    assertTrue(results.isEmpty());
  }
}
