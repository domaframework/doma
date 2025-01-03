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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.entity.IdColumnOnlyEmployee;
import org.seasar.doma.it.entity.IdColumnOnlyEmployee_;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.QueryDsl;

@ExtendWith(IntegrationTestEnvironment.class)
public class QueryDslEntityBatchUpdateTest {

  private final QueryDsl dsl;

  public QueryDslEntityBatchUpdateTest(Config config) {
    this.dsl = new QueryDsl(config);
  }

  @Test
  void test() {
    Employee_ e = new Employee_();

    List<Employee> employees =
        dsl.from(e).where(c -> c.in(e.employeeId, Arrays.asList(5, 6))).fetch();
    employees.forEach(it -> it.setEmployeeName("aaa"));

    BatchResult<Employee> result = dsl.update(e).batch(employees).execute();
    assertArrayEquals(new int[] {1, 1}, result.getCounts());
    assertEquals(employees, result.getEntities());

    List<Employee> employees2 =
        dsl.from(e).where(c -> c.in(e.employeeId, Arrays.asList(5, 6))).fetch();

    assertTrue(employees2.stream().allMatch(it -> "aaa".equals(it.getEmployeeName())));
  }

  @Test
  void suppressOptimisticLockException() {
    Employee_ e = new Employee_();

    List<Employee> employees =
        dsl.from(e).where(c -> c.in(e.employeeId, Arrays.asList(5, 6))).fetch();
    employees.forEach(
        it -> {
          it.setEmployeeId(100);
          it.setEmployeeName("aaa");
        });

    BatchResult<Employee> result =
        dsl.update(e, settings -> settings.setSuppressOptimisticLockException(true))
            .batch(employees)
            .execute();
    assertArrayEquals(new int[] {0, 0}, result.getCounts());
  }

  @Test
  void empty() {
    Employee_ e = new Employee_();

    BatchResult<Employee> result = dsl.update(e).batch(Collections.emptyList()).execute();
    assertTrue(result.getEntities().isEmpty());
  }

  @Test
  void idColumnOnlyEntity() {
    IdColumnOnlyEmployee_ e = new IdColumnOnlyEmployee_();

    IdColumnOnlyEmployee employee = dsl.from(e).where(c -> c.eq(e.employeeId, 5)).fetchOne();

    List<IdColumnOnlyEmployee> employeesList = Collections.singletonList(employee);
    BatchResult<IdColumnOnlyEmployee> result = dsl.update(e).batch(employeesList).execute();
    assertEquals(employeesList, result.getEntities());
    assertEquals(0, result.getCounts().length);
  }
}
