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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.QueryDsl;

@ExtendWith(IntegrationTestEnvironment.class)
public class QueryDslImmutableTest {

  private final QueryDsl dsl;

  public QueryDslImmutableTest(Config config) {
    this.dsl = new QueryDsl(config);
  }

  @Test
  void fetch() {
    Emp_ e = new Emp_();

    List<Emp> list = dsl.from(e).fetch();
    assertEquals(14, list.size());
  }

  @Test
  void associateWith() {
    Emp_ e = new Emp_();
    Emp_ m = new Emp_();
    Dept_ d = new Dept_();

    List<Emp> list =
        dsl.from(e)
            .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
            .leftJoin(m, on -> on.eq(e.managerId, m.employeeId))
            .where(c -> c.eq(d.departmentName, "SALES"))
            .associateWith(e, d, Emp::withDept)
            .associateWith(e, m, Emp::withManager)
            .fetch();

    assertEquals(6, list.size());
    assertTrue(
        list.stream().allMatch(it -> it.getDepartment().getDepartmentName().equals("SALES")));
    assertTrue(list.stream().allMatch(it -> Objects.nonNull(it.getManager())));
  }
}
