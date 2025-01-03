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
package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Emp;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.statement.Buildable;

class QueryDslEntityInsertTest {

  private final QueryDsl dsl = new QueryDsl(new MockConfig());

  @Test
  void insertInto() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal("1000"));
    emp.setVersion(1);

    Emp_ e = new Emp_();
    Buildable<?> stmt = dsl.insert(e).single(emp);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1)",
        sql.getFormattedSql());
  }

  @Test
  void excludeNull() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName(null);
    emp.setSalary(new BigDecimal("1000"));
    emp.setVersion(1);

    Emp_ e = new Emp_();
    Buildable<?> stmt = dsl.insert(e, settings -> settings.setExcludeNull(true)).single(emp);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "insert into EMP (ID, SALARY, VERSION) values (1, 1000, 1)", sql.getFormattedSql());
  }

  @Test
  void include() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal("1000"));
    emp.setVersion(1);

    Emp_ e = new Emp_();
    Buildable<?> stmt = dsl.insert(e, settings -> settings.include(e.salary)).single(emp);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "insert into EMP (ID, SALARY, VERSION) values (1, 1000, 1)", sql.getFormattedSql());
  }

  @Test
  void exclude() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal("1000"));
    emp.setVersion(1);

    Emp_ e = new Emp_();
    Buildable<?> stmt = dsl.insert(e, settings -> settings.exclude(e.salary)).single(emp);

    Sql<?> sql = stmt.asSql();
    assertEquals("insert into EMP (ID, NAME, VERSION) values (1, 'aaa', 1)", sql.getFormattedSql());
  }
}
