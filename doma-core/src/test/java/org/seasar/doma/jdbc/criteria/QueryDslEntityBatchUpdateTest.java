package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Emp;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.statement.Buildable;

class QueryDslEntityBatchUpdateTest {

  private final QueryDsl dsl = new QueryDsl(new MockConfig());

  @Test
  void test() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal("1000"));
    emp.setVersion(1);

    Emp_ e = new Emp_();
    Buildable<?> stmt = dsl.update(e).batch(Collections.singletonList(emp));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "update EMP set NAME = 'aaa', SALARY = 1000, VERSION = 1 + 1 where ID = 1 and VERSION = 1",
        sql.getFormattedSql());
  }

  @Test
  void ignoreVersion() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal("1000"));
    emp.setVersion(1);

    Emp_ e = new Emp_();
    Buildable<?> stmt =
        dsl.update(e, setting -> setting.setIgnoreVersion(true))
            .batch(Collections.singletonList(emp));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "update EMP set NAME = 'aaa', SALARY = 1000, VERSION = 1 where ID = 1",
        sql.getFormattedSql());
  }

  @Test
  void empty() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = dsl.update(e).batch(Collections.emptyList());

    Sql<?> sql = stmt.asSql();
    assertEquals("This SQL is empty because target entities are empty.", sql.getFormattedSql());
  }
}
