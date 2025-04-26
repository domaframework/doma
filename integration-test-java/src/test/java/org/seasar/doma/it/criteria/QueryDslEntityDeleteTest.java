/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.criteria.QueryDsl;

@ExtendWith(IntegrationTestEnvironment.class)
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

  @Test
  void returning() {
    Employee_ e = new Employee_();

    Employee employee = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();

    Result<Employee> result = dsl.delete(e).single(employee).returning().execute();
    assertEquals(1, result.getCount());
    assertNotEquals(employee, result.getEntity());

    var resultEntity = result.getEntity();
    assertEquals(5, resultEntity.getEmployeeId());

    Employee entity = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
    assertNull(entity);
  }

  @Test
  void returning_specificProperties() {
    Employee_ e = new Employee_();

    Employee employee = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();

    Result<Employee> result =
        dsl.delete(e).single(employee).returning(e.employeeId, e.employeeName).execute();
    assertEquals(1, result.getCount());
    assertNotEquals(employee, result.getEntity());

    var resultEntity = result.getEntity();
    assertEquals(5, resultEntity.getEmployeeId());
    assertEquals("MARTIN", resultEntity.getEmployeeName());
    assertNull(resultEntity.getDepartmentId());

    Employee entity = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
    assertNull(entity);
  }

  @Test
  void returning_OptimisticLockException() {
    Employee_ e = new Employee_();

    Employee employee = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
    employee.setEmployeeId(100);

    assertThrows(
        OptimisticLockException.class, () -> dsl.delete(e).single(employee).returning().execute());
  }

  @Test
  void returning_suppressOptimisticLockException() {
    Employee_ e = new Employee_();

    Employee employee = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
    employee.setEmployeeId(100);

    Result<Employee> result =
        dsl.delete(e, settings -> settings.setSuppressOptimisticLockException(true))
            .single(employee)
            .returning()
            .execute();
    assertEquals(0, result.getCount());
    assertNull(result.getEntity());
  }
}
