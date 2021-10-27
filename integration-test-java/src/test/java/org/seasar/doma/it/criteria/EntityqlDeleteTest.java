package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.criteria.Entityql;

@ExtendWith(IntegrationTestEnvironment.class)
public class EntityqlDeleteTest {

  private final Entityql entityql;

  public EntityqlDeleteTest(Config config) {
    this.entityql = new Entityql(config);
  }

  @Test
  void test() {
    Employee_ e = new Employee_();

    Employee employee = entityql.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();

    Result<Employee> result = entityql.delete(e, employee).execute();
    assertEquals(1, result.getCount());
    assertEquals(employee, result.getEntity());

    List<Employee> employees = entityql.from(e).where(c -> c.eq(e.employeeId, 5)).fetch();
    assertTrue(employees.isEmpty());
  }

  @Test
  void suppressOptimisticLockException() {
    Employee_ e = new Employee_();

    Employee employee = entityql.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
    employee.setEmployeeId(100);

    Result<Employee> result =
        entityql
            .delete(e, employee, settings -> settings.setSuppressOptimisticLockException(true))
            .execute();
    assertEquals(0, result.getCount());
  }
}
