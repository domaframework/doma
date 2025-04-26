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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.seasar.doma.internal.util.AssertionUtil.assertNull;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.it.entity.IdColumnOnlyEmployee;
import org.seasar.doma.it.entity.IdColumnOnlyEmployee_;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.criteria.QueryDsl;

@ExtendWith(IntegrationTestEnvironment.class)
public class QueryDslEntityUpdateTest {

  private final QueryDsl dsl;

  public QueryDslEntityUpdateTest(Config config) {
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

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  void returning() {
    Employee_ e = new Employee_();

    Employee employee = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
    employee.setEmployeeName("aaa");
    employee.setSalary(new Salary("2000"));

    Result<Employee> result = dsl.update(e).single(employee).returning().execute();
    assertEquals(1, result.getCount());
    assertNotEquals(employee, result.getEntity());

    Employee resultEntity = result.getEntity();
    assertNotNull(resultEntity);
    assertEquals("aaa", resultEntity.getEmployeeName());
    assertEquals(0, new BigDecimal("2000").compareTo(resultEntity.getSalary().getValue()));
    assertEquals(employee.getVersion() + 1, resultEntity.getVersion());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  void returning_OptimisticLockException() {
    Employee_ e = new Employee_();

    Employee employee = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
    employee.setEmployeeId(100);
    employee.setEmployeeName("aaa");

    assertThrows(
        OptimisticLockException.class, () -> dsl.update(e).single(employee).returning().execute());
  }

  @Test
  @Run(unless = {Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE})
  void returning_suppressOptimisticLockException() {
    Employee_ e = new Employee_();

    Employee employee = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();
    employee.setEmployeeId(100);
    employee.setEmployeeName("aaa");

    Result<Employee> result =
        dsl.update(e, settings -> settings.setSuppressOptimisticLockException(true))
            .single(employee)
            .returning()
            .execute();
    assertEquals(0, result.getCount());
    assertNotEquals(employee, result.getEntity());

    Employee resultEntity = result.getEntity();
    assertNull(resultEntity);
  }
}
