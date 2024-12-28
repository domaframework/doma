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
