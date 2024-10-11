package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Emp;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.statement.Buildable;

class QueryDslEntityqlUpdateTest {

  private final QueryDsl dsl = new QueryDsl(new MockConfig());

  @Test
  void test() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal("1000"));
    emp.setVersion(1);

    Emp_ e = new Emp_();
    Buildable<?> stmt = dsl.update(e).single(emp);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "update EMP set NAME = 'aaa', SALARY = 1000, VERSION = 1 + 1 where ID = 1 and VERSION = 1",
        sql.getFormattedSql());
  }

  @Test
  void include() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal("1000"));
    emp.setVersion(1);

    Emp_ e = new Emp_();
    Buildable<?> stmt = dsl.update(e, settings -> settings.include(e.salary)).single(emp);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "update EMP set SALARY = 1000, VERSION = 1 + 1 where ID = 1 and VERSION = 1",
        sql.getFormattedSql());
  }

  @Test
  void exclude() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal("1000"));
    emp.setVersion(1);

    Emp_ e = new Emp_();
    Buildable<?> stmt = dsl.update(e, settings -> settings.exclude(e.salary)).single(emp);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "update EMP set NAME = 'aaa', VERSION = 1 + 1 where ID = 1 and VERSION = 1",
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
    Buildable<?> stmt = dsl.update(e, settings -> settings.setIgnoreVersion(true)).single(emp);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "update EMP set NAME = 'aaa', SALARY = 1000, VERSION = 1 where ID = 1",
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
    Buildable<?> stmt = dsl.update(e, settings -> settings.setExcludeNull(true)).single(emp);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "update EMP set SALARY = 1000, VERSION = 1 + 1 where ID = 1 and VERSION = 1",
        sql.getFormattedSql());
  }
}
