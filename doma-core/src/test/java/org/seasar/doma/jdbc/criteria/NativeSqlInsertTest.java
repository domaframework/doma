package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.statement.Statement;

class NativeSqlInsertTest {

  private final NativeSql nativeSql = new NativeSql(new MockConfig());

  @Test
  void insert() {
    Emp_ e = new Emp_();
    Statement<Integer> stmt =
        nativeSql
            .insert(e)
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
    Statement<Integer> stmt = nativeSql.insert(e).select(c -> c.from(e).select());

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
        sql.getFormattedSql());
  }

  @Test
  void insert_select_properties() {
    Emp_ e = new Emp_();
    Statement<Integer> stmt =
        nativeSql.insert(e).select(c -> c.from(e).select(e.id, e.name, e.salary, e.version));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
        sql.getFormattedSql());
  }
}
