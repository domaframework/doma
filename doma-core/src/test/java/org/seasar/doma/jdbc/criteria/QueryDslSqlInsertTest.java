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

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Dept_;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.statement.Statement;

class QueryDslSqlInsertTest {

  private final QueryDsl dsl = new QueryDsl(new MockConfig());

  @Test
  void insert() {
    Emp_ e = new Emp_();
    Statement<Integer> stmt =
        dsl.insert(e)
            .values(
                c -> {
                  c.value(e.id, 99);
                  c.value(e.name, "aaa");
                  c.value(e.salary, null);
                  c.value(e.version, 1);
                });

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) values (99, 'aaa', null, 1)",
        sql.getFormattedSql());
  }

  @Test
  void insert_select_entity() {
    Emp_ e = new Emp_();
    Statement<Integer> stmt = dsl.insert(e).select(c -> c.from(e).select());

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
        sql.getFormattedSql());
  }

  @Test
  void insert_select_entity_without_select() {
    Emp_ e = new Emp_();
    Statement<Integer> stmt = dsl.insert(e).select(c -> c.from(e));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
        sql.getFormattedSql());
  }

  @Test
  void insert_select_entity_join_select() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Statement<Integer> stmt =
        dsl.insert(e).select(c -> c.from(d).innerJoin(e, on -> on.eq(d.id, e.id)).select(e));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) select t1_.ID, t1_.NAME, t1_.SALARY, t1_.VERSION from CATA.DEPT t0_ inner join EMP t1_ on (t0_.ID = t1_.ID)",
        sql.getFormattedSql());
  }

  @Test
  void insert_select_properties() {
    Emp_ e = new Emp_();
    Statement<Integer> stmt =
        dsl.insert(e).select(c -> c.from(e).select(e.id, e.name, e.salary, e.version));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
        sql.getFormattedSql());
  }
}
