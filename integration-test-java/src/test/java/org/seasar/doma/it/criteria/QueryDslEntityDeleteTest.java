package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.criteria.QueryDsl;

@ExtendWith(IntegrationTestEnvironment.class)
@Run(unless = Dbms.SQLITE)
public class QueryDslEntityDeleteTest {

  private final QueryDsl dsl;

  public QueryDslEntityDeleteTest(Config config) {
    this.dsl = new QueryDsl(config);
  }

  @Test
  void test() {
    Employee_ e = new Employee_();

    Employee employee = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();

    Result<Employee> result = dsl.delete(e).single(employee).execute();
    assertEquals(1, result.getCount());
    assertEquals(employee, result.getEntity());

    List<Employee> employees = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetch();
    assertTrue(employees.isEmpty());
  }

  @Test
  void suppressOptimisticLockException() {
    Employee_ e = new Employee_();

    Employee employee = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
    employee.setEmployeeId(100);

    Result<Employee> result =
        dsl.delete(e, settings -> settings.setSuppressOptimisticLockException(true))
            .single(employee)
            .execute();
    assertEquals(0, result.getCount());
  }
}
