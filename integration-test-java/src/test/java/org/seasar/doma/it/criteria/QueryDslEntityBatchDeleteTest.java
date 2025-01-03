package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.QueryDsl;

@ExtendWith(IntegrationTestEnvironment.class)
public class QueryDslEntityBatchDeleteTest {

  private final QueryDsl entityql;

  public QueryDslEntityBatchDeleteTest(Config config) {
    this.entityql = new QueryDsl(config);
  }

  @Test
  void test() {
    Employee_ e = new Employee_();

    List<Employee> employees =
        entityql.from(e).where(c -> c.in(e.employeeId, Arrays.asList(5, 6))).fetch();

    BatchResult<Employee> result = entityql.delete(e).batch(employees).execute();
    assertArrayEquals(new int[] {1, 1}, result.getCounts());
    assertEquals(employees, result.getEntities());

    List<Employee> employees2 =
        entityql.from(e).where(c -> c.in(e.employeeId, Arrays.asList(5, 6))).fetch();
    assertTrue(employees2.isEmpty());
  }

  @Test
  void suppressOptimisticLockException() {
    Employee_ e = new Employee_();

    List<Employee> employees =
        entityql.from(e).where(c -> c.in(e.employeeId, Arrays.asList(5, 6))).fetch();
    employees.forEach(it -> it.setEmployeeId(100));

    BatchResult<Employee> result =
        entityql
            .delete(e, settings -> settings.setSuppressOptimisticLockException(true))
            .batch(employees)
            .execute();
    assertArrayEquals(new int[] {0, 0}, result.getCounts());
  }

  @Test
  void empty() {
    Employee_ e = new Employee_();

    BatchResult<Employee> result = entityql.delete(e).batch(Collections.emptyList()).execute();
    assertTrue(result.getEntities().isEmpty());
  }
}
