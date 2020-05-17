package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Emp;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.statement.Buildable;

class EntityqlUpdateTest {

  private final Entityql entityql = new Entityql(new MockConfig());

  @Test
  void test() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal("1000"));
    emp.setVersion(1);

    Emp_ e = new Emp_();
    Buildable<?> stmt = entityql.update(e, emp);

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
    Buildable<?> stmt = entityql.update(e, emp, settings -> settings.setIgnoreVersion(true));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "update EMP set NAME = 'aaa', SALARY = 1000, VERSION = 1 where ID = 1",
        sql.getFormattedSql());
  }
}
