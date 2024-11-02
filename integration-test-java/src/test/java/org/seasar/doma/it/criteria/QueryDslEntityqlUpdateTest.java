package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.it.entity.IdColumnOnlyEmployee;
import org.seasar.doma.it.entity.IdColumnOnlyEmployee_;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.criteria.QueryDsl;

@ExtendWith(IntegrationTestEnvironment.class)
@Run(unless = Dbms.SQLITE)
public class QueryDslEntityqlUpdateTest {

  private final QueryDsl dsl;

  public QueryDslEntityqlUpdateTest(Config config) {
    this.dsl = new QueryDsl(config);
  }

  @Test
  void test() {
    Employee_ e = new Employee_();

    Employee employee = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
    employee.setEmployeeName("aaa");
    employee.setSalary(new Salary("2000"));

    Result<Employee> result = dsl.update(e).single(employee).execute();
    assertEquals(1, result.getCount());
    assertEquals(employee, result.getEntity());

    Employee employee2 = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
    assertNotNull(employee2);
    assertEquals("aaa", employee2.getEmployeeName());
    assertEquals(0, new BigDecimal("2000").compareTo(employee2.getSalary().getValue()));
  }

  @Test
  void suppressOptimisticLockException() {
    Employee_ e = new Employee_();

    Employee employee = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
    employee.setEmployeeId(100);
    employee.setEmployeeName("aaa");

    Result<Employee> result =
        dsl.update(e, settings -> settings.setSuppressOptimisticLockException(true))
            .single(employee)
            .execute();
    assertEquals(0, result.getCount());
  }

  @Test
  void idColumnOnlyEntity() {
    IdColumnOnlyEmployee_ e = new IdColumnOnlyEmployee_();

    IdColumnOnlyEmployee employee = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();

    Result<IdColumnOnlyEmployee> result = dsl.update(e).single(employee).execute();
    assertEquals(employee, result.getEntity());
    assertEquals(0, result.getCount());
  }
}
