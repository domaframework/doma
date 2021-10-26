package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.criteria.Entityql;

@ExtendWith(IntegrationTestEnvironment.class)
public class EntityqlUpdateTest {

  private final Entityql entityql;

  public EntityqlUpdateTest(Config config) {
    this.entityql = new Entityql(config);
  }

  @Test
  void test() {
    Employee_ e = new Employee_();

    Employee employee = entityql.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
    employee.setEmployeeName("aaa");
    employee.setSalary(new Salary("2000"));

    Result<Employee> result = entityql.update(e, employee).execute();
    assertEquals(1, result.getCount());
    assertEquals(employee, result.getEntity());

    Employee employee2 = entityql.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
    assertNotNull(employee2);
    assertEquals("aaa", employee2.getEmployeeName());
    assertEquals(0, new BigDecimal("2000").compareTo(employee2.getSalary().getValue()));
  }

  @Test
  void suppressOptimisticLockException() {
    Employee_ e = new Employee_();

    Employee employee = entityql.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
    employee.setEmployeeId(100);
    employee.setEmployeeName("aaa");

    Result<Employee> result =
        entityql
            .update(e, employee, settings -> settings.setSuppressOptimisticLockException(true))
            .execute();
    assertEquals(0, result.getCount());
  }
}
